package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import com.tiomadre.farmersassortment.core.item.TableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TableBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<StoolRugType> RUG = EnumProperty.create("rug", StoolRugType.class);
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final VoxelShape LEG_NW = Block.box(1.0D, 0.0D, 1.0D, 3.0D, 12.0D, 3.0D);
    private static final VoxelShape LEG_SW = Block.box(1.0D, 0.0D, 13.0D, 3.0D, 12.0D, 15.0D);
    private static final VoxelShape LEG_NE = Block.box(13.0D, 0.0D, 1.0D, 15.0D, 12.0D, 3.0D);
    private static final VoxelShape LEG_SE = Block.box(13.0D, 0.0D, 13.0D, 15.0D, 12.0D, 15.0D);
    private static final VoxelShape TOP = Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public TableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(RUG, StoolRugType.NONE)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUG, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockGetter level = context.getLevel();

        return this.defaultBlockState()
                .setValue(NORTH, canConnect(level.getBlockState(pos.north())))
                .setValue(EAST, canConnect(level.getBlockState(pos.east())))
                .setValue(SOUTH, canConnect(level.getBlockState(pos.south())))
                .setValue(WEST, canConnect(level.getBlockState(pos.west())));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState,
                                           @NotNull net.minecraft.world.level.LevelAccessor level, @NotNull BlockPos currentPos,
                                           @NotNull BlockPos neighborPos) {
        return switch (direction) {
            case NORTH -> state.setValue(NORTH, canConnect(neighborState));
            case EAST -> state.setValue(EAST, canConnect(neighborState));
            case SOUTH -> state.setValue(SOUTH, canConnect(neighborState));
            case WEST -> state.setValue(WEST, canConnect(neighborState));
            default -> super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        StoolRugType currentRug = state.getValue(RUG);
        StoolRugType heldRug = rugTypeFromItem(heldStack.getItem());

        if (heldRug != null && heldRug != currentRug) {
            if (!level.isClientSide) {
                if (!currentRug.hasRug()) {
                    heldStack.shrink(1);
                } else if (player.isCreative()) {
                    dropRug(level, pos, currentRug);
                } else {
                    heldStack.shrink(1);
                    if (!heldStack.isEmpty()) {
                        dropRug(level, pos, currentRug);
                    }
                }
                level.setBlock(pos, state.setValue(RUG, heldRug), Block.UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.getItem() instanceof ShearsItem && currentRug.hasRug()) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(RUG, StoolRugType.NONE), Block.UPDATE_ALL);
                dropRug(level, pos, currentRug);
                heldStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        return super.getDrops(state, builder).stream()
                .map(stack -> stack.is(this.asItem()) ? TableItem.applyRugToStack(stack, state.getValue(RUG)) : stack)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        return TableItem.applyRugToStack(stack, state.getValue(RUG));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return tableShape(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return tableShape(state);
    }

    private VoxelShape tableShape(BlockState state) {
        VoxelShape shape = TOP;
        if (!state.getValue(NORTH) && !state.getValue(WEST)) {
            shape = Shapes.or(shape, LEG_NW);
        }
        if (!state.getValue(SOUTH) && !state.getValue(WEST)) {
            shape = Shapes.or(shape, LEG_SW);
        }
        if (!state.getValue(NORTH) && !state.getValue(EAST)) {
            shape = Shapes.or(shape, LEG_NE);
        }
        if (!state.getValue(SOUTH) && !state.getValue(EAST)) {
            shape = Shapes.or(shape, LEG_SE);
        }
        return shape;
    }

    private void dropRug(Level level, BlockPos pos, StoolRugType rugType) {
        Item item = rugType.rugItem();
        if (item != null && level instanceof ServerLevel serverLevel) {
            ItemEntity drop = new ItemEntity(serverLevel, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(item));
            serverLevel.addFreshEntity(drop);
        }
    }
    private boolean canConnect(BlockState neighborState) {
        return neighborState.is(this);
    }
    @Nullable
    private StoolRugType rugTypeFromItem(Item item) {
        if (!(item instanceof BlockItem blockItem)) {
            return null;
        }

        String path = blockItem.getBlock().builtInRegistryHolder().key().location().getPath();
        if (path.equals("canvas_rug")) {
            return StoolRugType.CANVAS;
        }
        for (StoolRugType value : StoolRugType.values()) {
            if (path.equals(value.getSerializedName() + "_canvas_rug")) {
                return value;
            }
        }
        return null;
    }

}
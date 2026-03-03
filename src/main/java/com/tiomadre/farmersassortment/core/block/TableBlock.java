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
    private static final VoxelShape BASE_SHAPE = Shapes.or(
            Block.box(1.0D, 0.0D, 1.0D, 3.0D, 10.0D, 3.0D),
            Block.box(1.0D, 0.0D, 13.0D, 3.0D, 10.0D, 15.0D),
            Block.box(13.0D, 0.0D, 1.0D, 15.0D, 10.0D, 3.0D),
            Block.box(13.0D, 0.0D, 13.0D, 15.0D, 10.0D, 15.0D),
            Block.box(0.0D, 10.0D, 0.0D, 16.0D, 14.0D, 16.0D)
    );

    public TableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(RUG, StoolRugType.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, RUG);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
        return BASE_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return BASE_SHAPE;
    }

    private void dropRug(Level level, BlockPos pos, StoolRugType rugType) {
        Item item = rugType.rugItem();
        if (item != null && level instanceof ServerLevel serverLevel) {
            ItemEntity drop = new ItemEntity(serverLevel, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(item));
            serverLevel.addFreshEntity(drop);
        }
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
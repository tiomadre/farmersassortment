package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import net.minecraft.world.level.storage.loot.LootParams;


import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoolBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<StoolRugType> RUG = EnumProperty.create("rug", StoolRugType.class);
    private static final VoxelShape SEAT_SHAPE = Block.box(0.0D, 3.0D, 6.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape LEG_NW_UPPER = Block.box(0.0D, 1.0D, 6.0D, 2.0D, 3.0D, 8.0D);
    private static final VoxelShape LEG_NW_LOWER = Block.box(0.0D, 0.0D, 6.0D, 2.0D, 1.0D, 8.0D);
    private static final VoxelShape LEG_NE_UPPER = Block.box(14.0D, 1.0D, 6.0D, 16.0D, 3.0D, 8.0D);
    private static final VoxelShape LEG_NE_LOWER = Block.box(14.0D, 0.0D, 6.0D, 16.0D, 1.0D, 8.0D);
    private static final VoxelShape LEG_SW_UPPER = Block.box(0.0D, 1.0D, 14.0D, 2.0D, 3.0D, 16.0D);
    private static final VoxelShape LEG_SW_LOWER = Block.box(0.0D, 0.0D, 14.0D, 2.0D, 1.0D, 16.0D);
    private static final VoxelShape LEG_SE_UPPER = Block.box(14.0D, 1.0D, 14.0D, 16.0D, 3.0D, 16.0D);
    private static final VoxelShape LEG_SE_LOWER = Block.box(14.0D, 0.0D, 14.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape BASE_SHAPE = Shapes.or(SEAT_SHAPE, LEG_NW_UPPER, LEG_NW_LOWER, LEG_NE_UPPER, LEG_NE_LOWER, LEG_SW_UPPER, LEG_SW_LOWER, LEG_SE_UPPER, LEG_SE_LOWER);
    private static final VoxelShape RUG_EXTRUDE_NORTH = Block.box(0.0D, 4.0D, 4.0D, 16.0D, 5.0D, 6.0D);
    private static final VoxelShape RUGGED_SHAPE = Shapes.or(BASE_SHAPE, RUG_EXTRUDE_NORTH);
    private static final Map<Direction, VoxelShape> BASE_SHAPES = createDirectionalShapes(BASE_SHAPE);
    private static final Map<Direction, VoxelShape> RUGGED_SHAPES = createDirectionalShapes(RUGGED_SHAPE);
    private static final String STOOL_SEAT_TAG = "farmersassortment_stool_seat";
    public StoolBlock(Properties properties) {
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
                if (currentRug.hasRug()) {
                    dropRug(level, pos, currentRug);
                }
                level.setBlock(pos, state.setValue(RUG, heldRug), Block.UPDATE_ALL);
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
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

        if (!player.isSecondaryUseActive()) {
            return sit(level, pos, player);
        }

        if (heldStack.isEmpty()) {
            if (!level.isClientSide) {
                clearSeat(level, pos);
                level.setBlock(pos, state.setValue(FACING, state.getValue(FACING).getOpposite()), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.9F, .75F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            clearSeat(level, pos);

        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        return super.getDrops(state, builder).stream()
                .map(stack -> stack.is(this.asItem()) ? StoolItem.applyRugToStack(stack, state.getValue(RUG)) : stack)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        return StoolItem.applyRugToStack(stack, state.getValue(RUG));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getHitboxShape(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getHitboxShape(state);
    }

    private VoxelShape getHitboxShape(BlockState state) {
        Direction facing = state.getValue(FACING);
        boolean rugged = state.getValue(RUG).hasRug();
        return rugged ? RUGGED_SHAPES.get(facing) : BASE_SHAPES.get(facing);
    }

    private static Map<Direction, VoxelShape> createDirectionalShapes(VoxelShape northShape) {
        EnumMap<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.NORTH, northShape);
        shapes.put(Direction.EAST, rotateShapeClockwise(northShape));
        shapes.put(Direction.SOUTH, rotateShapeClockwise(shapes.get(Direction.EAST)));
        shapes.put(Direction.WEST, rotateShapeClockwise(shapes.get(Direction.SOUTH)));
        return shapes;
    }

    private static VoxelShape rotateShapeClockwise(VoxelShape shape) {
        VoxelShape[] rotated = new VoxelShape[]{Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                rotated[0] = Shapes.or(rotated[0], Shapes.box(1.0D - maxZ, minY, minX, 1.0D - minZ, maxY, maxX)));
        return rotated[0];
    }

    private InteractionResult sit(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ArmorStand seat = getSeat(level, pos);
        if (seat == null) {
            seat = new ArmorStand(level, pos.getX() + 0.5D, pos.getY() + 0.15D, pos.getZ() + 0.5D);
            seat.setInvisible(true);
            seat.setNoGravity(true);
            seat.setInvulnerable(true);
            seat.setSilent(true);
            applyMarkerSeatFlag(seat);
            seat.getPersistentData().putBoolean(STOOL_SEAT_TAG, true);
            seat.getPersistentData().putLong("stool_pos", pos.asLong());
            level.addFreshEntity(seat);
        }

        if (!seat.getPassengers().isEmpty()) {
            return InteractionResult.PASS;
        }

        player.startRiding(seat, false);
        return InteractionResult.CONSUME;
    }
    private void applyMarkerSeatFlag(ArmorStand seat) {
        CompoundTag data = new CompoundTag();
        seat.saveWithoutId(data);
        data.putBoolean("Marker", true);
        seat.load(data);
    }

    @Nullable
    private ArmorStand getSeat(Level level, BlockPos pos) {
        AABB box = new AABB(pos).inflate(0.3D, 0.5D, 0.3D);
        List<ArmorStand> seats = level.getEntitiesOfClass(ArmorStand.class, box, entity ->
                entity.getPersistentData().getBoolean(STOOL_SEAT_TAG)
                        && entity.getPersistentData().getLong("stool_pos") == pos.asLong());
        return seats.isEmpty() ? null : seats.get(0);
    }

    private void clearSeat(Level level, BlockPos pos) {
        ArmorStand seat = getSeat(level, pos);
        if (seat != null) {
            for (Entity passenger : seat.getPassengers()) {
                passenger.stopRiding();
            }
            seat.discard();
        }
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
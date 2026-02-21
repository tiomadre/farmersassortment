package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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

import javax.annotation.Nullable;
import java.util.List;

public class StoolBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<StoolRugType> RUG = EnumProperty.create("rug", StoolRugType.class);
    private static final VoxelShape SHAPE = Block.box(0.0D, 3.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape LEG_NW = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 3.0D, 2.0D);
    private static final VoxelShape LEG_NE = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 3.0D, 2.0D);
    private static final VoxelShape LEG_SW = Block.box(0.0D, 0.0D, 14.0D, 2.0D, 3.0D, 16.0D);
    private static final VoxelShape LEG_SE = Block.box(14.0D, 0.0D, 14.0D, 16.0D, 3.0D, 16.0D);
    private static final VoxelShape FULL_SHAPE = Shapes.or(SHAPE, LEG_NW, LEG_NE, LEG_SW, LEG_SE);
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

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            clearSeat(level, pos);
            StoolRugType rug = state.getValue(RUG);
            if (rug.hasRug()) {
                dropRug(level, pos, rug);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return FULL_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return FULL_SHAPE;
    }

    private InteractionResult sit(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ArmorStand seat = getSeat(level, pos);
        if (seat == null) {
            seat = new ArmorStand(level, pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
            seat.setInvisible(true);
            seat.setNoGravity(true);
            seat.setInvulnerable(true);
            seat.setSilent(true);
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
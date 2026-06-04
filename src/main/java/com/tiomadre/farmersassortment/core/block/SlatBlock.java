package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SlatBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape HORIZONTAL_NORTH_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 1.0D, 16.0D, 1.0D, 4.0D),
            Block.box(0.0D, 0.0D, 5.0D, 16.0D, 1.0D, 8.0D),
            Block.box(0.0D, 0.0D, 9.0D, 16.0D, 1.0D, 12.0D),
            Block.box(0.0D, 0.0D, 13.0D, 16.0D, 1.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 2.0D, 1.0D, 1.0D),
            Block.box(14.0D, 0.0D, 0.0D, 16.0D, 1.0D, 1.0D),
            Block.box(0.0D, 0.0D, 4.0D, 2.0D, 1.0D, 5.0D),
            Block.box(14.0D, 0.0D, 4.0D, 16.0D, 1.0D, 5.0D),
            Block.box(0.0D, 0.0D, 8.0D, 2.0D, 1.0D, 9.0D),
            Block.box(14.0D, 0.0D, 8.0D, 16.0D, 1.0D, 9.0D),
            Block.box(0.0D, 0.0D, 12.0D, 2.0D, 1.0D, 13.0D),
            Block.box(14.0D, 0.0D, 12.0D, 16.0D, 1.0D, 13.0D)
    );
    private static final VoxelShape HORIZONTAL_EAST_SHAPE = rotateShapeY(HORIZONTAL_NORTH_SHAPE, 1);

    private static final VoxelShape EAST_SHAPE = Shapes.or(
            Block.box(15.0D, 1.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(15.0D, 5.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(15.0D, 9.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(15.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(15.0D, 0.0D, 0.0D, 16.0D, 1.0D, 2.0D),
            Block.box(15.0D, 0.0D, 14.0D, 16.0D, 1.0D, 16.0D),
            Block.box(15.0D, 4.0D, 0.0D, 16.0D, 5.0D, 2.0D),
            Block.box(15.0D, 4.0D, 14.0D, 16.0D, 5.0D, 16.0D),
            Block.box(15.0D, 8.0D, 0.0D, 16.0D, 9.0D, 2.0D),
            Block.box(15.0D, 8.0D, 14.0D, 16.0D, 9.0D, 16.0D),
            Block.box(15.0D, 12.0D, 0.0D, 16.0D, 13.0D, 2.0D),
            Block.box(15.0D, 12.0D, 14.0D, 16.0D, 13.0D, 16.0D)
    );
    private static final VoxelShape WEST_SHAPE = Shapes.or(
            Block.box(0.0D, 1.0D, 0.0D, 1.0D, 4.0D, 16.0D),
            Block.box(0.0D, 5.0D, 0.0D, 1.0D, 8.0D, 16.0D),
            Block.box(0.0D, 9.0D, 0.0D, 1.0D, 12.0D, 16.0D),
            Block.box(0.0D, 13.0D, 0.0D, 1.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 2.0D),
            Block.box(0.0D, 0.0D, 14.0D, 1.0D, 1.0D, 16.0D),
            Block.box(0.0D, 4.0D, 0.0D, 1.0D, 5.0D, 2.0D),
            Block.box(0.0D, 4.0D, 14.0D, 1.0D, 5.0D, 16.0D),
            Block.box(0.0D, 8.0D, 0.0D, 1.0D, 9.0D, 2.0D),
            Block.box(0.0D, 8.0D, 14.0D, 1.0D, 9.0D, 16.0D),
            Block.box(0.0D, 12.0D, 0.0D, 1.0D, 13.0D, 2.0D),
            Block.box(0.0D, 12.0D, 14.0D, 1.0D, 13.0D, 16.0D)
    );
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(
            Block.box(0.0D, 1.0D, 15.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 5.0D, 15.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 9.0D, 15.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 13.0D, 15.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 15.0D, 2.0D, 1.0D, 16.0D),
            Block.box(14.0D, 0.0D, 15.0D, 16.0D, 1.0D, 16.0D),
            Block.box(0.0D, 4.0D, 15.0D, 2.0D, 5.0D, 16.0D),
            Block.box(14.0D, 4.0D, 15.0D, 16.0D, 5.0D, 16.0D),
            Block.box(0.0D, 8.0D, 15.0D, 2.0D, 9.0D, 16.0D),
            Block.box(14.0D, 8.0D, 15.0D, 16.0D, 9.0D, 16.0D),
            Block.box(0.0D, 12.0D, 15.0D, 2.0D, 13.0D, 16.0D),
            Block.box(14.0D, 12.0D, 15.0D, 16.0D, 13.0D, 16.0D)
    );
    private static final VoxelShape NORTH_SHAPE = Shapes.or(
            Block.box(0.0D, 1.0D, 0.0D, 16.0D, 4.0D, 1.0D),
            Block.box(0.0D, 5.0D, 0.0D, 16.0D, 8.0D, 1.0D),
            Block.box(0.0D, 9.0D, 0.0D, 16.0D, 12.0D, 1.0D),
            Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 1.0D),
            Block.box(0.0D, 0.0D, 0.0D, 2.0D, 1.0D, 1.0D),
            Block.box(14.0D, 0.0D, 0.0D, 16.0D, 1.0D, 1.0D),
            Block.box(0.0D, 4.0D, 0.0D, 2.0D, 5.0D, 1.0D),
            Block.box(14.0D, 4.0D, 0.0D, 16.0D, 5.0D, 1.0D),
            Block.box(0.0D, 8.0D, 0.0D, 2.0D, 9.0D, 1.0D),
            Block.box(14.0D, 8.0D, 0.0D, 16.0D, 9.0D, 1.0D),
            Block.box(0.0D, 12.0D, 0.0D, 2.0D, 13.0D, 1.0D),
            Block.box(14.0D, 12.0D, 0.0D, 16.0D, 13.0D, 1.0D)
    );

    public SlatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL, false)
                .setValue(WATERLOGGED, false));
    }

    private static VoxelShape rotateShapeY(VoxelShape shape, int quarterTurns) {
        List<double[]> boxes = new ArrayList<>();
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> boxes.add(new double[]{minX, minY, minZ, maxX, maxY, maxZ}));

        VoxelShape rotated = Shapes.empty();
        int turns = Math.floorMod(quarterTurns, 4);

        for (double[] box : boxes) {
            double minX = box[0];
            double minY = box[1];
            double minZ = box[2];
            double maxX = box[3];
            double maxY = box[4];
            double maxZ = box[5];

            for (int i = 0; i < turns; i++) {
                double rotatedMinX = 1.0D - maxZ;
                double rotatedMaxX = 1.0D - minZ;
                double rotatedMinZ = minX;
                double rotatedMaxZ = maxX;

                minX = rotatedMinX;
                maxX = rotatedMaxX;
                minZ = rotatedMinZ;
                maxZ = rotatedMaxZ;
            }

            rotated = Shapes.or(rotated, Shapes.box(minX, minY, minZ, maxX, maxY, maxZ));
        }

        return rotated;
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
        return false;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos placedPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(placedPos);
        BlockState supportState = context.getLevel().getBlockState(placedPos.relative(clickedFace.getOpposite()));

        if (clickedFace == Direction.UP && supportState.getBlock() instanceof SlatBlock) {
            return supportState.getValue(VERTICAL) ? supportState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER) : null;
        }

        boolean vertical = clickedFace.getAxis().isHorizontal();
        Direction facing = vertical ? clickedFace.getOpposite() : context.getHorizontalDirection();

        return this.defaultBlockState()
                .setValue(VERTICAL, vertical)
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VERTICAL, WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (!state.getValue(VERTICAL)) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? HORIZONTAL_EAST_SHAPE : HORIZONTAL_NORTH_SHAPE;
        }

        return switch (state.getValue(FACING)) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getShape(state, level, pos, context);
    }
    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!state.getValue(VERTICAL) && !entity.isSteppingCarefully() && entity.tickCount % 4 == 0
                && entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5D) {
            playSlatStepSound(level, pos, state, entity, 0.75F);
        }
        super.stepOn(level, pos, state, entity);
    }
    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (state.getValue(VERTICAL) && entity instanceof LivingEntity && entity.tickCount % 8 == 0
                && (entity.horizontalCollision || Math.abs(entity.getDeltaMovement().y) > 0.01D)) {
            playSlatStepSound(level, pos, state, entity, 0.35F);
        }
        super.entityInside(state, level, pos, entity);
    }

    private void playSlatStepSound(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity, float volumeMultiplier) {
        if (level.isClientSide || entity.isSilent()) {
            return;
        }

        SoundType soundType = state.getSoundType(level, pos, entity);
        level.playSound(null, pos, soundType.getStepSound(), SoundSource.BLOCKS, soundType.getVolume() * volumeMultiplier, soundType.getPitch());
    }

    @Override
    public boolean isLadder(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        return state.getValue(VERTICAL);
    }
}

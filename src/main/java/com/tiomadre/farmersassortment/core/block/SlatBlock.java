package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SlatBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");
    public static final BooleanProperty CEILING = BlockStateProperties.HANGING;
    public static final BooleanProperty JOINED = BooleanProperty.create("joined");


    private static final VoxelShape HORIZONTAL_NORTH_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 3.0D),
            Block.box(0.0D, 0.0D, 4.0D, 16.0D, 1.0D, 7.0D),
            Block.box(0.0D, 0.0D, 8.0D, 16.0D, 1.0D, 11.0D),
            Block.box(0.0D, 0.0D, 12.0D, 16.0D, 1.0D, 15.0D),
            Block.box(0.0D, 0.0D, 3.0D, 2.0D, 1.0D, 4.0D),
            Block.box(14.0D, 0.0D, 3.0D, 16.0D, 1.0D, 4.0D),
            Block.box(0.0D, 0.0D, 7.0D, 2.0D, 1.0D, 8.0D),
            Block.box(14.0D, 0.0D, 7.0D, 16.0D, 1.0D, 8.0D),
            Block.box(0.0D, 0.0D, 11.0D, 2.0D, 1.0D, 12.0D),
            Block.box(14.0D, 0.0D, 11.0D, 16.0D, 1.0D, 12.0D),
            Block.box(0.0D, 0.0D, 15.0D, 2.0D, 1.0D, 16.0D),
            Block.box(14.0D, 0.0D, 15.0D, 16.0D, 1.0D, 16.0D)
    );

    private static final VoxelShape HORIZONTAL_EAST_SHAPE = rotateShapeY(HORIZONTAL_NORTH_SHAPE, 1);
    private static final VoxelShape HORIZONTAL_SOUTH_SHAPE = rotateShapeY(HORIZONTAL_NORTH_SHAPE, 2);
    private static final VoxelShape HORIZONTAL_WEST_SHAPE = rotateShapeY(HORIZONTAL_NORTH_SHAPE, 3);

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

    public SlatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL, false)
                .setValue(CEILING, false)
                .setValue(JOINED, false));
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
        boolean placingVertical = context.getClickedFace().getAxis().isHorizontal();
        return !state.getValue(JOINED)
                && context.getItemInHand().is(this.asItem())
                && state.getValue(VERTICAL) != placingVertical;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos placedPos = context.getClickedPos();

        BlockState existingState = context.getLevel().getBlockState(placedPos);
        if (existingState.getBlock() instanceof SlatBlock
                && existingState.hasProperty(VERTICAL)
                && existingState.hasProperty(FACING)
                && existingState.hasProperty(CEILING)
                && existingState.hasProperty(JOINED)) {
            boolean placingVertical = clickedFace.getAxis().isHorizontal();
            if (!existingState.getValue(JOINED) && existingState.getValue(VERTICAL) != placingVertical) {
                BlockState joinedState = existingState.setValue(JOINED, true);
                if (existingState.getValue(VERTICAL)) {
                    boolean ceiling = clickedFace == Direction.DOWN;
                    joinedState = joinedState.setValue(CEILING, ceiling);
                }

                return joinedState;
            }
        }

        BlockPos supportPos = placedPos.relative(clickedFace.getOpposite());
        BlockState supportState = context.getLevel().getBlockState(supportPos);
        if (supportState.getBlock() instanceof SlatBlock
                && supportState.hasProperty(VERTICAL)
                && supportState.hasProperty(FACING)
                && supportState.hasProperty(CEILING)) {
            return this.defaultBlockState()
                    .setValue(VERTICAL, supportState.getValue(VERTICAL))
                    .setValue(FACING, supportState.getValue(FACING))
                    .setValue(CEILING, supportState.getValue(CEILING))
                    .setValue(JOINED, false);
        }

        boolean vertical = clickedFace.getAxis().isHorizontal();
        Direction facing = vertical ? clickedFace.getOpposite() : context.getHorizontalDirection();
        boolean ceiling = !vertical && clickedFace == Direction.DOWN;
        return this.defaultBlockState()
                .setValue(VERTICAL, vertical)
                .setValue(FACING, facing)
                .setValue(CEILING, ceiling)
                .setValue(JOINED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VERTICAL, CEILING, JOINED);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape floorShape = switch (facing) {
            case EAST -> HORIZONTAL_EAST_SHAPE;
            case SOUTH -> HORIZONTAL_SOUTH_SHAPE;
            case WEST -> HORIZONTAL_WEST_SHAPE;
            default -> HORIZONTAL_NORTH_SHAPE;
        };
        VoxelShape horizontalShape = state.getValue(CEILING) ? floorShape.move(0.0D, 15.0D / 16.0D, 0.0D) : floorShape;

        VoxelShape verticalShape = switch (facing) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };

        if (state.getValue(JOINED)) {
            return Shapes.or(horizontalShape, verticalShape);
        }

        return state.getValue(VERTICAL) ? verticalShape : horizontalShape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getShape(state, level, pos, context);
    }

    @Override
    public boolean isLadder(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (!state.getValue(VERTICAL)) {
            return false;
        }

        Direction facing = state.getValue(FACING);
        BlockPos backPos = pos.relative(facing.getOpposite());
        BlockState backState = level.getBlockState(backPos);
        if (!backState.isFaceSturdy(level, backPos, facing)) {
            return true;
        }

        double localX = entity.getX() - pos.getX();
        double localZ = entity.getZ() - pos.getZ();
        return switch (facing) {
            case NORTH -> localZ <= 0.5D;
            case SOUTH -> localZ >= 0.5D;
            case WEST -> localX <= 0.5D;
            case EAST -> localX >= 0.5D;
            default -> true;
        };
    }
}
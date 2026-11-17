package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.EnumMap;
import java.util.Map;

public class CandiedPumpkinBlock extends FeastBlock {
    private static final Map<Direction, VoxelShape[]> SHAPES = createShapes();

    public CandiedPumpkinBlock(Properties properties) {
        super(properties, FAItems.CANDIED_YELLOW_PUMPKIN, true);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING))[state.getValue(SERVINGS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    private static Map<Direction, VoxelShape[]> createShapes() {
        VoxelShape platter = box(1, 0, 1, 15, 2, 15);
        VoxelShape red = box(2, 2, 6, 9, 4, 10);
        VoxelShape orange = box(9, 2, 3, 13, 4, 10);
        VoxelShape white = box(2, 2, 2, 9, 4, 6);
        VoxelShape yellow = box(4.5, 2, 10, 11.5, 4, 14);

        VoxelShape tiltedRed = Shapes.or(
                box(2, 4, 4.59, 9, 5.42, 7.42),
                box(2, 5.42, 4.59, 9, 6.83, 8.83),
                box(2, 6.83, 6, 9, 8.25, 10.25));
        VoxelShape tiltedOrange = Shapes.or(
                box(9, 1.32, 2.76, 13, 3.09, 5),
                box(9, 2.18, 2.76, 13, 4.12, 7.5),
                box(9, 3.79, 5, 13, 5.85, 10));
        VoxelShape[] north = {
                platter,
                Shapes.or(platter, red),
                Shapes.or(platter, red, orange),
                Shapes.or(platter, white, tiltedRed, orange),
                Shapes.or(platter, white, tiltedRed, tiltedOrange, yellow)
        };

        EnumMap<Direction, VoxelShape[]> shapes = new EnumMap<>(Direction.class);
        Direction facing = Direction.NORTH;
        VoxelShape[] current = north;
        for (int turn = 0; turn < 4; turn++) {
            shapes.put(facing, current);
            if (turn < 3) {
                VoxelShape[] rotated = new VoxelShape[current.length];
                for (int servings = 0; servings < current.length; servings++) {
                    rotated[servings] = rotateClockwise(current[servings]);
                }
                current = rotated;
                facing = facing.getClockWise();
            }
        }
        return shapes;
    }

    private static VoxelShape rotateClockwise(VoxelShape shape) {
        VoxelShape[] rotated = {Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                rotated[0] = Shapes.or(rotated[0],
                        Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
        return rotated[0];
    }

    @Override
    public ItemStack getServingItem(BlockState state) {
        return switch (state.getValue(SERVINGS)) {
            case 4 -> new ItemStack(FAItems.CANDIED_YELLOW_PUMPKIN.get());
            case 3 -> new ItemStack(FAItems.CANDIED_WHITE_PUMPKIN.get());
            case 2 -> new ItemStack(FAItems.CANDIED_ORANGE_PUMPKIN.get());
            case 1 -> new ItemStack(FAItems.CANDIED_RED_PUMPKIN.get());
            default -> ItemStack.EMPTY;
        };
    }
}

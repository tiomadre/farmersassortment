package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.RopeFenceBlock;

public class NettedFenceBlock extends RopeFenceBlock {
    public NettedFenceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(WATERLOGGED) ? getShape(state, level, pos, context) : super.getCollisionShape(state, level, pos, context);
    }
}
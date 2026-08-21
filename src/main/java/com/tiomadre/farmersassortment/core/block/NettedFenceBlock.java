package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class NettedFenceBlock extends VariantRopeFenceBlock {
    public NettedFenceBlock(Properties properties, Supplier<? extends Block> gate) {
        super(properties, gate);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(WATERLOGGED)
                ? super.getCollisionShape(state, level, pos, context)
                : Shapes.empty();
    }
}
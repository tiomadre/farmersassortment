package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.RopeFenceBlock;

import java.util.function.Supplier;

public class VariantRopeFenceBlock extends RopeFenceBlock {
    private final Supplier<? extends Block> gate;

    public VariantRopeFenceBlock(Properties properties, Supplier<? extends Block> gate) {
        super(properties);
        this.gate = gate;
    }

    @Override
    protected boolean isSameFence(BlockState state) {
        return state.is(this);
    }

    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
        boolean isMatchingGate = state.is(gate.get()) && FenceGateBlock.connectsToDirection(state, direction);
        return (!isExceptionForConnection(state) && isSideSolid) || isSameFence(state) || isMatchingGate;
    }
}
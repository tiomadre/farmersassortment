package com.tiomadre.farmersassortment.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class UniqueStoveBlock extends StoveBlock {
    public UniqueStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Direction facing = state.getValue(FACING);
        if (hitResult.getDirection() != facing) {
            return InteractionResult.PASS;
        }

        boolean lit = state.getValue(LIT);
        if (!level.isClientSide) {
            BlockState updatedState = state.setValue(LIT, !lit);
            level.setBlock(pos, updatedState, 3);

            if (lit) {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7F, 1.0F);
            } else {
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.8F, 1.0F);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.8F, 0.8F);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

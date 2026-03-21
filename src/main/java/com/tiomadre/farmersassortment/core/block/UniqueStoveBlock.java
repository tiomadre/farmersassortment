package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class UniqueStoveBlock extends StoveBlock {
    public UniqueStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        if (!state.is(FABlocks.ALABASTER_STOVE.get())) {
            super.animateTick(state, level, pos, random);
            return;
        }

        if (random.nextDouble() < 0.1D) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

        Direction direction = state.getValue(FACING);
        Direction.Axis axis = direction.getAxis();
        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + random.nextDouble() * 6.0D / 16.0D;
        double centerZ = pos.getZ() + 0.5D;
        double frontOffset = 0.52D;
        double sideOffset = random.nextDouble() * 0.6D - 0.3D;
        double particleX = axis == Direction.Axis.X ? centerX + direction.getStepX() * frontOffset : centerX + sideOffset;
        double particleZ = axis == Direction.Axis.Z ? centerZ + direction.getStepZ() * frontOffset : centerZ + sideOffset;

        level.addParticle(FAParticleTypes.ALABASTER_STOVE_FIRE.get(), particleX, centerY, particleZ, 0.0D, 0.002D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, particleX, centerY, particleZ, 0.0D, 0.0D, 0.0D);
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
                level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.8F, 1.8F);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.4F, 2F);
            } else {
                level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.8F, 1.8F);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.4F, 2F);

            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
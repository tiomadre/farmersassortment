package com.tiomadre.farmersassortment.core.event;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AlabasterSounds {
    private static final float ALABASTER_ACCENT_VOLUME = 0.40F;
    private static final float ALABASTER_ACCENT_PITCH = 0.60F;

    private AlabasterSounds() {
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockState placedState = event.getPlacedBlock();
        if (isAlabasterSoundType(placedState)) {
            playAccent(level, event.getPos(), SoundEvents.AMETHYST_BLOCK_PLACE);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onBreak(BlockEvent.BreakEvent event) {
        Level level = event.getPlayer().level();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (isAlabasterSoundType(state)) {
            playAccent(level, pos, SoundEvents.AMETHYST_BLOCK_BREAK);
        }
    }

    private static boolean isAlabasterSoundType(BlockState state) {
        return state.getSoundType() == FABlocks.ALABASTER_SOUND_TYPE;
    }

    private static void playAccent(Level level, BlockPos pos, SoundEvent soundEvent) {
        level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, ALABASTER_ACCENT_VOLUME, ALABASTER_ACCENT_PITCH);
    }
}
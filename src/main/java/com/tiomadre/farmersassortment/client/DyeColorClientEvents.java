package com.tiomadre.farmersassortment.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DyeColorClientEvents {
    private DyeColorClientEvents() {}

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex == 0 && level != null && pos != null
                    && level.getBlockEntity(pos) instanceof RopeColorBlockEntity rope) {
                int color = rope.getColor();
                return state.is(FABlocks.DIVIDER.get()) ? DividerColor(color) : color;
            }
            return -1;
        }, ModBlocks.ROPE_FENCE.get(), ModBlocks.ROPE_FENCE_GATE.get(),
                FABlocks.DIVIDER.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? DyeColors.getColor(stack) : -1,
                ModBlocks.ROPE_FENCE.get().asItem(), ModBlocks.ROPE_FENCE_GATE.get().asItem());
    }

    private static int DividerColor(int color) {
        if (color < 0) return -1;

        float strength = 0.3F;
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;

        red = Math.round(255 + (red - 255) * strength);
        green = Math.round(255 + (green - 255) * strength);
        blue = Math.round(255 + (blue - 255) * strength);

        return red << 16 | green << 8 | blue;
    }
}

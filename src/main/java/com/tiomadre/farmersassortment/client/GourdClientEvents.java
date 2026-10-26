package com.tiomadre.farmersassortment.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FAGourds;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GourdClientEvents {
    private GourdClientEvents() {
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(FAGourds.CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(FAGourds.PILE.get(), RenderType.cutout());
            ItemProperties.register(FAGourds.GOURD.get(), new ResourceLocation(FarmersAssortment.MOD_ID, "variant"),
                    (stack, level, entity, seed) -> FAGourds.getVariant(stack).ordinal());
        });
    }

    @SubscribeEvent
    public static void colors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> {
            int age = Math.min(7, state.getValue(FAGourds.GourdCropBlock.AGE));
            return (age * 32 << 16) | ((255 - age * 8) << 8) | age * 4;
        }, FAGourds.CROP.get());
    }
}

package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.client.renderer.ButcherBlockCabinetRenderer;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FAClientEvents {
    private FAClientEvents() {
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FABlockEntityTypes.BUTCHER_BLOCK_CABINET.get(), ButcherBlockCabinetRenderer::new);
    }
}

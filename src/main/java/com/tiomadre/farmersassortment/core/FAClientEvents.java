package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.client.renderer.ButcherBlockCabinetRenderer;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FAClientEvents {
    private FAClientEvents() {
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FABlockEntityTypes.BUTCHER_BLOCK_CABINET.get(), ButcherBlockCabinetRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(Item.byBlock(FABlocks.TERRACOTTA_COOKING_POT.get()),
                new ResourceLocation(FarmersAssortment.MOD_ID, "color"),
                (stack, level, entity, seed) -> TerracottaCookingPotItem.getColor(stack).ordinal()));
    }
}

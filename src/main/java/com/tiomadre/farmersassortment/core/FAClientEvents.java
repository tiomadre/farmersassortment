package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.client.particle.AlabasterStoveFireParticle;
import com.tiomadre.farmersassortment.client.renderer.ButcherBlockCabinetRenderer;
import com.tiomadre.farmersassortment.client.renderer.FloatingDrawerRenderer;
import com.tiomadre.farmersassortment.client.renderer.RackRenderer;
import com.tiomadre.farmersassortment.client.screen.RackScreen;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import com.tiomadre.farmersassortment.core.item.TableItem;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAMenuTypes;
import com.tiomadre.farmersassortment.core.registry.FAParticleTypes;
import com.tiomadre.farmersassortment.core.registry.FAPumpkins;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import com.tiomadre.farmersassortment.client.screen.FloatingDrawerScreen;
import com.tiomadre.farmersassortment.client.renderer.FASnowGolemRenderer;
import net.minecraft.world.entity.EntityType;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FAClientEvents {
    private FAClientEvents() {
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FABlockEntityTypes.BUTCHER_BLOCK_CABINET.get(), ButcherBlockCabinetRenderer::new);
        event.registerBlockEntityRenderer(FABlockEntityTypes.RACK.get(), RackRenderer::new);
        event.registerBlockEntityRenderer(FABlockEntityTypes.FLOATING_COUNTER.get(), FloatingDrawerRenderer::new);
        event.registerEntityRenderer(EntityType.SNOW_GOLEM, FASnowGolemRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            FAPumpkins.VARIANTS.forEach(variant -> {
                ItemBlockRenderTypes.setRenderLayer(variant.stem.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(variant.attachedStem.get(), RenderType.cutout());
            });
            ItemProperties.register(Item.byBlock(FABlocks.TERRACOTTA_COOKING_POT.get()),
                    new ResourceLocation(FarmersAssortment.MOD_ID, "color"),
                    (stack, level, entity, seed) -> TerracottaCookingPotItem.getColor(stack).ordinal());

            FABlocks.allStools().forEach(stool -> ItemProperties.register(Item.byBlock(stool.get()),
                    new ResourceLocation(FarmersAssortment.MOD_ID, "rug"),
                    (stack, level, entity, seed) -> StoolItem.getRug(stack).ordinal()));

            FABlocks.allTables().forEach(table -> ItemProperties.register(Item.byBlock(table.get()),
                    new ResourceLocation(FarmersAssortment.MOD_ID, "rug"),
                    (stack, level, entity, seed) -> TableItem.getRug(stack).ordinal()));

            MenuScreens.register(FAMenuTypes.RACK.get(), RackScreen::new);
            MenuScreens.register(FAMenuTypes.FLOATING_COUNTER.get(), FloatingDrawerScreen::new);
        });
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FAParticleTypes.ALABASTER_STOVE_FIRE.get(), AlabasterStoveFireParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerPumpkinColors(RegisterColorHandlersEvent.Block event) {
        FAPumpkins.VARIANTS.forEach(variant -> {
            event.register((state, level, pos, tint) -> {
                int age = state.getValue(StemBlock.AGE);
                return (age * 32 << 16) | ((255 - age * 8) << 8) | (age * 4);
            }, variant.stem.get());
            event.register((state, level, pos, tint) -> 0xE0C71C, variant.attachedStem.get());
        });
    }

}

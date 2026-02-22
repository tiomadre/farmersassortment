package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.client.renderer.ButcherBlockCabinetRenderer;
import com.tiomadre.farmersassortment.client.renderer.SlabTableclothBakedModel;
import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
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
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        var models = event.getModels();

        var overlayModels = new java.util.EnumMap<StoolRugType, BakedModel>(StoolRugType.class);
        for (StoolRugType rugType : StoolRugType.values()) {
            if (!rugType.hasRug()) {
                continue;
            }
            ResourceLocation overlayLocation = new ResourceLocation(FarmersAssortment.MOD_ID, "slab_tablecloth_" + rugType.getSerializedName());
            BakedModel overlayModel = models.get(new ModelResourceLocation(overlayLocation, ""));
            if (overlayModel != null) {
                overlayModels.put(rugType, overlayModel);
            }
        }

        if (overlayModels.isEmpty()) {
            return;
        }

        java.util.List<ModelResourceLocation> slabTopKeys = new java.util.ArrayList<>();
        for (ResourceLocation key : models.keySet()) {
            if (!(key instanceof ModelResourceLocation modelKey)) {
                continue;
            }

            String variant = modelKey.getVariant();
            if (!variant.contains("type=top")) {
                continue;
            }

            ResourceLocation blockId = new ResourceLocation(modelKey.getNamespace(), modelKey.getPath());
            if (!(BuiltInRegistries.BLOCK.get(blockId) instanceof SlabBlock)) {
                continue;
            }

            slabTopKeys.add(modelKey);
        }

        for (ModelResourceLocation key : slabTopKeys) {
            BakedModel original = models.get(key);
            if (original != null) {
                models.put(key, new SlabTableclothBakedModel(original, overlayModels));
            }
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(Item.byBlock(FABlocks.TERRACOTTA_COOKING_POT.get()),
                    new ResourceLocation(FarmersAssortment.MOD_ID, "color"),
                    (stack, level, entity, seed) -> TerracottaCookingPotItem.getColor(stack).ordinal());

            FABlocks.stools().forEach(stool -> ItemProperties.register(Item.byBlock(stool.get()),
                    new ResourceLocation(FarmersAssortment.MOD_ID, "rug"),
                    (stack, level, entity, seed) -> StoolItem.getRug(stack).ordinal()));
        });
    }
}
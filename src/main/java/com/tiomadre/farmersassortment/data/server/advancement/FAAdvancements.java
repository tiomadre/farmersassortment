package com.tiomadre.farmersassortment.data.server.advancement;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class FAAdvancements extends AdvancementProvider {
    public FAAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new MainAdvancements()));
    }

    private static class MainAdvancements implements AdvancementSubProvider {
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer) {
            Advancement root = Advancement.Builder.advancement()
                    .display(FAItems.ALABASTER_KNIFE.get(),
                            title("root"),
                            description("root"),
                            new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/husbandry.png"),
                            FrameType.TASK,
                            false,
                            false,
                            false)
                    .addCriterion("has_knife", InventoryChangeTrigger.TriggerInstance.hasItems(FAItems.ALABASTER_KNIFE.get()))
                    .save(consumer, id("main/root"));

            Advancement cuttingBoard = Advancement.Builder.advancement()
                    .parent(root)
                    .display(FABlocks.SPRUCE_CUTTING_BOARD.get(),
                            title("cutting_board"),
                            description("cutting_board"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_cutting_board", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.SPRUCE_CUTTING_BOARD.get()))
                    .save(consumer, id("main/cutting_board"));

            Advancement cookingPot = Advancement.Builder.advancement()
                    .parent(cuttingBoard)
                    .display(FABlocks.COPPER_COOKING_POT.get(),
                            title("cooking_pot"),
                            description("cooking_pot"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_cooking_pot", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.COPPER_COOKING_POT.get()))
                    .save(consumer, id("main/cooking_pot"));

            Advancement furniture = Advancement.Builder.advancement()
                    .parent(cuttingBoard)
                    .display(FABlocks.OAK_TABLE.get(),
                            title("furniture"),
                            description("furniture"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_table", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.OAK_TABLE.get()))
                    .save(consumer, id("main/furniture"));

            Advancement.Builder.advancement()
                    .parent(cookingPot)
                    .display(FABlocks.ALABASTER_RACK.get(),
                            title("alchemist"),
                            description("alchemist"),
                            null,
                            FrameType.GOAL,
                            true,
                            true,
                            false)
                    .addCriterion("has_rack", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_RACK.get()))
                    .save(consumer, id("main/alchemist"));

            Advancement.Builder.advancement()
                    .parent(furniture)
                    .display(FABlocks.ALABASTER_STOVE.get(),
                            title("master_chef"),
                            description("master_chef"),
                            null,
                            FrameType.GOAL,
                            true,
                            true,
                            false)
                    .addCriterion("has_stove", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_STOVE.get()))
                    .save(consumer, id("main/master_chef"));
        }

        private static Component title(String path) {
            return Component.translatable("advancements.farmersassortment.main." + path + ".title");
        }

        private static Component description(String path) {
            return Component.translatable("advancements.farmersassortment.main." + path + ".description");
        }

        private static String id(String path) {
            return FarmersAssortment.MOD_ID + ":" + path;
        }
    }
}
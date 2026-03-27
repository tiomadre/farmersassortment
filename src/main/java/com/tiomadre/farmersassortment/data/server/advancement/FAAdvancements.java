package com.tiomadre.farmersassortment.data.server.advancement;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FAAdvanceTriggers;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.tiomadre.farmersassortment.data.server.tags.FATags.Items.COOKING_POTS;
import static com.tiomadre.farmersassortment.data.server.tags.FATags.Items.CUTTING_BOARDS;

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

            //Cutting Board Advancement
            Advancement cuttingBoard = Advancement.Builder.advancement()
                    .parent(root)
                    .display(FABlocks.BAMBOO_CUTTING_BOARD.get(),
                            title("cutting_board"),
                            description("cutting_board"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_cutting_board", hasItemInTag(CUTTING_BOARDS))
                    .save(consumer, id("main/cutting_board"));

            //Cooking Pot Advancement
            Advancement cookingPot = Advancement.Builder.advancement()
                    .parent(root)
                    .display(FABlocks.COPPER_COOKING_POT.get(),
                            title("cooking_pot"),
                            description("cooking_pot"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_cooking_pot", hasItemInTag(COOKING_POTS))
                    .save(consumer, id("main/cooking_pot"));
            //Cook Alabaster
            Advancement alabaster = Advancement.Builder.advancement()
                    .parent(cookingPot)
                    .display(FAItems.ALABASTER.get(),
                            title("transmutation"),
                            description("transmutation"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("has_alabaster", InventoryChangeTrigger.TriggerInstance.hasItems(FAItems.ALABASTER.get()))
                    .save(consumer, id("main/alabaster"));
            //Make Table
            Advancement furniture = Advancement.Builder.advancement()
                    .parent(root)
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

            Advancement alchemist = Advancement.Builder.advancement()
                    .parent(alabaster)
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


            Advancement flipTable = Advancement.Builder.advancement()
                    .parent(furniture)
                    .display(FABlocks.OAK_TABLE.get(),
                            title("flip_table"),
                            description("flip_table"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("flip_table", new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, FAAdvanceTriggers.FLIP_TABLE.toString());

            Advancement.Builder.advancement()
                    .parent(alabaster)
                    .display(FAItems.ALABASTER.get(),
                            title("master_chef"),
                            description("master_chef"),
                            null,
                            FrameType.CHALLENGE,
                            true,
                            true,
                            false)
                    .addCriterion("has_alabaster_block", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_BLOCK.get()))
                    .addCriterion("has_alabaster_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_BRICKS.get()))
                    .addCriterion("has_alabaster_slab", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_SLAB.get()))
                    .addCriterion("has_alabaster_stairs", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_STAIRS.get()))
                    .addCriterion("has_alabaster_wall", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_WALL.get()))
                    .addCriterion("has_alabaster_pillar", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_PILLAR.get()))
                    .addCriterion("has_alabaster_floating_counter", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_FLOATING_COUNTER.get()))
                    .addCriterion("has_alabaster_rack", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_RACK.get()))
                    .addCriterion("has_alabaster_table", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_TABLE.get()))
                    .addCriterion("has_alabaster_cooking_pot", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_COOKING_POT.get()))
                    .addCriterion("has_alabaster_stove", InventoryChangeTrigger.TriggerInstance.hasItems(FABlocks.ALABASTER_STOVE.get()))
                    .save(consumer, id("main/master_chef"));
        }
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

    private static InventoryChangeTrigger.TriggerInstance hasItemInTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

}

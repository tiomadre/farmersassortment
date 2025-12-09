package com.tiomadre.farmersassortment.data.server.recipes;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.CookingPotBlock;

import java.util.Objects;
import java.util.function.Consumer;

public final class FACrafting extends RecipeProvider {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersAssortment.MOD_ID);

    public FACrafting(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> output) {
        cuttingBoard(output, FABlocks.SPRUCE_CUTTING_BOARD, Blocks.SPRUCE_PLANKS);
        cuttingBoard(output, FABlocks.BIRCH_CUTTING_BOARD, Blocks.BIRCH_PLANKS);
        cuttingBoard(output, FABlocks.JUNGLE_CUTTING_BOARD, Blocks.JUNGLE_PLANKS);
        cuttingBoard(output, FABlocks.ACACIA_CUTTING_BOARD, Blocks.ACACIA_PLANKS);
        cuttingBoard(output, FABlocks.DARK_OAK_CUTTING_BOARD, Blocks.DARK_OAK_PLANKS);
        cuttingBoard(output, FABlocks.MANGROVE_CUTTING_BOARD, Blocks.MANGROVE_PLANKS);
        cuttingBoard(output, FABlocks.CHERRY_CUTTING_BOARD, Blocks.CHERRY_PLANKS);
        cuttingBoard(output, FABlocks.BAMBOO_CUTTING_BOARD, Blocks.BAMBOO_PLANKS);
        cuttingBoard(output, FABlocks.CRIMSON_CUTTING_BOARD, Blocks.CRIMSON_PLANKS);
        cuttingBoard(output, FABlocks.WARPED_CUTTING_BOARD, Blocks.WARPED_PLANKS);
        butcherBlockCabinet(output, FABlocks.OAK_BUTCHER_BLOCK_CABINET, blockItem("farmersdelight", "cutting_board"), blockItem("farmersdelight", "oak_cabinet"));
        butcherBlockCabinet(output, FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET, FABlocks.SPRUCE_CUTTING_BOARD.get(), blockItem("farmersdelight", "spruce_cabinet"));
        butcherBlockCabinet(output, FABlocks.BIRCH_BUTCHER_BLOCK_CABINET, FABlocks.BIRCH_CUTTING_BOARD.get(), blockItem("farmersdelight", "birch_cabinet"));
        butcherBlockCabinet(output, FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET, FABlocks.JUNGLE_CUTTING_BOARD.get(), blockItem("farmersdelight", "jungle_cabinet"));
        butcherBlockCabinet(output, FABlocks.ACACIA_BUTCHER_BLOCK_CABINET, FABlocks.ACACIA_CUTTING_BOARD.get(), blockItem("farmersdelight", "acacia_cabinet"));
        butcherBlockCabinet(output, FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET, FABlocks.DARK_OAK_CUTTING_BOARD.get(), blockItem("farmersdelight", "dark_oak_cabinet"));
        butcherBlockCabinet(output, FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET, FABlocks.MANGROVE_CUTTING_BOARD.get(), blockItem("farmersdelight", "mangrove_cabinet"));
        butcherBlockCabinet(output, FABlocks.CHERRY_BUTCHER_BLOCK_CABINET, FABlocks.CHERRY_CUTTING_BOARD.get(), blockItem("farmersdelight", "cherry_cabinet"));
        butcherBlockCabinet(output, FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET, FABlocks.BAMBOO_CUTTING_BOARD.get(), blockItem("farmersdelight", "bamboo_cabinet"));
        butcherBlockCabinet(output, FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET, FABlocks.CRIMSON_CUTTING_BOARD.get(), blockItem("farmersdelight", "crimson_cabinet"));
        butcherBlockCabinet(output, FABlocks.WARPED_BUTCHER_BLOCK_CABINET, FABlocks.WARPED_CUTTING_BOARD.get(), blockItem("farmersdelight", "warped_cabinet"));
        variantCookingPot(output, FABlocks.GOLDEN_COOKING_POT, Items.GOLD_INGOT, Items.WOODEN_SHOVEL, Items.BRICK);
        variantCookingPot(output, FABlocks.COPPER_COOKING_POT, Items.COPPER_INGOT, Items.WOODEN_SHOVEL, Items.BRICK);
        variantCookingPot(output, FABlocks.ALABASTER_COOKING_POT, Items.QUARTZ, Items.WOODEN_SHOVEL, Items.GOLD_INGOT);
        variantCookingPot(output, FABlocks.TERRACOTTA_COOKING_POT, Blocks.TERRACOTTA, Items.WOODEN_SHOVEL, Items.BRICK);
        variantStove(output, (RegistryObject<? extends ItemLike>) FABlocks.ALABASTER_STOVE, Blocks.QUARTZ_BLOCK, Items.GOLD_INGOT, Items.FLINT_AND_STEEL);
        knife(output, FAItems.AMETHYST_KNIFE, Items.AMETHYST_SHARD);
        knife(output, FAItems.QUARTZ_KNIFE, Items.QUARTZ);
    }

    private void cuttingBoard(Consumer<FinishedRecipe> output, RegistryObject<? extends ItemLike> board, ItemLike planks) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, board.get())
                .define('#', planks)
                .define('S', Items.STICK)
                .pattern("S##")
                .pattern("S##")
                .unlockedBy(getHasName(planks), has(planks))
                .save(output);
    }

    private void variantCookingPot(Consumer<FinishedRecipe> output, RegistryObject<CookingPotBlock> variantCookingPot, ItemLike material, ItemLike shovel, ItemLike base) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, variantCookingPot.get())
                .define('M', material)
                .define('S', shovel)
                .define('B', base)
                .define('W', Items.WATER_BUCKET)
                .pattern("BSB")
                .pattern("MWM")
                .pattern("MMM")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    private void knife(Consumer<FinishedRecipe> output, RegistryObject<? extends Item> knife, ItemLike material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, knife.get())
                .define('M', material)
                .define('S', Items.STICK)
                .pattern(" M")
                .pattern(" S")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }
    private void butcherBlockCabinet(Consumer<FinishedRecipe> output, RegistryObject<? extends ItemLike> cabinet,
                                     ItemLike cuttingBoard, ItemLike baseCabinet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cabinet.get())
                .define('#', cuttingBoard)
                .define('C', baseCabinet)
                .pattern("#")
                .pattern("C")
                .unlockedBy(getHasName(cuttingBoard), has(cuttingBoard))
                .save(output);
    }

    private void variantStove(Consumer<FinishedRecipe> output, RegistryObject<? extends ItemLike> stove, ItemLike material,
                              ItemLike ingot, ItemLike fuel) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, stove.get())
                .define('M', material)
                .define('I', ingot)
                .define('F', fuel)
                .pattern("III")
                .pattern("M M")
                .pattern("MFM")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    private ItemLike blockItem(String namespace, String path) {
        ResourceLocation id = new ResourceLocation(namespace, path);
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(id), () -> "Missing block " + id);
    }


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
package com.tiomadre.farmersassortment.data.server.recipes;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
        goldenCookingPot(output);
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

    private void copperCookingPot(Consumer<FinishedRecipe> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FABlocks.COPPER_COOKING_POT.get())
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BRICK)
                .define('W', Items.WATER_BUCKET)
                .pattern("C C")
                .pattern("CWC")
                .pattern("BBB")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);
    }

    private void goldenCookingPot(Consumer<FinishedRecipe> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FABlocks.GOLDEN_COOKING_POT.get())
                .define('G', Items.GOLD_INGOT)
                .define('B', Items.BRICK)
                .define('W', Items.WATER_BUCKET)
                .pattern("G G")
                .pattern("GWG")
                .pattern("BBB")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
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

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
package com.tiomadre.farmersassortment.data.server.recipes;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FACrafting extends RecipeProvider {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersAssortment.MOD_ID);

    public FACrafting(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
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
    }

    private void cuttingBoard(RecipeOutput output, RegistryObject<? extends ItemLike> board, ItemLike planks) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, board.get())
                .define('#', planks)
                .define('S', Items.STICK)
                .pattern("S##")
                .pattern("S##")
                .unlockedBy(getHasName(planks), has(planks))
                .showNotification()
                .save(output);
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}

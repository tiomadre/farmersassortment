package com.tiomadre.farmersassortment.data.server.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class FACooking implements DataProvider {
    private final PackOutput.PathProvider recipes;

    public FACooking(PackOutput output) {
        this.recipes = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return saveCookingRecipe(cachedOutput, new ResourceLocation(FarmersAssortment.MOD_ID, "alabaster"),
                FAItems.ALABASTER.get(), 2,
                ingredient(Items.QUARTZ), ingredient(Items.QUARTZ), ingredient(Items.GOLD_INGOT));
    }

    private CompletableFuture<?> saveCookingRecipe(CachedOutput cachedOutput, ResourceLocation id, ItemLike result, int count,
                                                   JsonObject... ingredients) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "farmersdelight:cooking");
        recipe.addProperty("recipe_book_tab", "misc");

        JsonArray ingredientList = new JsonArray();
        for (JsonObject ingredient : ingredients) {
            ingredientList.add(ingredient);
        }
        recipe.add("ingredients", ingredientList);

        JsonObject recipeResult = new JsonObject();
        recipeResult.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.asItem())).toString());
        if (count > 1) {
            recipeResult.addProperty("count", count);
        }
        recipe.add("result", recipeResult);
        recipe.addProperty("cookingtime", 200);

        Path path = this.recipes.json(id);
        return DataProvider.saveStable(cachedOutput, recipe, path);
    }

    private JsonObject ingredient(ItemLike item) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).toString());
        return ingredient;
    }

    @Override
    public String getName() {
        return "Farmer's Assortment Cooking Recipes";
    }
}
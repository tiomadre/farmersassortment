package com.tiomadre.farmersassortment.core.recipe;

import com.google.gson.JsonObject;
import com.tiomadre.farmersassortment.core.registry.FAGourds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import javax.annotation.Nullable;
import java.util.List;

public final class GourdCuttingRecipe extends CuttingBoardRecipe {
    private GourdCuttingRecipe(CuttingBoardRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getIngredients().get(0), recipe.getTool(),
                recipe.getRollableResults(), recipe.getSoundEventID());
    }

    @Override
    public List<ItemStack> rollResults(RandomSource random, int fortune, RecipeWrapper inventory) {
        return FAGourds.chopped(FAGourds.getVariant(inventory.getItem(0)));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FAGourds.CUTTING_SERIALIZER.get();
    }

    public static final class Serializer extends CuttingBoardRecipe.Serializer {
        @Override
        public CuttingBoardRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new GourdCuttingRecipe(super.fromJson(id, json));
        }

        @Nullable
        @Override
        public CuttingBoardRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            CuttingBoardRecipe recipe = super.fromNetwork(id, buffer);
            return recipe == null ? null : new GourdCuttingRecipe(recipe);
        }
    }
}

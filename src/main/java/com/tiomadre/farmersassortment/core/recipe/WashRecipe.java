package com.tiomadre.farmersassortment.core.recipe;

import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;

public class WashRecipe extends CustomRecipe {
    private final Supplier<Item> item;
    private final Supplier<? extends RecipeSerializer<?>> serializer;

    public WashRecipe(ResourceLocation id, CraftingBookCategory category,
                      Supplier<Item> item,
                      Supplier<? extends RecipeSerializer<?>> serializer) {
        super(id, category);
        this.item = item;
        this.serializer = serializer;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean foundItem = false;
        boolean water = false;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(item.get()) && DyeColors.getColor(stack) >= 0 && !foundItem) foundItem = true;
            else if (stack.is(Items.WATER_BUCKET) && !water) water = true;
            else return false;
        }
        return foundItem && water;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
        if (!matches(container, null)) return ItemStack.EMPTY;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.is(item.get())) {
                ItemStack result = stack.copyWithCount(1);
                DyeColors.setColor(result, -1);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (container.getItem(slot).is(Items.WATER_BUCKET)) {
                remaining.set(slot, container.getItem(slot).copyWithCount(1));
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer.get();
    }
}

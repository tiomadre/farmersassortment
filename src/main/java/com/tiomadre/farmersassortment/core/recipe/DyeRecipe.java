package com.tiomadre.farmersassortment.core.recipe;

import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class DyeRecipe extends CustomRecipe {
    private final Supplier<Item> item;
    private final boolean cross;
    private final Supplier<? extends RecipeSerializer<?>> serializer;

    public DyeRecipe(ResourceLocation id, CraftingBookCategory category,
                     Supplier<Item> item, boolean cross,
                     Supplier<? extends RecipeSerializer<?>> serializer) {
        super(id, category);
        this.item = item;
        this.cross = cross;
        this.serializer = serializer;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (!canCraftInDimensions(container.getWidth(), container.getHeight())) return false;
        if (!(container.getItem(4).getItem() instanceof DyeItem)) return false;
        Item target = item.get();
        for (int slot = 0; slot < 9; slot++) {
            if (slot == 4) continue;
            ItemStack stack = container.getItem(slot);
            boolean corner = slot == 0 || slot == 2 || slot == 6 || slot == 8;
            if (cross && corner) {
                if (!stack.isEmpty()) return false;
            } else if (!stack.is(target)) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
        if (!matches(container, null)) return ItemStack.EMPTY;
        DyeItem dye = (DyeItem) container.getItem(4).getItem();
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = container.getItem(slot);
            if (slot != 4 && stack.is(item.get())) ingredients.add(stack);
        }
        ItemStack result = new ItemStack(item.get(), cross ? 4 : 8);
        DyeColors.setColor(result, DyeColors.blend(ingredients, dye));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer.get();
    }
}

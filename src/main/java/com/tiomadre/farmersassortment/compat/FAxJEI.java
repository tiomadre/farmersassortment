package com.tiomadre.farmersassortment.compat;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.Items;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraftforge.fml.ModList;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

@JeiPlugin
public class FAxJEI implements IModPlugin {
    private static final RecipeType<CuttingBoardRecipe> CUTTING = FDRecipeTypes.CUTTING;
    private static final RecipeType<CookingPotRecipe> COOKING = FDRecipeTypes.COOKING;
    private static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = RecipeTypes.CAMPFIRE_COOKING;
    private static final RecipeType<Object> CRAB_TRAP = RecipeType.create("crabbersdelight", "crab_trap", Object.class);
    private static final RecipeType<Object> CRAB_TRAPPING = RecipeType.create("crabbersdelight", "crab_trapping", Object.class);
    private static final ResourceLocation CD_CRAB_TRAP_ID = new ResourceLocation("crabbersdelight", "crab_trap");
    private static final ResourceLocation ID = new ResourceLocation(FarmersAssortment.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        List<CraftingRecipe> recipes = new ArrayList<>();
        for (boolean gate : new boolean[]{false, true}) {
            String name = gate ? "rope_fence_gate_dye" : "rope_fence_dye";
            if (level.getRecipeManager().byKey(new ResourceLocation(FarmersAssortment.MOD_ID, name)).isEmpty()) continue;
            var block = gate ? ModBlocks.ROPE_FENCE_GATE.get() : ModBlocks.ROPE_FENCE.get();
            for (DyeColor color : DyeColor.values()) {
                DyeItem dye = DyeItem.byColor(color);
                NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
                for (int slot = 0; slot < 9; slot++) {
                    boolean corner = slot == 0 || slot == 2 || slot == 6 || slot == 8;
                    if (slot == 4) ingredients.set(slot, Ingredient.of(dye));
                    else if (!gate || !corner) ingredients.set(slot, Ingredient.of(block));
                }
                ItemStack result = new ItemStack(block, gate ? 4 : 8);
                DyeColors.setColor(result, DyeColors.blend(List.of(), dye));
                recipes.add(new ShapedRecipe(new ResourceLocation(FarmersAssortment.MOD_ID,
                        "jei/" + name + "/" + color.getName()), "", CraftingBookCategory.MISC,
                        3, 3, ingredients, result));
            }
        }
        for (boolean gate : new boolean[]{false, true}) {
            String name = gate ? "rope_fence_gate_wash" : "rope_fence_wash";
            if (level.getRecipeManager().byKey(new ResourceLocation(FarmersAssortment.MOD_ID, name)).isEmpty()) continue;
            var block = gate ? ModBlocks.ROPE_FENCE_GATE.get() : ModBlocks.ROPE_FENCE.get();
            for (DyeColor color : DyeColor.values()) {
                ItemStack dyed = new ItemStack(block);
                DyeColors.setColor(dyed, DyeColors.blend(List.of(), DyeItem.byColor(color)));
                NonNullList<Ingredient> ingredients = NonNullList.of(Ingredient.EMPTY,
                        Ingredient.of(dyed), Ingredient.of(Items.WATER_BUCKET));
                recipes.add(new ShapelessRecipe(new ResourceLocation(FarmersAssortment.MOD_ID,
                        "jei/" + name + "/" + color.getName()), "", CraftingBookCategory.MISC,
                        new ItemStack(block), ingredients));
            }
        }
        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CUTTING_BOARD.get()), CUTTING);
        FABlocks.allCuttingBoards().forEach(board -> registration.addRecipeCatalyst(new ItemStack(board.get()), CUTTING));
        FABlocks.allButcherBlockCabinets().forEach(cabinet -> registration.addRecipeCatalyst(new ItemStack(cabinet.get()), CUTTING));

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_POT.get()), COOKING);
        FABlocks.allCookingPots().forEach(pot -> registration.addRecipeCatalyst(new ItemStack(pot.get()), COOKING));

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SKILLET.get()), CAMPFIRE_COOKING);

        if (!ModList.get().isLoaded("crabbersdelight") || !FarmersAssortment.isCrabbersCompatEnabled()) {
            return;
        }

        FAxCrabbersBlocks.cuttingBoards().forEach(board -> registration.addRecipeCatalyst(new ItemStack(board.get()), CUTTING));
        FAxCrabbersBlocks.butcherBlockCabinets().forEach(cabinet -> registration.addRecipeCatalyst(new ItemStack(cabinet.get()), CUTTING));
        FAxCrabbersBlocks.cookingPots().forEach(pot -> registration.addRecipeCatalyst(new ItemStack(pot.get()), COOKING));
        FAxCrabbersBlocks.skillets().forEach(skillet -> registration.addRecipeCatalyst(new ItemStack(skillet.get()), CAMPFIRE_COOKING));

        ItemStack crabTrapStack = getCrabTrapStack();
        if (!crabTrapStack.isEmpty()) {
            registration.addRecipeCatalyst(crabTrapStack, CRAB_TRAP, CRAB_TRAPPING);
        }
        FAxCrabbersBlocks.crabTraps().forEach(trap -> registration.addRecipeCatalyst(new ItemStack(trap.get()), CRAB_TRAP, CRAB_TRAPPING));
    }

    private static ItemStack getCrabTrapStack() {
        if (!net.minecraftforge.registries.ForgeRegistries.ITEMS.containsKey(CD_CRAB_TRAP_ID)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(CD_CRAB_TRAP_ID));
    }
}

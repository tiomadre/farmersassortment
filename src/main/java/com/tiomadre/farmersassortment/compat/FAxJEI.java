package com.tiomadre.farmersassortment.compat;

import alabaster.crabbersdelight.common.registry.CDModBlocks;
import alabaster.crabbersdelight.integration.jei.CrabTrapCategory;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

@JeiPlugin
public class FAxJEI implements IModPlugin {
    private static final RecipeType<CuttingBoardRecipe> CUTTING = FDRecipeTypes.CUTTING;
    private static final RecipeType<CookingPotRecipe> COOKING = FDRecipeTypes.COOKING;
    private static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = RecipeTypes.CAMPFIRE_COOKING;
    private static final RecipeType<CrabTrapCategory> CRAB_TRAP = RecipeType.create("crabbersdelight", "crab_trap", CrabTrapCategory.class);
    private static final RecipeType<CrabTrapCategory> CRAB_TRAPPING = RecipeType.create("crabbersdelight", "crab_trapping", CrabTrapCategory.class);
    private static final ResourceLocation ID = new ResourceLocation(FarmersAssortment.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CUTTING_BOARD.get()), CUTTING);
        FABlocks.allCuttingBoards().forEach(board -> registration.addRecipeCatalyst(new ItemStack(board.get()), CUTTING));
        FABlocks.allButcherBlockCabinets().forEach(cabinet -> registration.addRecipeCatalyst(new ItemStack(cabinet.get()), CUTTING));
        FAxCrabbersBlocks.cuttingBoards().forEach(board -> registration.addRecipeCatalyst(new ItemStack(board.get()), CUTTING));
        FAxCrabbersBlocks.butcherBlockCabinets().forEach(cabinet -> registration.addRecipeCatalyst(new ItemStack(cabinet.get()), CUTTING));

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_POT.get()), COOKING);
        FABlocks.allCookingPots().forEach(pot -> registration.addRecipeCatalyst(new ItemStack(pot.get()), COOKING));
        FAxCrabbersBlocks.cookingPots().forEach(pot -> registration.addRecipeCatalyst(new ItemStack(pot.get()), COOKING));

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SKILLET.get()), CAMPFIRE_COOKING);
        FAxCrabbersBlocks.skillets().forEach(skillet -> registration.addRecipeCatalyst(new ItemStack(skillet.get()), CAMPFIRE_COOKING));

        registration.addRecipeCatalyst(new ItemStack(CDModBlocks.CRAB_TRAP.get()), CRAB_TRAP, CRAB_TRAPPING);
        FAxCrabbersBlocks.crabTraps().forEach(trap -> registration.addRecipeCatalyst(new ItemStack(trap.get()), CRAB_TRAP, CRAB_TRAPPING));
    }
}

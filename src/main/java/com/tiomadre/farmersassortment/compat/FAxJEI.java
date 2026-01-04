package com.tiomadre.farmersassortment.compat;

import alabaster.crabbersdelight.common.registry.CDModBlocks;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@JeiPlugin
public class FAxJEI implements IModPlugin {
    private static final RecipeType<Object> CUTTING = RecipeType.create("farmersdelight", "cutting", Object.class);
    private static final RecipeType<Object> CRAB_TRAP = RecipeType.create("crabbersdelight", "crab_trap", Object.class);
    private static final RecipeType<Object> CRAB_TRAPPING = RecipeType.create("crabbersdelight", "crab_trapping", Object.class);
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

        registration.addRecipeCatalyst(new ItemStack(CDModBlocks.CRAB_TRAP.get()), CRAB_TRAP, CRAB_TRAPPING);
        FAxCrabbersBlocks.crabTraps().forEach(trap -> registration.addRecipeCatalyst(new ItemStack(trap.get()), CRAB_TRAP, CRAB_TRAPPING));
    }
}
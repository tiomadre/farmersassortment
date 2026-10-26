package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.config.FAConfig;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class CookingPotCookingAccelerator {
    @Shadow
    private int cookTime;

    @Unique
    private double farmersassortment$bonusProgress;

    @Inject(method = "processCooking", at = @At("HEAD"))
    private void farmersassortment$applyCookingBoost(
            CookingPotRecipe recipe,
            CookingPotBlockEntity cookingPot,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Level level = cookingPot.getLevel();

        if (level == null || level.isClientSide) {
            farmersassortment$bonusProgress = 0.0D;
            return;
        }

        double boost = FAConfig.ALABASTER_STOVE_COOKING_BOOST.get();

        if (boost <= 0.0D
                || (FAConfig.ALABASTER_STOVE_ALABASTER_POTS_ONLY.get()
                && !cookingPot.getBlockState().is(FABlocks.ALABASTER_COOKING_POT.get()))) {
            farmersassortment$bonusProgress = 0.0D;
            return;
        }

        BlockState stove = level.getBlockState(cookingPot.getBlockPos().below());

        if (!stove.is(FABlocks.ALABASTER_STOVE.get())
                || !stove.hasProperty(StoveBlock.LIT)
                || !stove.getValue(StoveBlock.LIT)) {
            farmersassortment$bonusProgress = 0.0D;
            return;
        }

        if (cookTime == 0) {
            farmersassortment$bonusProgress = 0.0D;
        }

        farmersassortment$bonusProgress += boost;

        int extraProgress = (int) farmersassortment$bonusProgress;
        farmersassortment$bonusProgress -= extraProgress;

        int remainingProgress = Math.max(0, recipe.getCookTime() - cookTime - 1);
        cookTime += Math.min(extraProgress, remainingProgress);
    }
}
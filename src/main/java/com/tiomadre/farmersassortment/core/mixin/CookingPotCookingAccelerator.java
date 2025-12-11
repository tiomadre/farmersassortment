package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.block.UniqueStoveBlock;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class CookingPotCookingAccelerator {

    @Unique
    private int farmersassortment$alabasterBonusCounter;

    @Shadow
    private int cookTime;

    @Shadow
    private int cookTimeTotal;

    @Shadow
    public abstract boolean isHeated();

    @Inject(method = "cookingTick", at = @At("TAIL"))
    private static void farmersassortment$accelerateAboveAlabasterStove(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot, CallbackInfo ci) {
        CookingPotCookingAccelerator self = (CookingPotCookingAccelerator) (Object) cookingPot;

        if (level.isClientSide || !self.isHeated() || !state.is(FABlocks.ALABASTER_COOKING_POT.get())) {
            self.farmersassortment$alabasterBonusCounter = 0;
            return;
        }

        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is(FABlocks.ALABASTER_STOVE.get()) && belowState.hasProperty(UniqueStoveBlock.LIT) && belowState.getValue(UniqueStoveBlock.LIT)) {
            self.farmersassortment$alabasterBonusCounter++;
            if (self.farmersassortment$alabasterBonusCounter >= 5) {
                self.cookTime = Math.min(self.cookTime + 1, self.cookTimeTotal);
                self.farmersassortment$alabasterBonusCounter = 0;
            }
        } else {
            self.farmersassortment$alabasterBonusCounter = 0;
        }
    }
}
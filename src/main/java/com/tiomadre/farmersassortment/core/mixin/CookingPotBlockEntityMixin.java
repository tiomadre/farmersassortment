package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin {

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void farmersassortment$overrideCopperCookingPotName(CallbackInfoReturnable<Component> cir) {
        CookingPotBlockEntity self = (CookingPotBlockEntity) (Object) this;

        if (self.getCustomName() != null) {
            return;
        }

        BlockState state = self.getBlockState();
        if (state.is(FABlocks.COPPER_COOKING_POT.get())) {
            cir.setReturnValue(Component.translatable("container.farmersassortment.copper_cooking_pot"));
        } else if (state.is(FABlocks.GOLDEN_COOKING_POT.get())) {
            cir.setReturnValue(Component.translatable("container.farmersassortment.golden_cooking_pot"));
        } else if (state.is(FABlocks.TERRACOTTA_COOKING_POT.get())) {
            cir.setReturnValue(Component.translatable("container.farmersassortment.terracotta_cooking_pot"));
        }
    }
}
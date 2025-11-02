package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

@Mixin(CookingPotMenu.class)
public abstract class VariantPotMenu {

    @Shadow(remap = false)
    @Final
    private ContainerLevelAccess access;

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    private void farmersassortment$keepCopperCookingPotOpen(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        boolean isCopperCookingPot = this.access.evaluate((level, pos) ->
                level.getBlockState(pos).is(FABlocks.COPPER_COOKING_POT.get()), false);

        if (isCopperCookingPot) {
            cir.setReturnValue(true);
        }
    }
}
package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.tiomadre.foragersinsight.common.gui.DiffuserMenu")
public abstract class DiffuserMenu {

    @Shadow(remap = false)
    @Final
    private ContainerLevelAccess access;

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    private void farmersassortment$keepAmethystDiffuserOpen(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        boolean isAmethystDiffuser = this.access.evaluate((level, pos) -> level.getBlockState(pos).is(FAxForagersBlocks.AMETHYST_DIFFUSER.get()), false);
        if (isAmethystDiffuser) {
            cir.setReturnValue(true);
        }
    }
}
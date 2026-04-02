package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class DiffuserMenuMixin {

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    private static void farmersassortment$allowVariantDiffusers(ContainerLevelAccess access, Player player, Block validBlock,
                                                                CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(validBlock);
        if (blockId == null || !"foragersinsight".equals(blockId.getNamespace()) || !"diffuser".equals(blockId.getPath())) {
            return;
        }

        boolean isVariantDiffuser = access.evaluate((level, pos) -> {
            var state = level.getBlockState(pos);
            return FAxForagersBlocks.diffusers().anyMatch(diffuser -> state.is(diffuser.get()));
        }, false);
        if (isVariantDiffuser) {
            cir.setReturnValue(true);
        }
    }
}
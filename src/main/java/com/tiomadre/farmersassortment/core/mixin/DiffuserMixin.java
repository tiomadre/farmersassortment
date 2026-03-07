package com.tiomadre.farmersassortment.core.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.tiomadre.foragersinsight.common.block.entity.DiffuserBlockEntity")
public abstract class DiffuserMixin {

    @Inject(method = "getDiffuserStack", at = @At("RETURN"), cancellable = true)
    private void farmersassortment$dropVariantDiffuser(CallbackInfoReturnable<ItemStack> cir) {
        BlockEntity blockEntity = (BlockEntity) (Object) this;
        ItemStack originalStack = cir.getReturnValue();
        ItemStack variantStack = new ItemStack(blockEntity.getBlockState().getBlock().asItem(), originalStack.getCount());

        if (variantStack.isEmpty()) {
            return;
        }

        if (originalStack.hasTag()) {
            variantStack.setTag(originalStack.getTag().copy());
        }

        cir.setReturnValue(variantStack);
    }
}
package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.block.state.SlabRugState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlabBlock.class)
public abstract class SlabBlockMixin {

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void farmersassortment$addRugState(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SlabRugState.RUG);
    }
}
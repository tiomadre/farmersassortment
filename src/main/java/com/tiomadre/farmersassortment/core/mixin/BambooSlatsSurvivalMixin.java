package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BambooSaplingBlock.class, BambooStalkBlock.class})
public abstract class BambooSlatsSurvivalMixin {
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void farmersassortment$checkEmbeddedSoil(BlockState state, LevelReader level, BlockPos pos,
                                                    CallbackInfoReturnable<Boolean> callback) {
        BlockPos below = pos.below();
        if (SlatsBlock.embedded(level.getBlockState(below))) {
            callback.setReturnValue(SlatsBlock.soilAt(level, below).is(BlockTags.BAMBOO_PLANTABLE_ON));
        }
    }
}

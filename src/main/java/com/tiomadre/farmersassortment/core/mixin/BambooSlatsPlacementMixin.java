package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BambooStalkBlock.class)
public abstract class BambooSlatsPlacementMixin {
    @Redirect(method = "getStateForPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState farmersassortment$readEmbeddedSoil(Level level, BlockPos pos) {
        return SlatsBlock.unwrap(level, pos);
    }
}

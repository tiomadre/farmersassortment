package com.tiomadre.farmersassortment.data.server.loot;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FALoot extends LootTableProvider {
    public FALoot(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, Set.of(), List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)));
    }

    private static class Blocks extends BlockLootSubProvider {
        protected Blocks() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            FABlocks.allCuttingBoards().forEach(cuttingBoard -> this.dropSelf(cuttingBoard.get()));
            FABlocks.allButcherBlockCabinets().forEach(cabinet -> this.add(cabinet.get(), this.createSingleItemTable(cabinet.get())
                    .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))));
            FABlocks.allCookingPots().forEach(cookingPot -> this.dropSelf(cookingPot.get()));
            this.dropSelf(FAxCrabbersBlocks.PEARLESCENT_SKILLET.get());
            this.dropSelf(FABlocks.ALABASTER_STOVE.get());
            FAxForagersBlocks.diffusers().forEach(diffuser -> this.dropSelf(diffuser.get()));

            FAxCrabbersBlocks.crabTraps().forEach(trap -> this.add(trap.get(), this.createSingleItemTable(trap.get())
                    .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))));
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream()
                    .filter(block -> Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace().equals(FarmersAssortment.MOD_ID))
                    .collect(Collectors.toSet());
        }
    }
}
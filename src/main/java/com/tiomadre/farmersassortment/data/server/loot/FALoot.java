package com.tiomadre.farmersassortment.data.server.loot;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FARugs;
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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            Set<Block> generated = new HashSet<>();

            registerDropSelf(FABlocks.allCuttingBoards(), generated);
            registerCopyName(FABlocks.allButcherBlockCabinets(), generated);
            registerDropSelf(FABlocks.allCookingPots(), generated);
            registerDropSelf(FABlocks.allStools(), generated);
            registerDropSelf(Stream.of(FAxCrabbersBlocks.PEARLESCENT_SKILLET), generated);
            registerDropSelf(FABlocks.floatingCounters(), generated);
            registerDropSelf(FARugs.canvasRugs(), generated);
            registerDropSelf(Stream.of(FABlocks.ALABASTER_STOVE), generated);
            registerDropSelf(FAxForagersBlocks.diffusers(), generated);
            registerCopyName(FAxCrabbersBlocks.crabTraps(), generated);

            getKnownBlocks().forEach(block -> {
                if (generated.add(block)) {
                    this.dropSelf(block);
                }
            });
        }

        private <T extends Block> void registerDropSelf(Stream<? extends RegistryObject<T>> blocks, Set<Block> generated) {
            blocks.map(RegistryObject::get).forEach(block -> {
                generated.add(block);
                this.dropSelf(block);
            });
        }

        private <T extends Block> void registerCopyName(Stream<? extends RegistryObject<T>> blocks, Set<Block> generated) {
            blocks.map(RegistryObject::get).forEach(block -> {
                generated.add(block);
                this.add(block, this.createSingleItemTable(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
            });

        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream()
                    .filter(block -> Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace().equals(FarmersAssortment.MOD_ID))
                    .collect(Collectors.toSet());
        }
    }
}

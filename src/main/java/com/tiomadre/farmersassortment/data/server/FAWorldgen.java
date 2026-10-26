package com.tiomadre.farmersassortment.data.server;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.worldgen.FABiomeModifiers;
import com.tiomadre.farmersassortment.core.worldgen.FAConfiguredFeatures;
import com.tiomadre.farmersassortment.core.worldgen.FAPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class FAWorldgen extends DatapackBuiltinEntriesProvider {
    public FAWorldgen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider,
                new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, FAConfiguredFeatures::bootstrap)
                        .add(Registries.PLACED_FEATURE, FAPlacedFeatures::bootstrap)
                        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, FABiomeModifiers::bootstrap),
                Set.of(FarmersAssortment.MOD_ID, "minecraft"));
    }
}
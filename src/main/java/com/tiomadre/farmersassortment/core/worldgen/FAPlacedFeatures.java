package com.tiomadre.farmersassortment.core.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public final class FAPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN = ResourceKey.create(
            Registries.PLACED_FEATURE, new ResourceLocation("minecraft", "patch_pumpkin"));

    public static final ResourceKey<PlacedFeature> PATCH_GOURD = ResourceKey.create(
            Registries.PLACED_FEATURE, new ResourceLocation("farmersassortment", "patch_gourd"));

    private FAPlacedFeatures() {
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, PATCH_PUMPKIN,
                configuredFeatures.getOrThrow(FAConfiguredFeatures.PATCH_PUMPKIN),
                RarityFilter.onAverageOnceEvery(300),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());

        PlacementUtils.register(context, PATCH_GOURD,
                configuredFeatures.getOrThrow(FAConfiguredFeatures.PATCH_GOURD),
                RarityFilter.onAverageOnceEvery(600),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());
    }

}

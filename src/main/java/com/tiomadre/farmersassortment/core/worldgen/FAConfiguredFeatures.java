package com.tiomadre.farmersassortment.core.worldgen;

import com.tiomadre.farmersassortment.core.registry.FAPumpkins;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import com.tiomadre.farmersassortment.core.registry.FAFeatures;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class FAConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PUMPKIN = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, new ResourceLocation("minecraft", "patch_pumpkin"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GOURD = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, new ResourceLocation("farmersassortment", "patch_gourd"));


    private FAConfiguredFeatures() {
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        WeightedStateProvider pumpkins = new WeightedStateProvider(
                SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.PUMPKIN.defaultBlockState(), 7)
                        .add(FAPumpkins.RED.pumpkin.get().defaultBlockState(), 1)
                        .add(FAPumpkins.WHITE.pumpkin.get().defaultBlockState(), 1)
                        .add(FAPumpkins.YELLOW.pumpkin.get().defaultBlockState(), 1));

        FeatureUtils.register(context, PATCH_PUMPKIN, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96, 7, 3,
                        PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(pumpkins),
                                BlockPredicate.allOf(
                                        BlockPredicate.matchesBlocks(Blocks.AIR),
                                        BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK)))));

        FeatureUtils.register(context, PATCH_GOURD, FAFeatures.GOURD_PATCH.get(),
                NoneFeatureConfiguration.INSTANCE);
    }
}

package com.tiomadre.farmersassortment.core.worldgen;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public final class FABiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_GOURD_PATCHES = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS,
            new ResourceLocation(FarmersAssortment.MOD_ID, "add_gourd_patches"));

    private FABiomeModifiers() {
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(ADD_GOURD_PATCHES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(FAPlacedFeatures.PATCH_GOURD)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }
}
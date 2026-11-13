package com.tiomadre.farmersassortment.core.worldgen;

import com.mojang.serialization.Codec;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FABiomeModifiers {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FarmersAssortment.MOD_ID);

    private static final RegistryObject<Codec<GourdPatches>> GOURD_PATCHES =
            SERIALIZERS.register("gourd_patches", () -> PlacedFeature.CODEC.fieldOf("feature")
                    .xmap(GourdPatches::new, GourdPatches::feature).codec());

    public static final ResourceKey<BiomeModifier> ADD_GOURD_PATCHES = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS,
            new ResourceLocation(FarmersAssortment.MOD_ID, "add_gourd_patches"));

    private FABiomeModifiers() {
    }

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        context.register(ADD_GOURD_PATCHES, new GourdPatches(placedFeatures.getOrThrow(FAPlacedFeatures.PATCH_GOURD)));
    }

    private record GourdPatches(Holder<PlacedFeature> feature) implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.AFTER_EVERYTHING) {
                var features = builder.getGenerationSettings().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
                if (features.stream().anyMatch(placedFeature -> placedFeature.is(FAPlacedFeatures.PATCH_PUMPKIN))
                        && !features.contains(feature)) {
                    features.add(feature);
                }
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return GOURD_PATCHES.get();
        }
    }
}

package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.worldgen.GourdPatchFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FAFeatures {
    private static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, FarmersAssortment.MOD_ID);

    public static final RegistryObject<GourdPatchFeature> GOURD_PATCH =
            FEATURES.register("gourd_patch", GourdPatchFeature::new);

    private FAFeatures() {
    }

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }
}
package com.tiomadre.farmersassortment.data.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FAParticleTypes;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public final class FAParticleDef implements DataProvider {
    private final PackOutput.PathProvider particles;

    public FAParticleDef(PackOutput output) {
        this.particles = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return DataProvider.saveStable(cachedOutput, singleTexture("farmersassortment:alabaster_fire"), this.particles.json(FAParticleTypes.ALABASTER_STOVE_FIRE.getId()));
    }

    private JsonObject singleTexture(String texture) {
        JsonObject particle = new JsonObject();
        JsonArray textures = new JsonArray();
        textures.add(texture);
        particle.add("textures", textures);
        return particle;
    }

    @Override
    public String getName() {
        return FarmersAssortment.MOD_ID + " Particle Definitions";
    }
}
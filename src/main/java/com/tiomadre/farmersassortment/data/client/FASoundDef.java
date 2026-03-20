package com.tiomadre.farmersassortment.data.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FASoundEvents;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;

public final class FASoundDef implements DataProvider {
    private final PackOutput.PathProvider sounds;

    public FASoundDef(PackOutput output) {
        this.sounds = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        JsonObject sounds = new JsonObject();
        addAlabasterSound(sounds, FASoundEvents.ALABASTER_BREAK, "minecraft:block.calcite.break", 0.5F, 0.94F, "minecraft:block.amethyst_block.break", 0.32F, 1.12F);
        addAlabasterSound(sounds, FASoundEvents.ALABASTER_STEP, "minecraft:block.calcite.step", 0.5F, 0.94F, "minecraft:block.amethyst_block.step", 0.26F, 1.08F);
        addAlabasterSound(sounds, FASoundEvents.ALABASTER_PLACE, "minecraft:block.calcite.place", 0.5F, 0.94F, "minecraft:block.amethyst_block.place", 0.26F, 1.08F);
        addAlabasterSound(sounds, FASoundEvents.ALABASTER_HIT, "minecraft:block.calcite.hit", 0.5F, 0.94F, "minecraft:block.amethyst_block.hit", 0.26F, 1.08F);
        addAlabasterSound(sounds, FASoundEvents.ALABASTER_FALL, "minecraft:block.calcite.fall", 0.5F, 0.94F, "minecraft:block.amethyst_block.fall", 0.26F, 1.08F);
        return DataProvider.saveStable(cachedOutput, sounds, this.sounds.json(new ResourceLocation(FarmersAssortment.MOD_ID, "sounds")));
    }

    private void addAlabasterSound(JsonObject sounds, RegistryObject<SoundEvent> soundEvent,
                                   String primarySound, float primaryVolume, float primaryPitch,
                                   String accentSound, float accentVolume, float accentPitch) {
        sounds.add(soundEvent.getId().getPath(), layeredAlabasterSound(primarySound, primaryVolume, primaryPitch, accentSound, accentVolume, accentPitch));
    }

    private JsonObject layeredAlabasterSound(String primarySound, float primaryVolume, float primaryPitch,
                                             String accentSound, float accentVolume, float accentPitch) {
        JsonObject definition = new JsonObject();
        JsonArray soundEntries = new JsonArray();
        soundEntries.add(sound(primarySound, primaryVolume, primaryPitch));
        soundEntries.add(sound(accentSound, accentVolume, accentPitch));
        definition.add("sounds", soundEntries);
        return definition;
    }

    private JsonObject sound(String name, float volume, float pitch) {
        JsonObject sound = new JsonObject();
        sound.addProperty("name", name);
        sound.addProperty("volume", volume);
        sound.addProperty("pitch", pitch);
        return sound;
    }

    @Override
    public String getName() {
        return "Farmer's Assortment Sound Definitions";
    }
}
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
        addSound(sounds, FASoundEvents.ALABASTER_BREAK, "minecraft:block.calcite.break", 0.55F, 1.8F, "minecraft:block.amethyst_block.place", 0.40F, .6F);
        addSound(sounds, FASoundEvents.ALABASTER_STEP, "minecraft:block.calcite.step", 0.55F, 1.8F, "minecraft:block.amethyst_block.step", 0.40F, .6F);
        addSound(sounds, FASoundEvents.ALABASTER_PLACE, "minecraft:block.calcite.place", 0.55F, 1.8F, "minecraft:block.amethyst_block.place", 0.40F, .6F);
        addSound(sounds, FASoundEvents.ALABASTER_HIT, "minecraft:block.calcite.hit", 0.55F, 1.8F, "minecraft:block.amethyst_block.hit", 0.40F, .6F);
        addSound(sounds, FASoundEvents.ALABASTER_FALL, "minecraft:block.calcite.fall", 0.55F, 1.8F, "minecraft:block.amethyst_block.fall", 0.40F, .6F);
        return DataProvider.saveStable(cachedOutput, sounds, this.sounds.json(new ResourceLocation(FarmersAssortment.MOD_ID, "sounds")));
    }

    private void addSound(JsonObject sounds, RegistryObject<SoundEvent> soundEvent,
                                   String primarySound, float primaryVolume, float primaryPitch,
                                   String accentSound, float accentVolume, float accentPitch) {
        sounds.add(soundEvent.getId().getPath(), layeredSound(primarySound, primaryVolume, primaryPitch, accentSound, accentVolume, accentPitch));
    }

    private JsonObject layeredSound(String primarySound, float primaryVolume, float primaryPitch,
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
        sound.addProperty("type", "event");
        sound.addProperty("volume", volume);
        sound.addProperty("pitch", pitch);
        return sound;
    }

    @Override
    public String getName() {
        return "Farmer's Assortment Sound Definitions";
    }
}
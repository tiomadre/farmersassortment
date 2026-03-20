package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FASoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FarmersAssortment.MOD_ID);

    public static final RegistryObject<SoundEvent> ALABASTER_BREAK = register("block_alabaster_break");
    public static final RegistryObject<SoundEvent> ALABASTER_STEP = register("block_alabaster_step");
    public static final RegistryObject<SoundEvent> ALABASTER_PLACE = register("block_alabaster_place");
    public static final RegistryObject<SoundEvent> ALABASTER_HIT = register("block_alabaster_hit");
    public static final RegistryObject<SoundEvent> ALABASTER_FALL = register("block_alabaster_fall");

    private FASoundEvents() {
    }

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(FarmersAssortment.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
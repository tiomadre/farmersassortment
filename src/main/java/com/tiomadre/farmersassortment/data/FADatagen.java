package com.tiomadre.farmersassortment.data;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.data.client.FABlockStates;
import com.tiomadre.farmersassortment.data.client.FAItemModels;
import com.tiomadre.farmersassortment.data.server.recipes.FACrafting;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FADatagen {
    private FADatagen() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(true, new FACrafting(generator.getPackOutput()));

        }
        if (event.includeClient()) {
            generator.addProvider(true, new FABlockStates(generator.getPackOutput(), event.getExistingFileHelper()));
            generator.addProvider(true, new FAItemModels(generator.getPackOutput(), event.getExistingFileHelper()));
        }
    }
}

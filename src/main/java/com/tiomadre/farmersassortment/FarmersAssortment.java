package com.tiomadre.farmersassortment;

import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FACrafting;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FarmersAssortment.MOD_ID)
public class FarmersAssortment {
    public static final String MOD_ID = "farmersassortment";
    private static final Logger LOGGER = LogManager.getLogger();

    public FarmersAssortment() {
        LOGGER.info("Loading Farmer's Assortment");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FABlocks.register(modEventBus);
        FAItems.register(modEventBus);
        FACrafting.register(modEventBus);
    }
}

package com.tiomadre.farmersassortment;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FarmersAssortment.MOD_ID)
public class FarmersAssortment {
    public static final String MOD_ID = "farmersassortment";
    private static final Logger LOGGER = LogManager.getLogger();

    public FarmersAssortment() {
        LOGGER.info("Loading Farmer's Assortment");
    }
}

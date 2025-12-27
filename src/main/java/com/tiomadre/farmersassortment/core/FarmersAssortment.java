package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import com.tiomadre.farmersassortment.core.registry.FATab;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.data.server.recipes.FACrafting;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.HashSet;
import java.util.Set;

@Mod(FarmersAssortment.MOD_ID)
public class FarmersAssortment {
    public static final String MOD_ID = "farmersassortment";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public FarmersAssortment() {
        LOGGER.info("Loading Farmer's Assortment");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        FAxCrabbersBlocks.init();
        FABlocks.init();
        FAItems.init();
        REGISTRY_HELPER.register(modEventBus);
        FABlockEntityTypes.register(modEventBus);
        FATab.register(modEventBus);
        FACrafting.register(modEventBus);
        modEventBus.addListener(FABlocks::onCommonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityTypeAccessor cookingPotAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.COOKING_POT.get();
            Set<Block> validBlocks = cookingPotAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedValidBlocks = new HashSet<>(validBlocks);
            boolean changed = false;

            for (Block block : new Block[]{FABlocks.COPPER_COOKING_POT.get(), FABlocks.GOLDEN_COOKING_POT.get(), FABlocks.TERRACOTTA_COOKING_POT.get()}) {
                if (!updatedValidBlocks.contains(block)) {
                    updatedValidBlocks.add(block);
                    changed = true;
                }
            }

            if (changed) {
                cookingPotAccessor.farmersassortment$setValidBlocks(updatedValidBlocks);
            }
        });
    }
}
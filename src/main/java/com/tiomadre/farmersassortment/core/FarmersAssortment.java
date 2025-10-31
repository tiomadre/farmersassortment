package com.tiomadre.farmersassortment.core;

import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FATab;
import com.tiomadre.farmersassortment.data.server.recipes.FACrafting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.Set;

@Mod(FarmersAssortment.MOD_ID)
public class FarmersAssortment {
    public static final String MOD_ID = "farmersassortment";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public FarmersAssortment() {
        LOGGER.info("Loading Farmer's Assortment");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRY_HELPER.register(modEventBus);
        FATab.register(modEventBus);
        FACrafting.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityType<?> cookingPot = ModBlockEntityTypes.COOKING_POT.get();
            Set<Block> cookingPotBlocks = ((BlockEntityTypeAccessor) (Object) cookingPot).farmersassortment$getValidBlocks();
            cookingPotBlocks.add(FABlocks.COPPER_COOKING_POT.get());

            BlockEntityType<?> cuttingBoard = ModBlockEntityTypes.CUTTING_BOARD.get();
            Set<Block> cuttingBoardBlocks = ((BlockEntityTypeAccessor) (Object) cuttingBoard).farmersassortment$getValidBlocks();
            FABlocks.cuttingBoards()
                    .map(RegistryObject::get)
                    .forEach(cuttingBoardBlocks::add);
        });
    }
}

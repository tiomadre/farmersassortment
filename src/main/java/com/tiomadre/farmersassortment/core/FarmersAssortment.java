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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod(FarmersAssortment.MOD_ID)
public class FarmersAssortment {
    public static final String MOD_ID = "farmersassortment";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    private static final String FORAGERS_COMPAT_CLASS = "com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks";
    private static boolean crabbersCompatEnabled;
    private static boolean foragersCompatEnabled;

    public FarmersAssortment() {
        LOGGER.info("Loading Farmer's Assortment");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        crabbersCompatEnabled = initializeCompat("crabbersdelight", FAxCrabbersBlocks::init);
        foragersCompatEnabled = initializeCompat("foragersinsight", FarmersAssortment::initializeForagersCompat);
        FABlocks.init();
        FAItems.init();
        REGISTRY_HELPER.register(modEventBus);
        FABlockEntityTypes.register(modEventBus);
        FATab.register(modEventBus);
        FACrafting.register(modEventBus);
        modEventBus.addListener(FABlocks::onCommonSetup);
        if (crabbersCompatEnabled) {
            modEventBus.addListener(FAxCrabbersBlocks::onCommonSetup);
        }
        if (foragersCompatEnabled) {
            modEventBus.addListener(FarmersAssortment::onForagersCommonSetup);
        }
    }

    public static boolean isCrabbersCompatEnabled() {
        return crabbersCompatEnabled;
    }

    public static boolean isForagersCompatEnabled() {
        return foragersCompatEnabled;
    }

    private boolean initializeCompat(String modId, Runnable compatInitializer) {
        if (!ModList.get().isLoaded(modId)) {
            return false;
        }

        try {
            compatInitializer.run();
            return true;
        } catch (Throwable throwable) {
            LOGGER.error("Failed to initialize {} compat, disabling integration.", modId, throwable);
            return false;
        }
    }
    private static void initializeForagersCompat() {
        invokeCompatStatic(FORAGERS_COMPAT_CLASS, "init");
    }

    private static void onForagersCommonSetup(FMLCommonSetupEvent event) {
        invokeCompatStatic(FORAGERS_COMPAT_CLASS, "onCommonSetup", FMLCommonSetupEvent.class, event);
    }

    private static void invokeCompatStatic(String className, String methodName) {
        invokeCompatStatic(className, methodName, new Class<?>[0]);
    }

    private static void invokeCompatStatic(String className, String methodName, Class<?> parameterType, Object argument) {
        invokeCompatStatic(className, methodName, new Class<?>[]{parameterType}, argument);
    }

    private static void invokeCompatStatic(String className, String methodName, Class<?>[] parameterTypes, Object... arguments) {
        try {
            Class<?> compatClass = Class.forName(className);
            Method method = compatClass.getMethod(methodName, parameterTypes);
            method.invoke(null, arguments);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to invoke compat method " + className + "#" + methodName, exception);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityTypeAccessor cookingPotAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.COOKING_POT.get();
            Set<Block> validBlocks = cookingPotAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedValidBlocks = new HashSet<>(validBlocks);
            AtomicBoolean changed = new AtomicBoolean(false);

            FABlocks.allCookingPots()
                    .map(RegistryObject::get)
                    .forEach(block -> {
                        if (!updatedValidBlocks.contains(block)) {
                            updatedValidBlocks.add(block);
                            changed.set(true);
                        }
                    });

            if (changed.get()) {
                cookingPotAccessor.farmersassortment$setValidBlocks(updatedValidBlocks);
            }

            BlockEntityTypeAccessor skilletAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.SKILLET.get();
            Set<Block> validSkilletBlocks = skilletAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedSkilletBlocks = new HashSet<>(validSkilletBlocks);
            AtomicBoolean skilletChanged = new AtomicBoolean(false);

            if (isCrabbersCompatEnabled()) {
                FAxCrabbersBlocks.skillets()
                        .map(RegistryObject::get)
                        .forEach(block -> {
                            if (!updatedSkilletBlocks.contains(block)) {
                                updatedSkilletBlocks.add(block);
                                skilletChanged.set(true);
                            }
                        });
            }

            if (skilletChanged.get()) {
                skilletAccessor.farmersassortment$setValidBlocks(updatedSkilletBlocks);
            }
        });
    }
}
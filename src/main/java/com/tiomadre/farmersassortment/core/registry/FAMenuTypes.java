package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.menu.RackMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.tiomadre.farmersassortment.core.menu.FloatingDrawerMenu;

public final class FAMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FarmersAssortment.MOD_ID);

    public static final RegistryObject<MenuType<RackMenu>> RACK =
            MENU_TYPES.register("rack", () -> IForgeMenuType.create((windowId, inventory, data) -> new RackMenu(windowId, inventory)));

    public static final RegistryObject<MenuType<FloatingDrawerMenu>> FLOATING_COUNTER =
            MENU_TYPES.register("floating_counter",
                    () -> IForgeMenuType.create(FloatingDrawerMenu::new));

    private FAMenuTypes() {
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
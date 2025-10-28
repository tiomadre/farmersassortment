package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;

public final class FATab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersAssortment.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FA_TAB = TABS.register("fa_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.farmersassortment"))
            .icon(() -> new ItemStack(FABlocks.SPRUCE_CUTTING_BOARD.get()))
            .displayItems((parameters, output) ->
                    FAItems.ITEMS.getEntries().stream()
                            .sorted(Comparator.comparing(registryObject -> registryObject.getId().getPath()))
                            .map(RegistryObject::get)
                            .map(Item::getDefaultInstance)
                            .forEach(output::accept))
            .build());

    private FATab() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}

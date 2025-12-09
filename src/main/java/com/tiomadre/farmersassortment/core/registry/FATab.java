package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;


public final class FATab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersAssortment.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FA_TAB = TABS.register("fa_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.farmersassortment"))
            .icon(() -> new ItemStack(FABlocks.OAK_BUTCHER_BLOCK_CABINET.get()))
            .displayItems((parameters, output) ->
                    Stream.concat(
                                    FABlocks.BLOCKS.getDeferredRegister().getEntries().stream()
                                            .map(registryObject -> Map.<ResourceLocation, Supplier<? extends ItemLike>>entry(
                                                    registryObject.getId(), registryObject)),
                                    FAItems.ITEMS.getDeferredRegister().getEntries().stream()
                                            .map(registryObject -> Map.<ResourceLocation, Supplier<? extends ItemLike>>entry(
                                                    registryObject.getId(), registryObject)))
                            .sorted(Comparator
                                    .comparingInt((Map.Entry<ResourceLocation, Supplier<? extends ItemLike>> entry) ->
                                            categoryOrder(entry.getKey().getPath()))
                                    .thenComparing(entry -> entry.getKey().getPath()))
                            .map(Map.Entry::getValue)
                            .map(Supplier::get)
                            .map(ItemStack::new)
                            .forEach(output::accept))
            .build());

    private FATab() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    private static int categoryOrder(String path) {
        if (path.contains("butcher_block")) {
            return 0;
        }
        if (path.contains("_stove")) {
            return 1;
        }
        if (path.contains("_cooking_pot")) {
            return 2;
        }
        if (path.contains("cutting_board")) {
            return 3;
        }
        if (path.endsWith("_knife")) {
            return 4;
        }
        return 5;
    }
}

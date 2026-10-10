package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
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
                                    Stream.concat(
                                            FADynamicFurniture.blocks()
                                                    .map(registryObject -> Map.<ResourceLocation, Supplier<? extends ItemLike>>entry(
                                                            registryObject.getId(), registryObject)),
                                            FAItems.ITEMS.getDeferredRegister().getEntries().stream()
                                                    .map(registryObject -> Map.<ResourceLocation, Supplier<? extends ItemLike>>entry(
                                                            registryObject.getId(), registryObject))))
                            .filter(entry -> shouldDisplay(entry.getKey()))
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
        if (path.endsWith("_canvas_rug")) {
            return 1;
        }
        if (path.contains("_cooking_pot")) {
            return 2;
        }
        if (path.endsWith("_crab_trap")) {
            return 3;
        }
        if (path.contains("cutting_board")) {
            return 4;
        }
        if (path.endsWith("_diffuser")) {
            return 5;
        }
        if (path.endsWith("_floating_counter")) {
            return 6;
        }
        if (path.endsWith("_knife")) {
            return 7;
        }
        if (path.endsWith("_rack")) {
            return 8;
        }
        if (path.endsWith("_slats")) {
            return 9;
        }
        if (path.endsWith("_stool")) {
            return 10;
        }
        if (path.contains("_stove")) {
            return 11;
        }
        if (path.endsWith("_table")) {
            return 12;
        }
        return 13;
    }

    private static final Set<String> CRABBERS_ITEMS = Set.of(
            "palm_floating_counter",
            "clamshell_knife",
            "lilac_crab_trap",
            "palm_cutting_board",
            "palm_butcher_block_cabinet",
            "palm_crab_trap",
            "pearlescent_cooking_pot",
            "pearlescent_skillet",
            "pearlescent_diffuser"

    );
    private static final Set<String> FORAGERS_ITEMS = Set.of(
            "lilac_floating_counter",
            "lilac_cutting_board",
            "lilac_butcher_block_cabinet",
            "amethyst_diffuser",
            "alabaster_diffuser",
            "pearlescent_diffuser"
    );

    private static boolean shouldDisplay(ResourceLocation id) {
        String path = id.getPath();
        if ((CRABBERS_ITEMS.contains(path) || path.endsWith("_crab_trap")) && !ModList.get().isLoaded("crabbersdelight")) {
            return false;
        }
        return !FORAGERS_ITEMS.contains(path) || ModList.get().isLoaded("foragersinsight");
    }
}

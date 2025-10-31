package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.KnifeItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import static com.tiomadre.farmersassortment.core.registry.FABlocks.*;

public final class FAItems {
    public static final ItemSubRegistryHelper ITEMS = FarmersAssortment.REGISTRY_HELPER.getItemSubHelper();

    public static final RegistryObject<Item> AMETHYST_KNIFE = ITEMS.createItem("amethyst_knife",
            () -> new KnifeItem(Tiers.IRON, 0.5F, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> QUARTZ_KNIFE = ITEMS.createItem("quartz_knife",
            () -> new KnifeItem(Tiers.IRON, 0.5F, -2.0F, new Item.Properties()));
    private FAItems() {
    }
}

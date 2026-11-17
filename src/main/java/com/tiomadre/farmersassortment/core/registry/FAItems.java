package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.KnifeItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;


public final class FAItems {
    public static final ItemSubRegistryHelper ITEMS = FarmersAssortment.REGISTRY_HELPER.getItemSubHelper();

    public static final RegistryObject<Item> ALABASTER = ITEMS.createItem("alabaster", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOASTED_PEPITAS = ITEMS.createItem("toasted_pepitas", () ->
            new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.5F).fast().build())));

    public static final RegistryObject<Item> AMETHYST_KNIFE = ITEMS.createItem("amethyst_knife",
            () -> new KnifeItem(Tiers.STONE, 0.5F, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> QUARTZ_KNIFE = ITEMS.createItem("quartz_knife",
            () -> new KnifeItem(Tiers.STONE, 0.5F, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> CLAMSHELL_KNIFE = ITEMS.createItem("clamshell_knife",
            () -> new KnifeItem(Tiers.WOOD, 0.5F, -2.0F, new Item.Properties()));
    public static final RegistryObject<Item> ALABASTER_KNIFE = ITEMS.createItem("alabaster_knife",
            () -> new KnifeItem(Tiers.DIAMOND, 0.5F, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> CANDIED_YELLOW_PUMPKIN = ITEMS.createItem("candied_yellow_pumpkin_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationMod(0.6F)
                    .build())));
    public static final RegistryObject<Item> CANDIED_WHITE_PUMPKIN = ITEMS.createItem("candied_white_pumpkin_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationMod(0.6F)
                    .build())));
    public static final RegistryObject<Item> CANDIED_ORANGE_PUMPKIN = ITEMS.createItem("candied_pumpkin_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationMod(0.6F)
                    .build())));
    public static final RegistryObject<Item> CANDIED_RED_PUMPKIN = ITEMS.createItem("candied_red_pumpkin_slice",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(5)
                    .saturationMod(0.6F)
                    .build())));

    private FAItems() {
    }
    public static void init() {
    }
}



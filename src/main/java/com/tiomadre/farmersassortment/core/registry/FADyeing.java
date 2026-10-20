package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.recipe.DyeRecipe;
import com.tiomadre.farmersassortment.core.recipe.WashRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class FADyeing {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FarmersAssortment.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersAssortment.MOD_ID);

    public static final RegistryObject<BlockEntityType<RopeColorBlockEntity>> ROPE_COLOR =
            BLOCK_ENTITIES.register("rope_color", () -> BlockEntityType.Builder.of(
                    RopeColorBlockEntity::new, ModBlocks.ROPE_FENCE.get(),
                    ModBlocks.ROPE_FENCE_GATE.get(), FABlocks.DIVIDER.get()).build(null));

    public static final RegistryObject<RecipeSerializer<DyeRecipe>> DIVIDER_DYE =
            RECIPES.register("upcycled_divider_dye", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new DyeRecipe(id, category,
                            () -> FABlocks.DIVIDER.get().asItem(), false,
                            FADyeing.DIVIDER_DYE)));
    public static final RegistryObject<RecipeSerializer<WashRecipe>> DIVIDER_WASH =
            RECIPES.register("upcycled_divider_wash", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new WashRecipe(id, category,
                            () -> FABlocks.DIVIDER.get().asItem(),
                            FADyeing.DIVIDER_WASH)));
    public static final RegistryObject<RecipeSerializer<DyeRecipe>> FENCE_DYE =
            RECIPES.register("rope_fence_dye", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new DyeRecipe(id, category,
                            () -> ModBlocks.ROPE_FENCE.get().asItem(), false,
                            FADyeing.FENCE_DYE)));
    public static final RegistryObject<RecipeSerializer<DyeRecipe>> GATE_DYE =
            RECIPES.register("rope_fence_gate_dye", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new DyeRecipe(id, category,
                            () -> ModBlocks.ROPE_FENCE_GATE.get().asItem(), true,
                            FADyeing.GATE_DYE)));
    public static final RegistryObject<RecipeSerializer<WashRecipe>> FENCE_WASH =
            RECIPES.register("rope_fence_wash", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new WashRecipe(id, category,
                            () -> ModBlocks.ROPE_FENCE.get().asItem(),
                            FADyeing.FENCE_WASH)));
    public static final RegistryObject<RecipeSerializer<WashRecipe>> GATE_WASH =
            RECIPES.register("rope_fence_gate_wash", () -> new SimpleCraftingRecipeSerializer<>(
                    (id, category) -> new WashRecipe(id, category,
                            () -> ModBlocks.ROPE_FENCE_GATE.get().asItem(),
                            FADyeing.GATE_WASH)));

    private FADyeing() {}

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
        RECIPES.register(bus);
    }
}

package com.tiomadre.farmersassortment.core.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FAPumpkins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID)
public final class FAPumpkinRecipes {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> INPUT_FIELDS =
            List.of("ingredient", "ingredients", "key", "input", "inputs", "base", "addition");

    private FAPumpkinRecipes() {
    }

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        RecipeManager manager = event.getServerResources().getRecipeManager();
        ICondition.IContext context = event.getConditionContext();

        event.addListener(new SimpleJsonResourceReloadListener(new Gson(), "recipes") {
            @Override
            protected void apply(Map<ResourceLocation, JsonElement> resources,
                                 ResourceManager resourceManager, ProfilerFiller profiler) {
                Map<ResourceLocation, Recipe<?>> updated = new LinkedHashMap<>();
                manager.getRecipes().forEach(recipe -> updated.put(recipe.getId(), recipe));

                resources.forEach((id, element) -> {
                    Recipe<?> original = updated.get(id);
                    if (original == null || !element.isJsonObject()) {
                        return;
                    }

                    var output = original.getResultItem(event.getRegistryAccess());
                    boolean protectedOutput = output.is(Items.PUMPKIN)
                            || output.is(Items.PUMPKIN_SEEDS)
                            || FAPumpkins.VARIANTS.stream().anyMatch(variant ->
                            output.is(variant.pumpkin.get().asItem())
                                    || output.is(variant.carved.get().asItem())
                                    || output.is(variant.lantern.get().asItem())
                                    || output.is(variant.seeds.get()));

                    if (protectedOutput) {
                        return;
                    }

                    JsonObject json = element.getAsJsonObject().deepCopy();
                    boolean changed = false;
                    for (String field : INPUT_FIELDS) {
                        if (json.has(field)) {
                            changed |= expand(json.get(field));
                        }
                    }

                    if (changed) {
                        try {
                            Recipe<?> replacement = RecipeManager.fromJson(id, json, context);
                            if (replacement != null) {
                                updated.put(id, replacement);
                            }
                        } catch (RuntimeException exception) {
                            LOGGER.error("Unable to add in recipe {}", id, exception);
                        }
                    }
                });

                manager.replaceRecipes(updated.values());
            }
        });
    }

    private static boolean expand(JsonElement element) {
        if (element.isJsonArray()) {
            boolean changed = false;
            for (JsonElement child : element.getAsJsonArray()) {
                changed |= expand(child);
            }
            return changed;
        }

        if (!element.isJsonObject()) {
            return false;
        }

        JsonObject object = element.getAsJsonObject();
        if (object.has("item")
                && object.get("item").isJsonPrimitive()
                && object.get("item").getAsString().equals("farmersdelight:pumpkin_slice")) {
            object.remove("item");
            object.addProperty("tag", "farmersassortment:pumpkin_slices");
            return true;
        }

        boolean changed = false;
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            changed |= expand(entry.getValue());
        }
        return changed;
    }
}

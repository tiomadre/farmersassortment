package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import com.tiomadre.farmersassortment.core.block.RackBlock;
import com.tiomadre.farmersassortment.core.block.TableBlock;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import com.tiomadre.farmersassortment.core.item.TableItem;
import com.google.gson.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddPackFindersEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FADynamicFurniture extends AbstractPackResources {
    private static final Set<String> EXCLUDED_NAMESPACES = Set.of("minecraft", FarmersAssortment.MOD_ID);
    private static final Set<ResourceLocation> EXCLUDED_PLANKS = Set.of(
            new ResourceLocation("foragersinsight", "lilac_planks")
    );

    private static final Map<String, RegistryObject<StoolBlock>> DYNAMIC_STOOLS = new LinkedHashMap<>();
    private static final List<DynamicStoolDefinition> DYNAMIC_DEFINITIONS = new ArrayList<>();

    private static final Map<String, RegistryObject<RackBlock>> DYNAMIC_RACKS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<TableBlock>> DYNAMIC_TABLES = new LinkedHashMap<>();
    private static final List<FurnitureDefinition> FURNITURE_DEFINITIONS = new ArrayList<>();
    private final PackType type;
    private final Map<ResourceLocation, byte[]> resources = new LinkedHashMap<>();

    private FADynamicFurniture(String id, PackType type) {
        super(id, true);
        this.type = type;
        JsonArray furniture = new JsonArray();
        JsonArray stools = new JsonArray();
        JsonArray tables = new JsonArray();
        for (FurnitureDefinition definition : FURNITURE_DEFINITIONS) {
            String source = "oak_" + definition.kind();
            if (type == PackType.CLIENT_RESOURCES) {
                copy("blockstates/" + source + ".json", source, definition);
                copy("models/item/" + source + ".json", source, definition);
            } else {
                copy("recipes/" + source + ".json", source, definition);
                copy("loot_tables/blocks/" + source + ".json", source, definition);
                copy("advancements/recipes/decorations/" + source + ".json", source, definition);
            }
            furniture.add(definition.id().toString());
            if (definition.kind().equals("stool")) stools.add(definition.id().toString());
            if (definition.kind().equals("table")) tables.add(definition.id().toString());
        }
        if (type == PackType.SERVER_DATA) {
            addTag(new ResourceLocation("minecraft", "tags/blocks/mineable/axe.json"), furniture);
            addTag(new ResourceLocation(FarmersAssortment.MOD_ID, "tags/blocks/stools.json"), stools);
            addTag(new ResourceLocation(FarmersAssortment.MOD_ID, "tags/blocks/tables.json"), tables);
        }
    }

    public static Stream<RegistryObject<StoolBlock>> stools() {
        return DYNAMIC_STOOLS.values().stream();
    }

    public static Stream<RegistryObject<RackBlock>> racks() {
        return DYNAMIC_RACKS.values().stream();
    }

    public static Stream<RegistryObject<TableBlock>> tables() {
        return DYNAMIC_TABLES.values().stream();
    }

    public static Stream<RegistryObject<? extends Block>> blocks() {
        return Stream.concat(stools().map(block -> (RegistryObject<? extends Block>) block),
                Stream.concat(racks().map(block -> (RegistryObject<? extends Block>) block),
                        tables().map(block -> (RegistryObject<? extends Block>) block)));
    }

    public static List<DynamicStoolDefinition> stoolDefinitions() {
        return List.copyOf(DYNAMIC_DEFINITIONS);
    }

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.LOWEST, FADynamicFurniture::onRegister);
        eventBus.addListener(FADynamicFurniture::onAddPackFinders);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            discoverAndRegisterBlocks(event);
        } else if (event.getRegistryKey().equals(Registries.ITEM)) {
            registerItems(event);
        }
    }

    private static void discoverAndRegisterBlocks(RegisterEvent event) {
        List<ResourceLocation> planks = ForgeRegistries.BLOCKS.getKeys().stream()
                .filter(id -> !EXCLUDED_NAMESPACES.contains(id.getNamespace()))
                .filter(id -> id.getPath().endsWith("_planks"))
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        event.register(ForgeRegistries.Keys.BLOCKS, helper -> planks.forEach(planksId -> {
            String woodType = woodType(planksId);
            ResourceLocation slabId = new ResourceLocation(planksId.getNamespace(), woodType + "_slab");
            String prefix = planksId.getNamespace() + "_" + woodType;
            ResourceLocation texture = new ResourceLocation(planksId.getNamespace(), "block/" + planksId.getPath());
            ResourceLocation rackId = new ResourceLocation(FarmersAssortment.MOD_ID, prefix + "_rack");
            helper.register(rackId, new RackBlock(BlockBehaviour.Properties.copy(resolveBlock(planksId)).noOcclusion()));
            DYNAMIC_RACKS.put(rackId.getPath(), RegistryObject.create(rackId, ForgeRegistries.BLOCKS));
            FURNITURE_DEFINITIONS.add(new FurnitureDefinition(rackId, "rack", planksId, texture,
                    displayName(woodType) + " Rack"));
            if (!(FarmersAssortment.isForagersCompatEnabled()
                    && planksId.equals(new ResourceLocation("foragersinsight", "lilac_planks")))) {
                ResourceLocation tableId = new ResourceLocation(FarmersAssortment.MOD_ID, prefix + "_table");
                helper.register(tableId, new TableBlock(BlockBehaviour.Properties.copy(resolveBlock(planksId)).noOcclusion()));
                DYNAMIC_TABLES.put(tableId.getPath(), RegistryObject.create(tableId, ForgeRegistries.BLOCKS));
                FURNITURE_DEFINITIONS.add(new FurnitureDefinition(tableId, "table", planksId, texture,
                        displayName(woodType) + " Table"));
            }
            if (!isSupportedPlanks(planksId)) return;
            String stoolName = planksId.getNamespace() + "_" + woodType + "_stool";
            ResourceLocation stoolId = new ResourceLocation(FarmersAssortment.MOD_ID, stoolName);
            StoolBlock stoolBlock = new StoolBlock(BlockBehaviour.Properties.copy(resolveBlock(planksId)).noOcclusion());

            helper.register(stoolId, stoolBlock);
            RegistryObject<StoolBlock> stool = RegistryObject.create(stoolId, ForgeRegistries.BLOCKS);
            DYNAMIC_STOOLS.put(stoolName, stool);
            DYNAMIC_DEFINITIONS.add(new DynamicStoolDefinition(stool, planksId, slabId,
                    inferSeatTexture(planksId.getNamespace(), woodType)));
            FURNITURE_DEFINITIONS.add(new FurnitureDefinition(stoolId, "stool", slabId, texture,
                    displayName(woodType) + " Stool"));
        }));
    }

    private static boolean isSupportedPlanks(ResourceLocation id) {
        if (EXCLUDED_NAMESPACES.contains(id.getNamespace()) || EXCLUDED_PLANKS.contains(id)) {
            return false;
        }

        String path = id.getPath();
        if (!path.endsWith("_planks")) {
            return false;
        }

        ResourceLocation slabId = new ResourceLocation(id.getNamespace(), woodType(id) + "_slab");
        return ForgeRegistries.BLOCKS.containsKey(slabId);
    }

    private static String woodType(ResourceLocation planksId) {
        String path = planksId.getPath();
        return path.substring(0, path.length() - "_planks".length());
    }

    private static void registerItems(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> FURNITURE_DEFINITIONS.forEach(definition -> {
            Item item = switch (definition.kind()) {
                case "stool" -> new StoolItem(DYNAMIC_STOOLS.get(definition.id().getPath()).get(),
                        new Item.Properties(), definition.name());
                case "table" -> new TableItem(DYNAMIC_TABLES.get(definition.id().getPath()).get(), new Item.Properties()) {
                    @Override
                    public Component getName(ItemStack stack) {
                        return Component.translatableWithFallback(getDescriptionId(stack), definition.name());
                    }
                };
                default -> new BlockItem(DYNAMIC_RACKS.get(definition.id().getPath()).get(), new Item.Properties()) {
                    @Override
                    public Component getName(ItemStack stack) {
                        return Component.translatableWithFallback(getDescriptionId(stack), definition.name());
                    }
                };
            };
            helper.register(definition.id(), item);
        }));
    }

    private static String displayName(String woodType) {
        return Stream.of(woodType.split("_"))
                .filter(part -> !part.isEmpty())
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1))
                .collect(Collectors.joining(" "));
    }
    private static ResourceLocation inferSeatTexture(String namespace, String woodType) {
        ResourceLocation strippedLog = new ResourceLocation(namespace, "stripped_" + woodType + "_log");
        if (ForgeRegistries.BLOCKS.containsKey(strippedLog)) {
            return new ResourceLocation(namespace, "block/stripped_" + woodType + "_log");
        }

        ResourceLocation strippedStem = new ResourceLocation(namespace, "stripped_" + woodType + "_stem");
        if (ForgeRegistries.BLOCKS.containsKey(strippedStem)) {
            return new ResourceLocation(namespace, "block/stripped_" + woodType + "_stem");
        }

        ResourceLocation strippedBlock = new ResourceLocation(namespace, "stripped_" + woodType + "_block");
        if (ForgeRegistries.BLOCKS.containsKey(strippedBlock)) {
            return new ResourceLocation(namespace, "block/stripped_" + woodType + "_block");
        }

        return new ResourceLocation("minecraft", "block/stripped_oak_log");
    }

    private static Block resolveBlock(ResourceLocation id) {
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        return block == null || block == Blocks.AIR ? Blocks.OAK_PLANKS : block;
    }

    private static void onAddPackFinders(AddPackFindersEvent event) {
        PackType type = event.getPackType();
        event.addRepositorySource(consumer -> {
            Pack pack = Pack.readMetaAndCreate(FarmersAssortment.MOD_ID + ":dynamic_furniture",
                    Component.literal("Farmer's Assortment Dynamic Furniture"), true,
                    id -> new FADynamicFurniture(id, type), type, Pack.Position.BOTTOM, PackSource.BUILT_IN);
            if (pack != null) consumer.accept(pack);
        });
    }

    private void copy(String path, String source, FurnitureDefinition definition) {
        ResourceLocation target = new ResourceLocation(FarmersAssortment.MOD_ID,
                path.replace(source, definition.id().getPath()));
        if (resources.containsKey(target)) return;
        String root = type == PackType.CLIENT_RESOURCES ? "assets" : "data";
        String location = "/" + root + "/" + FarmersAssortment.MOD_ID + "/" + path;
        JsonElement original;
        try (InputStream stream = FarmersAssortment.class.getResourceAsStream(location)) {
            if (stream == null) throw new IllegalStateException("Missing furniture template: " + location);
            original = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read furniture template: " + location, exception);
        }
        resources.put(target, transform(original, source, definition).toString().getBytes(StandardCharsets.UTF_8));
        if (type == PackType.CLIENT_RESOURCES) copyReferencedModels(original, source, definition);
    }

    private void copyReferencedModels(JsonElement element, String source, FurnitureDefinition definition) {
        if (element.isJsonObject()) {
            element.getAsJsonObject().entrySet().forEach(entry -> copyReferencedModels(entry.getValue(), source, definition));
        } else if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(value -> copyReferencedModels(value, source, definition));
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String value = element.getAsString();
            String prefix = FarmersAssortment.MOD_ID + ":";
            if (value.startsWith(prefix + "block/" + source) || value.startsWith(prefix + "item/" + source)) {
                copy("models/" + value.substring(prefix.length()) + ".json", source, definition);
            }
        }
    }

    private JsonElement transform(JsonElement element, String source, FurnitureDefinition definition) {
        if (element.isJsonObject()) {
            JsonObject result = new JsonObject();
            element.getAsJsonObject().entrySet().forEach(entry ->
                    result.add(entry.getKey(), transform(entry.getValue(), source, definition)));
            return result;
        }
        if (element.isJsonArray()) {
            JsonArray result = new JsonArray();
            element.getAsJsonArray().forEach(value -> result.add(transform(value, source, definition)));
            return result;
        }
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String value = element.getAsString();
            if (value.equals("minecraft:oak_planks") || value.equals("minecraft:oak_slab")) {
                value = definition.ingredientId().toString();
            } else if (Set.of("minecraft:block/oak_log", "minecraft:block/oak_log_top",
                    "minecraft:block/stripped_oak_log", "minecraft:block/stripped_oak_log_top").contains(value)) {
                value = inferLogTexture(definition.texture(), value).toString();
            } else {
                String prefix = FarmersAssortment.MOD_ID + ":";
                String name = definition.id().getPath();
                value = value.replace(prefix + "block/" + source, prefix + "block/" + name)
                        .replace(prefix + "item/" + source, prefix + "item/" + name)
                        .replace(prefix + "blocks/" + source, prefix + "blocks/" + name);
                if (value.equals(prefix + source)) value = definition.id().toString();
            }
            return new JsonPrimitive(value);
        }
        return element.deepCopy();
    }

    private static ResourceLocation inferLogTexture(ResourceLocation planksTexture, String template) {
        String namespace = planksTexture.getNamespace();
        String path = planksTexture.getPath();
        String woodType = path.substring("block/".length(), path.length() - "_planks".length());
        boolean stripped = template.contains("/stripped_");
        boolean top = template.endsWith("_top");

        List<String> candidates = new ArrayList<>();
        if (stripped) {
            candidates.add("stripped_" + woodType + "_log");
            candidates.add("stripped_" + woodType + "_stem");
            candidates.add("stripped_" + woodType + "_block");
        }
        candidates.add(woodType + "_log");
        candidates.add(woodType + "_stem");
        candidates.add(woodType + "_block");

        for (String candidate : candidates) {
            if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(namespace, candidate))) {
                return new ResourceLocation(namespace, "block/" + candidate + (top ? "_top" : ""));
            }
        }

        return new ResourceLocation(template);
    }

    private void addTag(ResourceLocation id, JsonArray values) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        tag.add("values", values);
        resources.put(id, tag.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        if (!String.join("/", elements).equals("pack.mcmeta")) return null;
        int format = type == PackType.CLIENT_RESOURCES ? 15 : 12;
        byte[] metadata = ("{\"pack\":{\"pack_format\":" + format
                + ",\"description\":\"Dynamic furniture\"}}").getBytes(StandardCharsets.UTF_8);
        return () -> new ByteArrayInputStream(metadata);
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType requestedType, ResourceLocation location) {
        byte[] bytes = requestedType == type ? resources.get(location) : null;
        return bytes == null ? null : () -> new ByteArrayInputStream(bytes);
    }

    @Override
    public void listResources(PackType requestedType, String namespace, String path, ResourceOutput output) {
        if (requestedType != type) return;
        String prefix = path.isEmpty() ? "" : path + "/";
        resources.forEach((id, bytes) -> {
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(prefix)) {
                output.accept(id, () -> new ByteArrayInputStream(bytes));
            }
        });
    }

    @Override
    public Set<String> getNamespaces(PackType requestedType) {
        return requestedType == type ? Set.of(FarmersAssortment.MOD_ID, "minecraft") : Set.of();
    }

    @Override
    public void close() {
    }

    private record FurnitureDefinition(ResourceLocation id, String kind, ResourceLocation ingredientId,
                                       ResourceLocation texture, String name) {
    }

    public record DynamicStoolDefinition(RegistryObject<StoolBlock> block, ResourceLocation planksId,
                                         ResourceLocation slabId, ResourceLocation seatTexture) {
    }
}

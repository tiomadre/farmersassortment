package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import com.tiomadre.farmersassortment.core.block.RackBlock;
import com.tiomadre.farmersassortment.core.block.TableBlock;
import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import com.tiomadre.farmersassortment.core.block.entity.SlatsBlockEntity;
import com.tiomadre.farmersassortment.core.item.SlatsItem;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import com.tiomadre.farmersassortment.core.item.TableItem;
import com.google.gson.*;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public static final TagKey<Block> SOILS = TagKey.create(Registries.BLOCK,
            new ResourceLocation(FarmersAssortment.MOD_ID, "slats_soils"));
    public static final RegistryObject<BlockEntityType<SlatsBlockEntity>> SLATS_ENTITY = RegistryObject.create(
            new ResourceLocation(FarmersAssortment.MOD_ID, "slats"), ForgeRegistries.BLOCK_ENTITY_TYPES);
    private static final Map<String, RegistryObject<SlatsBlock>> DYNAMIC_SLATS = new LinkedHashMap<>();
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
        JsonArray slats = new JsonArray();
        JsonObject language = new JsonObject();
        for (FurnitureDefinition definition : FURNITURE_DEFINITIONS) {
            String source = "oak_" + definition.kind();
            if (definition.kind().equals("slats")) {
                if (type == PackType.CLIENT_RESOURCES) {
                    addSlatsClient(definition);
                    language.addProperty("block." + FarmersAssortment.MOD_ID + "."
                            + definition.id().getPath(), definition.name());
                } else {
                    addSlatsServer(definition);
                }
                slats.add(definition.id().toString());
            } else if (type == PackType.CLIENT_RESOURCES) {
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
            addTag(new ResourceLocation("minecraft", "tags/blocks/climbable.json"), slats);
            put("tags/blocks/slats.json", tag(slats));
            put("tags/items/slats.json", tag(slats));
            JsonArray soils = new JsonArray();
            for (String soil : List.of("#minecraft:dirt", "#minecraft:sand", "minecraft:gravel",
                    "minecraft:mud", "minecraft:clay", "minecraft:soul_sand", "minecraft:soul_soil",
                    "minecraft:farmland", "minecraft:dirt_path")) {
                soils.add(soil);
            }
            put("tags/blocks/slats_soils.json", tag(soils));
        } else {
            put("lang/en_us.json", language);
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

    public static Stream<RegistryObject<SlatsBlock>> slats() {
        return DYNAMIC_SLATS.values().stream();
    }

    public static Stream<RegistryObject<? extends Block>> blocks() {
        return Stream.concat(stools().map(block -> (RegistryObject<? extends Block>) block),
                Stream.concat(racks().map(block -> (RegistryObject<? extends Block>) block),
                        Stream.concat(tables().map(block -> (RegistryObject<? extends Block>) block),
                                slats().map(block -> (RegistryObject<? extends Block>) block))));
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
        } else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> helper.register(SLATS_ENTITY.getId(),
                    BlockEntityType.Builder.of(SlatsBlockEntity::new,
                            slats().map(RegistryObject::get).toArray(Block[]::new)).build(null)));
        }
    }

    private static void discoverAndRegisterBlocks(RegisterEvent event) {
        List<ResourceLocation> planks = ForgeRegistries.BLOCKS.getKeys().stream()
                .filter(id -> !id.getNamespace().equals(FarmersAssortment.MOD_ID))
                .filter(id -> id.getPath().endsWith("_planks"))
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        event.register(ForgeRegistries.Keys.BLOCKS, helper -> planks.forEach(planksId -> {
            String woodType = woodType(planksId);
            String slatsPrefix = planksId.getNamespace().equals("minecraft")
                    ? woodType : planksId.getNamespace() + "_" + woodType;
            ResourceLocation slatsId = new ResourceLocation(FarmersAssortment.MOD_ID, slatsPrefix + "_slats");
            helper.register(slatsId, new SlatsBlock(resolveBlock(planksId).defaultBlockState()));
            DYNAMIC_SLATS.put(slatsId.getPath(), RegistryObject.create(slatsId, ForgeRegistries.BLOCKS));
            FURNITURE_DEFINITIONS.add(new FurnitureDefinition(slatsId, "slats", planksId,
                    new ResourceLocation(planksId.getNamespace(), "block/" + planksId.getPath()),
                    displayName(woodType) + " Slats"));
            if (EXCLUDED_NAMESPACES.contains(planksId.getNamespace())) return;
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
                case "slats" -> new SlatsItem(DYNAMIC_SLATS.get(definition.id().getPath()).get(),
                        new Item.Properties(), definition.name());
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
                if (definition.kind().equals("rack")
                        && definition.ingredientId().equals(new ResourceLocation("foragersinsight", "lilac_planks"))) {
                    value = new ResourceLocation(FarmersAssortment.MOD_ID,
                            value.endsWith("_top")
                                    ? "block/stripped_lilac_log_big_top"
                                    : "block/stripped_lilac_log_big").toString();
                } else {
                    value = inferLogTexture(definition.texture(), value).toString();
                }
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

    private void addSlatsClient(FurnitureDefinition definition) {
        String name = definition.id().getPath();
        String modelPrefix = FarmersAssortment.MOD_ID + ":block/" + name;
        put("models/block/" + name + ".json", model(definition, "slats", 0, false));
        put("models/block/" + name + "_side.json", model(definition, "slats_side", 16, false));
        put("models/block/" + name + "_vertical.json", model(definition, "slats", 0, true));
        put("models/block/" + name + "_embedded.json", model(definition, "slats_embedded", 0, false));
        JsonObject variants = new JsonObject();
        for (SlatsBlock.Mode mode : SlatsBlock.Mode.values()) {
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                JsonObject variant = new JsonObject();
                String suffix = switch (mode) {
                    case FLOOR -> "";
                    case SIDE, SUSPENDED -> "_side";
                    case VERTICAL -> "_vertical";
                    case EMBEDDED -> "_embedded";
                };
                variant.addProperty("model", modelPrefix + suffix);
                int rotation = switch (facing) {
                    case EAST -> 90;
                    case SOUTH -> 180;
                    case WEST -> 270;
                    default -> 0;
                };
                if (rotation != 0) {
                    variant.addProperty("y", rotation);
                }
                variants.add("mode=" + mode.getSerializedName() + ",facing=" + facing.getName(), variant);
            }
        }
        JsonObject blockstate = new JsonObject();
        blockstate.add("variants", variants);
        put("blockstates/" + name + ".json", blockstate);
        JsonObject item = new JsonObject();
        item.addProperty("parent", modelPrefix);
        put("models/item/" + name + ".json", item);
    }

    private JsonObject model(FurnitureDefinition definition, String template, int offset, boolean vertical) {
        JsonObject model = readTemplate(template);
        model.remove("format_version");
        model.remove("groups");
        model.remove("credit");
        JsonObject textures = new JsonObject();
        textures.addProperty("2", definition.texture().toString());
        textures.addProperty("particle", definition.texture().toString());
        textures.addProperty("missing", "minecraft:block/white_wool");
        model.add("textures", textures);
        model.addProperty("render_type", "minecraft:cutout");
        for (JsonElement value : model.getAsJsonArray("elements")) {
            JsonObject element = value.getAsJsonObject();
            element.remove("rotation");
            JsonArray from = element.getAsJsonArray("from");
            JsonArray to = element.getAsJsonArray("to");
            if (vertical) {
                element.add("from", coordinates(from.get(0).getAsDouble(), from.get(2).getAsDouble(),
                        16 - to.get(1).getAsDouble()));
                element.add("to", coordinates(to.get(0).getAsDouble(), to.get(2).getAsDouble(),
                        16 - from.get(1).getAsDouble()));
                JsonObject faces = new JsonObject();
                element.getAsJsonObject("faces").entrySet().forEach(entry -> {
                    String direction = switch (entry.getKey()) {
                        case "north" -> "down";
                        case "south" -> "up";
                        case "up" -> "north";
                        case "down" -> "south";
                        default -> entry.getKey();
                    };
                    JsonObject face = entry.getValue().getAsJsonObject().deepCopy();
                    int rotation = face.has("rotation") ? face.get("rotation").getAsInt() : 0;
                    int adjustment = switch (entry.getKey()) {
                        case "north", "up" -> 180;
                        case "east" -> 270;
                        case "west" -> 90;
                        default -> 0;
                    };
                    face.addProperty("rotation", (rotation + adjustment) % 360);
                    face.remove("cullface");
                    faces.add(direction, face);
                });
                element.add("faces", faces);
            } else if (offset != 0) {
                from.set(1, new JsonPrimitive(from.get(1).getAsDouble() + offset));
                to.set(1, new JsonPrimitive(to.get(1).getAsDouble() + offset));
            }
        }
        return model;
    }

    private static JsonArray coordinates(double x, double y, double z) {
        JsonArray result = new JsonArray();
        result.add(x);
        result.add(y);
        result.add(z);
        return result;
    }

    private static JsonObject readTemplate(String name) {
        String path = "/assets/" + FarmersAssortment.MOD_ID
                + "/models/block/template/slats/" + name + ".json";
        try (InputStream stream = FarmersAssortment.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Missing Slats template: " + path);
            }
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read Slats template: " + path, exception);
        }
    }

    private void addSlatsServer(FurnitureDefinition definition) {
        String name = definition.id().getPath();
        String id = definition.id().toString();
        String planks = definition.ingredientId().toString();
        put("loot_tables/blocks/" + name + ".json", json("""
                {"type":"minecraft:block","pools":[{
                  "rolls":1,"entries":[{"type":"minecraft:item","name":"%s"}],
                  "conditions":[{"condition":"minecraft:survives_explosion"}]
                }]}
                """.formatted(id)));
        if (!ForgeRegistries.ITEMS.containsKey(definition.ingredientId())) {
            return;
        }
        put("recipes/" + name + ".json", json("""
                {"type":"minecraft:crafting_shaped","category":"building",
                  "group":"farmersassortment_slats","pattern":["SSS","PPP"],
                  "key":{"S":{"item":"minecraft:stick"},"P":{"item":"%s"}},
                  "result":{"item":"%s","count":4}}
                """.formatted(planks, id)));
        put("advancements/recipes/decorations/" + name + ".json", json("""
                {"parent":"minecraft:recipes/root","criteria":{
                  "has_planks":{"trigger":"minecraft:inventory_changed",
                    "conditions":{"items":[{"items":["%s"]}]}},
                  "has_the_recipe":{"trigger":"minecraft:recipe_unlocked",
                    "conditions":{"recipe":"%s"}}},
                  "requirements":[["has_planks","has_the_recipe"]],
                  "rewards":{"recipes":["%s"]}}
                """.formatted(planks, id, id)));
    }

    private static JsonObject json(String value) {
        return JsonParser.parseString(value).getAsJsonObject();
    }

    private static JsonObject tag(JsonArray values) {
        JsonObject result = new JsonObject();
        result.addProperty("replace", false);
        result.add("values", values.deepCopy());
        return result;
    }

    private void put(String path, JsonElement value) {
        put(new ResourceLocation(FarmersAssortment.MOD_ID, path), value);
    }

    private void put(ResourceLocation id, JsonElement value) {
        resources.put(id, value.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void addTag(ResourceLocation id, JsonArray values) {
        put(id, tag(values));
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        if (!String.join("/", elements).equals("pack.mcmeta")) return null;
        int format = 15;
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

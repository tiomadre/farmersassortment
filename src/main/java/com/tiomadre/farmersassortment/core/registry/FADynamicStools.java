package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import com.tiomadre.farmersassortment.core.item.StoolItem;
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

public final class FADynamicStools {
    private static final Set<String> EXCLUDED_NAMESPACES = Set.of("minecraft", FarmersAssortment.MOD_ID);
    private static final Set<ResourceLocation> EXCLUDED_PLANKS = Set.of(
            new ResourceLocation("foragersinsight", "lilac_planks")
    );

    private static final Map<String, RegistryObject<StoolBlock>> DYNAMIC_STOOLS = new LinkedHashMap<>();
    private static final List<DynamicStoolDefinition> DYNAMIC_DEFINITIONS = new ArrayList<>();

    private FADynamicStools() {
    }

    public static Stream<RegistryObject<StoolBlock>> stools() {
        return DYNAMIC_STOOLS.values().stream();
    }

    public static List<DynamicStoolDefinition> stoolDefinitions() {
        return List.copyOf(DYNAMIC_DEFINITIONS);
    }

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.LOWEST, FADynamicStools::onRegister);
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
                .filter(FADynamicStools::isSupportedPlanks)
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        event.register(ForgeRegistries.Keys.BLOCKS, helper -> planks.forEach(planksId -> {
            String woodType = woodType(planksId);
            ResourceLocation slabId = new ResourceLocation(planksId.getNamespace(), woodType + "_slab");
            String stoolName = planksId.getNamespace() + "_" + woodType + "_stool";
            ResourceLocation stoolId = new ResourceLocation(FarmersAssortment.MOD_ID, stoolName);
            StoolBlock stoolBlock = new StoolBlock(BlockBehaviour.Properties.copy(resolveBlock(planksId)).noOcclusion());

            helper.register(stoolId, stoolBlock);
            RegistryObject<StoolBlock> stool = RegistryObject.create(stoolId, ForgeRegistries.BLOCKS);
            DYNAMIC_STOOLS.put(stoolName, stool);
            DYNAMIC_DEFINITIONS.add(new DynamicStoolDefinition(stool, planksId, slabId,
                    inferSeatTexture(planksId.getNamespace(), woodType)));
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
        event.register(ForgeRegistries.Keys.ITEMS, helper -> DYNAMIC_DEFINITIONS.forEach(definition -> {
            ResourceLocation stoolId = definition.block().getId();
            if (stoolId != null) {
                helper.register(stoolId, new StoolItem(definition.block().get(), new Item.Properties(),
                        displayName(woodType(definition.planksId())) + " Stool"));
            }
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

    public record DynamicStoolDefinition(RegistryObject<StoolBlock> block, ResourceLocation planksId,
                                         ResourceLocation slabId, ResourceLocation seatTexture) {
    }
}

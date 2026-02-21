package com.tiomadre.farmersassortment.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class FADynamicStools {
    private static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();
    private static final Set<String> EXCLUDED_NAMESPACES = Set.of("minecraft", FarmersAssortment.MOD_ID);
    private static final Set<ResourceLocation> EXCLUDED_PLANKS = Set.of(
            new ResourceLocation("crabbersdelight", "palm_planks"),
            new ResourceLocation("foragersinsight", "lilac_planks")
    );

    private static final Map<String, RegistryObject<StoolBlock>> DYNAMIC_STOOLS = new LinkedHashMap<>();
    private static final List<DynamicStoolDefinition> DYNAMIC_DEFINITIONS = new ArrayList<>();

    static {
        discoverAndRegister();
    }

    private FADynamicStools() {
    }

    public static Stream<RegistryObject<StoolBlock>> stools() {
        return DYNAMIC_STOOLS.values().stream();
    }

    public static List<DynamicStoolDefinition> stoolDefinitions() {
        return List.copyOf(DYNAMIC_DEFINITIONS);
    }

    private static void discoverAndRegister() {
        for (ResourceLocation id : ForgeRegistries.BLOCKS.getKeys()) {
            if (EXCLUDED_NAMESPACES.contains(id.getNamespace()) || EXCLUDED_PLANKS.contains(id)) {
                continue;
            }
            String path = id.getPath();
            if (!path.endsWith("_planks")) {
                continue;
            }

            String woodType = path.substring(0, path.length() - "_planks".length());
            ResourceLocation slabId = new ResourceLocation(id.getNamespace(), woodType + "_slab");
            if (!ForgeRegistries.BLOCKS.containsKey(slabId)) {
                continue;
            }

            String stoolName = id.getNamespace() + "_" + woodType + "_stool";
            if (DYNAMIC_STOOLS.containsKey(stoolName)) {
                continue;
            }

            RegistryObject<StoolBlock> stool = registerStool(stoolName, id);
            DYNAMIC_STOOLS.put(stoolName, stool);
            DYNAMIC_DEFINITIONS.add(new DynamicStoolDefinition(stool, id, slabId, inferSeatTexture(id.getNamespace(), woodType)));
        }
    }

    private static RegistryObject<StoolBlock> registerStool(String name, ResourceLocation planksId) {
        ResourceLocation stoolId = new ResourceLocation(FarmersAssortment.MOD_ID, name);
        return BLOCKS.createBlockWithItem(name,
                () -> new StoolBlock(BlockBehaviour.Properties.copy(resolveBlock(planksId)).noOcclusion()),
                () -> new StoolItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(stoolId)), new Item.Properties()));
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
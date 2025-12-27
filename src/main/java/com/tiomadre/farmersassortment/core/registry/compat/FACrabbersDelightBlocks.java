package com.tiomadre.farmersassortment.core.registry.compat;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.Objects;
import java.util.stream.Stream;

public final class FACrabbersDelightBlocks {
    private static final String MOD_ID = "crabbersdelight";
    private static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<CuttingBoardBlock> PALM_CUTTING_BOARD = registerCuttingBoard("palm");
    public static final RegistryObject<ButcherBlockCabinetBlock> PALM_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("palm");

    private FACrabbersDelightBlocks() {
    }

    public static void init() {
    }

    public static Stream<RegistryObject<CuttingBoardBlock>> cuttingBoards() {
        return Stream.of(PALM_CUTTING_BOARD);
    }

    public static Stream<RegistryObject<ButcherBlockCabinetBlock>> butcherBlockCabinets() {
        return Stream.of(PALM_BUTCHER_BLOCK_CABINET);
    }

    private static RegistryObject<CuttingBoardBlock> registerCuttingBoard(String woodType) {
        Block baseBlock = compatBlock(woodType + "_planks");
        return BLOCKS.createBlock(woodType + "_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.copy(baseBlock)),
                new Item.Properties());
    }

    private static RegistryObject<ButcherBlockCabinetBlock> registerButcherBlockCabinet(String woodType) {
        Block baseBlock = compatBlock(woodType + "_planks");
        SoundType soundType = baseBlock.defaultBlockState().getSoundType();
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.BARREL)
                .mapColor(baseBlock.defaultMapColor())
                .sound(soundType);
        return BLOCKS.createBlock(woodType + "_butcher_block_cabinet",
                () -> new ButcherBlockCabinetBlock(properties),
                new Item.Properties());
    }

    private static Block compatBlock(String path) {
        ResourceLocation id = new ResourceLocation(MOD_ID, path);
        return Objects.requireNonNullElse(ForgeRegistries.BLOCKS.getValue(id), Blocks.OAK_PLANKS);
    }
}
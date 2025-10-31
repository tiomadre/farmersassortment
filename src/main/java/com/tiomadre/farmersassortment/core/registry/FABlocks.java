package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.item.CookingPotItem;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;

import java.util.Objects;
import java.util.stream.Stream;

public final class FABlocks {
    public static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<CuttingBoardBlock> SPRUCE_CUTTING_BOARD = registerCuttingBoard("spruce", Blocks.SPRUCE_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> BIRCH_CUTTING_BOARD = registerCuttingBoard("birch", Blocks.BIRCH_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> JUNGLE_CUTTING_BOARD = registerCuttingBoard("jungle", Blocks.JUNGLE_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> ACACIA_CUTTING_BOARD = registerCuttingBoard("acacia", Blocks.ACACIA_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> DARK_OAK_CUTTING_BOARD = registerCuttingBoard("dark_oak", Blocks.DARK_OAK_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> MANGROVE_CUTTING_BOARD = registerCuttingBoard("mangrove", Blocks.MANGROVE_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> CHERRY_CUTTING_BOARD = registerCuttingBoard("cherry", Blocks.CHERRY_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> BAMBOO_CUTTING_BOARD = registerCuttingBoard("bamboo", Blocks.BAMBOO_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> CRIMSON_CUTTING_BOARD = registerCuttingBoard("crimson", Blocks.CRIMSON_PLANKS);
    public static final RegistryObject<CuttingBoardBlock> WARPED_CUTTING_BOARD = registerCuttingBoard("warped", Blocks.WARPED_PLANKS);
    private static final ResourceLocation COPPER_COOKING_POT_ID = new ResourceLocation(FarmersAssortment.MOD_ID, "copper_cooking_pot");

    public static final RegistryObject<CookingPotBlock> COPPER_COOKING_POT = BLOCKS.createBlockWithItem("copper_cooking_pot",
            () -> new CookingPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)),
            () -> new CookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(COPPER_COOKING_POT_ID)), new Item.Properties().stacksTo(1)));

    public static Stream<RegistryObject<CuttingBoardBlock>> cuttingBoards() {
        return Stream.of(
                SPRUCE_CUTTING_BOARD,
                BIRCH_CUTTING_BOARD,
                JUNGLE_CUTTING_BOARD,
                ACACIA_CUTTING_BOARD,
                DARK_OAK_CUTTING_BOARD,
                MANGROVE_CUTTING_BOARD,
                CHERRY_CUTTING_BOARD,
                BAMBOO_CUTTING_BOARD,
                CRIMSON_CUTTING_BOARD,
                WARPED_CUTTING_BOARD
        );
    }

    private static RegistryObject<CuttingBoardBlock> registerCuttingBoard(String woodType, Block baseBlock) {
        return BLOCKS.createBlock(woodType + "_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.copy(baseBlock)),
                new Item.Properties());
    }
    public static void init() {
    }
}
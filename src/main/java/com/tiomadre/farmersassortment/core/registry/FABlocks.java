package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.FarmersAssortment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.function.Supplier;

public final class FABlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersAssortment.MOD_ID);

    public static final RegistryObject<CuttingBoardBlock> OAK_CUTTING_BOARD = registerCuttingBoard("oak", Blocks.OAK_PLANKS);
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

    private FABlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static RegistryObject<CuttingBoardBlock> registerCuttingBoard(String woodType, Block baseBlock) {
        return registerBlock(woodType + "_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock)));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        FAItems.registerBlockItem(name, block);
        return block;
    }
}

package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.tiomadre.farmersassortment.core.block.TerracottaCookingPotBlock;
import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.item.CookingPotItem;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class FABlocks {
    public static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<ButcherBlockCabinetBlock> OAK_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("oak", Blocks.OAK_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> SPRUCE_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("spruce", Blocks.SPRUCE_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> BIRCH_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("birch", Blocks.BIRCH_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> JUNGLE_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("jungle", Blocks.JUNGLE_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> ACACIA_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("acacia", Blocks.ACACIA_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> DARK_OAK_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("dark_oak", Blocks.DARK_OAK_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> MANGROVE_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("mangrove", Blocks.MANGROVE_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> CHERRY_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("cherry", Blocks.CHERRY_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> BAMBOO_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("bamboo", Blocks.BAMBOO_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> CRIMSON_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("crimson", Blocks.CRIMSON_PLANKS);
    public static final RegistryObject<ButcherBlockCabinetBlock> WARPED_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("warped", Blocks.WARPED_PLANKS);

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
    private static final ResourceLocation GOLDEN_COOKING_POT_ID = new ResourceLocation(FarmersAssortment.MOD_ID, "golden_cooking_pot");
    private static final ResourceLocation ALABASTER_COOKING_POT_ID = new ResourceLocation(FarmersAssortment.MOD_ID, "alabaster_cooking_pot");
    private static final ResourceLocation TERRACOTTA_COOKING_POT_ID = new ResourceLocation(FarmersAssortment.MOD_ID, "terracotta_cooking_pot");

    public static final RegistryObject<CookingPotBlock> COPPER_COOKING_POT = registerCookingPot("copper_cooking_pot", MapColor.METAL, COPPER_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> GOLDEN_COOKING_POT = registerCookingPot("golden_cooking_pot", MapColor.GOLD, GOLDEN_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> ALABASTER_COOKING_POT = registerCookingPot("alabaster_cooking_pot", MapColor.TERRACOTTA_WHITE, ALABASTER_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> TERRACOTTA_COOKING_POT = registerTerracottaCookingPot();
    public static final RegistryObject<StoveBlock> ALABASTER_STOVE = BLOCKS.createBlock("alabaster_stove",
            () -> new StoveBlock(BlockBehaviour.Properties.copy(ModBlocks.STOVE.get())),
            new Item.Properties());


    public static Stream<RegistryObject<CuttingBoardBlock>> cuttingBoards() {
        return Stream.of(SPRUCE_CUTTING_BOARD, BIRCH_CUTTING_BOARD, JUNGLE_CUTTING_BOARD, ACACIA_CUTTING_BOARD, DARK_OAK_CUTTING_BOARD, MANGROVE_CUTTING_BOARD,
                CHERRY_CUTTING_BOARD, BAMBOO_CUTTING_BOARD, CRIMSON_CUTTING_BOARD, WARPED_CUTTING_BOARD);
    }

    private static RegistryObject<CuttingBoardBlock> registerCuttingBoard(String woodType, Block baseBlock) {
        return BLOCKS.createBlock(woodType + "_cutting_board", () -> new CuttingBoardBlock(BlockBehaviour.Properties.copy(baseBlock)),
                new Item.Properties());
    }
    public static Stream<RegistryObject<ButcherBlockCabinetBlock>> butcherBlockCabinets() {
        return Stream.of(
                OAK_BUTCHER_BLOCK_CABINET,
                SPRUCE_BUTCHER_BLOCK_CABINET,
                BIRCH_BUTCHER_BLOCK_CABINET,
                JUNGLE_BUTCHER_BLOCK_CABINET,
                ACACIA_BUTCHER_BLOCK_CABINET,
                DARK_OAK_BUTCHER_BLOCK_CABINET,
                MANGROVE_BUTCHER_BLOCK_CABINET,
                CHERRY_BUTCHER_BLOCK_CABINET,
                BAMBOO_BUTCHER_BLOCK_CABINET,
                CRIMSON_BUTCHER_BLOCK_CABINET,
                WARPED_BUTCHER_BLOCK_CABINET
        );
    }
    private static RegistryObject<ButcherBlockCabinetBlock> registerButcherBlockCabinet(String woodType, Block baseBlock) {
        SoundType soundType = baseBlock.defaultBlockState().getSoundType();
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.BARREL)
                .mapColor(baseBlock.defaultMapColor())
                .sound(soundType);
        return BLOCKS.createBlock(woodType + "_butcher_block_cabinet",
                () -> new ButcherBlockCabinetBlock(properties),
                new Item.Properties());
    }
    public static void init() {
    }

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityTypeAccessor cuttingBoardAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.CUTTING_BOARD.get();
            Set<Block> cuttingBoardValidBlocks = cuttingBoardAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedCuttingBoardBlocks = new HashSet<>(cuttingBoardValidBlocks);
            cuttingBoards().map(RegistryObject::get).forEach(updatedCuttingBoardBlocks::add);
            cuttingBoardAccessor.farmersassortment$setValidBlocks(updatedCuttingBoardBlocks);
            butcherBlockCabinets().map(RegistryObject::get).forEach(updatedCuttingBoardBlocks::add);

            BlockEntityTypeAccessor cookingPotAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.COOKING_POT.get();
            Set<Block> cookingPotValidBlocks = cookingPotAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedCookingPotBlocks = new HashSet<>(cookingPotValidBlocks);
            boolean changed = false;

            {
                Block block = COPPER_COOKING_POT.get();
                if (!updatedCookingPotBlocks.contains(block)) {
                    updatedCookingPotBlocks.add(block);
                    changed = true;
                }
            }
            {
                Block block = GOLDEN_COOKING_POT.get();
                if (!updatedCookingPotBlocks.contains(block)) {
                    updatedCookingPotBlocks.add(block);
                    changed = true;
                }
            }
            {
                Block block = TERRACOTTA_COOKING_POT.get();
                if (!updatedCookingPotBlocks.contains(block)) {
                    updatedCookingPotBlocks.add(block);
                    changed = true;
                }
            }
            {
                Block block = ALABASTER_COOKING_POT.get();
                if (!updatedCookingPotBlocks.contains(block)) {
                    updatedCookingPotBlocks.add(block);
                    changed = true;
                }
            }

            if (changed) {
                cookingPotAccessor.farmersassortment$setValidBlocks(updatedCookingPotBlocks);
            }
        });
    }

    private static RegistryObject<CookingPotBlock> registerCookingPot(String name, MapColor mapColor, ResourceLocation id) {
        return BLOCKS.createBlockWithItem(name,
                () -> new CookingPotBlock(BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.5F, 6.0F).sound(SoundType.LANTERN)),
                () -> new CookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(id)), new Item.Properties().stacksTo(1)));
    }

    private static RegistryObject<CookingPotBlock> registerTerracottaCookingPot() {
        return BLOCKS.createBlockWithItem("terracotta_cooking_pot",
                () -> new TerracottaCookingPotBlock(BlockBehaviour.Properties.of().mapColor(Blocks.TERRACOTTA.defaultMapColor()).strength(0.5F, 6.0F).sound(SoundType.DECORATED_POT)),
                () -> new TerracottaCookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(TERRACOTTA_COOKING_POT_ID)), new Item.Properties().stacksTo(1)));
    }
}
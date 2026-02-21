package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.*;
import com.tiomadre.farmersassortment.core.item.StoolItem;
import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
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
import vectorwing.farmersdelight.common.item.CookingPotItem;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;


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

    public static final RegistryObject<FloatingCounterBlock> OAK_FLOATING_COUNTER = registerFloatingCounter("oak", Blocks.OAK_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> SPRUCE_FLOATING_COUNTER = registerFloatingCounter("spruce", Blocks.SPRUCE_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> BIRCH_FLOATING_COUNTER = registerFloatingCounter("birch", Blocks.BIRCH_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> JUNGLE_FLOATING_COUNTER = registerFloatingCounter("jungle", Blocks.JUNGLE_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> ACACIA_FLOATING_COUNTER = registerFloatingCounter("acacia", Blocks.ACACIA_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> DARK_OAK_FLOATING_COUNTER = registerFloatingCounter("dark_oak", Blocks.DARK_OAK_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> MANGROVE_FLOATING_COUNTER = registerFloatingCounter("mangrove", Blocks.MANGROVE_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> CHERRY_FLOATING_COUNTER = registerFloatingCounter("cherry", Blocks.CHERRY_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> BAMBOO_FLOATING_COUNTER = registerFloatingCounter("bamboo", Blocks.BAMBOO_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> CRIMSON_FLOATING_COUNTER = registerFloatingCounter("crimson", Blocks.CRIMSON_PLANKS);
    public static final RegistryObject<FloatingCounterBlock> WARPED_FLOATING_COUNTER = registerFloatingCounter("warped", Blocks.WARPED_PLANKS);


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

    public static final RegistryObject<CookingPotBlock> COPPER_COOKING_POT = registerCookingPot("copper_cooking_pot", MapColor.METAL, SoundType.COPPER, COPPER_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> GOLDEN_COOKING_POT = registerCookingPot("golden_cooking_pot", MapColor.GOLD, SoundType.LANTERN, GOLDEN_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> ALABASTER_COOKING_POT = registerCookingPot("alabaster_cooking_pot", MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, ALABASTER_COOKING_POT_ID);
    public static final RegistryObject<CookingPotBlock> TERRACOTTA_COOKING_POT = registerTerracottaCookingPot();

    public static final RegistryObject<UniqueStoveBlock> ALABASTER_STOVE = BLOCKS.createBlock("alabaster_stove", () -> new UniqueStoveBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)), new Item.Properties());
    public static final RegistryObject<StoolBlock> OAK_STOOL = registerStool("oak", Blocks.OAK_PLANKS);
    public static final RegistryObject<StoolBlock> SPRUCE_STOOL = registerStool("spruce", Blocks.SPRUCE_PLANKS);
    public static final RegistryObject<StoolBlock> BIRCH_STOOL = registerStool("birch", Blocks.BIRCH_PLANKS);
    public static final RegistryObject<StoolBlock> JUNGLE_STOOL = registerStool("jungle", Blocks.JUNGLE_PLANKS);
    public static final RegistryObject<StoolBlock> ACACIA_STOOL = registerStool("acacia", Blocks.ACACIA_PLANKS);
    public static final RegistryObject<StoolBlock> DARK_OAK_STOOL = registerStool("dark_oak", Blocks.DARK_OAK_PLANKS);
    public static final RegistryObject<StoolBlock> MANGROVE_STOOL = registerStool("mangrove", Blocks.MANGROVE_PLANKS);
    public static final RegistryObject<StoolBlock> CHERRY_STOOL = registerStool("cherry", Blocks.CHERRY_PLANKS);
    public static final RegistryObject<StoolBlock> BAMBOO_STOOL = registerStool("bamboo", Blocks.BAMBOO_PLANKS);
    public static final RegistryObject<StoolBlock> CRIMSON_STOOL = registerStool("crimson", Blocks.CRIMSON_PLANKS);
    public static final RegistryObject<StoolBlock> WARPED_STOOL = registerStool("warped", Blocks.WARPED_PLANKS);

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

    public static Stream<RegistryObject<ButcherBlockCabinetBlock>> allButcherBlockCabinets() {
        Stream<RegistryObject<ButcherBlockCabinetBlock>> cabinets = butcherBlockCabinets();
        if (FarmersAssortment.isCrabbersCompatEnabled()) {
            cabinets = Stream.concat(cabinets, FAxCrabbersBlocks.butcherBlockCabinets());
        }
        if (FarmersAssortment.isForagersCompatEnabled()) {
            cabinets = Stream.concat(cabinets, FAxForagersBlocks.butcherBlockCabinets());
        }
        return cabinets;
    }
    public static Stream<RegistryObject<CuttingBoardBlock>> allCuttingBoards() {
        Stream<RegistryObject<CuttingBoardBlock>> boards = cuttingBoards();
        if (FarmersAssortment.isCrabbersCompatEnabled()) {
            boards = Stream.concat(boards, FAxCrabbersBlocks.cuttingBoards());
        }
        if (FarmersAssortment.isForagersCompatEnabled()) {
            boards = Stream.concat(boards, FAxForagersBlocks.cuttingBoards());
        }
        return boards;
    }
    public static Stream<RegistryObject<CookingPotBlock>> cookingPots() {
        return Stream.of(COPPER_COOKING_POT, GOLDEN_COOKING_POT, ALABASTER_COOKING_POT, TERRACOTTA_COOKING_POT);
    }

    public static Stream<RegistryObject<CookingPotBlock>> allCookingPots() {
        if (!FarmersAssortment.isCrabbersCompatEnabled()) {
            return cookingPots();
        }
        return Stream.concat(cookingPots(), FAxCrabbersBlocks.cookingPots());
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
            allCuttingBoards().map(RegistryObject::get).forEach(updatedCuttingBoardBlocks::add);
            allButcherBlockCabinets().map(RegistryObject::get).forEach(updatedCuttingBoardBlocks::add);
            cuttingBoardAccessor.farmersassortment$setValidBlocks(updatedCuttingBoardBlocks);

            BlockEntityTypeAccessor cookingPotAccessor = (BlockEntityTypeAccessor) ModBlockEntityTypes.COOKING_POT.get();
            Set<Block> cookingPotValidBlocks = cookingPotAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedCookingPotBlocks = new HashSet<>(cookingPotValidBlocks);
            boolean changed = false;

            for (RegistryObject<CookingPotBlock> cookingPot : allCookingPots().toList()) {
                Block block = cookingPot.get();
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

    private static RegistryObject<CookingPotBlock> registerCookingPot(String name, MapColor mapColor, SoundType soundType, ResourceLocation id) {
        return BLOCKS.createBlockWithItem(name,
                () -> new CookingPotBlock(BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.5F, 6.0F).sound(soundType)),
                () -> new CookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(id)), new Item.Properties().stacksTo(1)));
    }


    private static RegistryObject<CookingPotBlock> registerTerracottaCookingPot() {
        return BLOCKS.createBlockWithItem("terracotta_cooking_pot",
                () -> new TerracottaCookingPotBlock(BlockBehaviour.Properties.of().mapColor(Blocks.TERRACOTTA.defaultMapColor()).strength(0.5F, 6.0F).sound(SoundType.DECORATED_POT)),
                () -> new TerracottaCookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(TERRACOTTA_COOKING_POT_ID)), new Item.Properties().stacksTo(1)));
    }

    private static RegistryObject<FloatingCounterBlock> registerFloatingCounter(String woodType, Block baseBlock) {
        return BLOCKS.createBlock(woodType + "_floating_counter",
                () -> new FloatingCounterBlock(BlockBehaviour.Properties.copy(baseBlock)),
                new Item.Properties());
    }

    public static Stream<RegistryObject<FloatingCounterBlock>> floatingCounters() {
        return Stream.of(
                OAK_FLOATING_COUNTER,
                SPRUCE_FLOATING_COUNTER,
                BIRCH_FLOATING_COUNTER,
                JUNGLE_FLOATING_COUNTER,
                ACACIA_FLOATING_COUNTER,
                DARK_OAK_FLOATING_COUNTER,
                MANGROVE_FLOATING_COUNTER,
                CHERRY_FLOATING_COUNTER,
                BAMBOO_FLOATING_COUNTER,
                CRIMSON_FLOATING_COUNTER,
                WARPED_FLOATING_COUNTER
        );
    }

    private static RegistryObject<StoolBlock> registerStool(String woodType, Block baseBlock) {
        String name = woodType + "_stool";
        ResourceLocation id = new ResourceLocation(FarmersAssortment.MOD_ID, name);
        return BLOCKS.createBlockWithItem(name,
                () -> new StoolBlock(BlockBehaviour.Properties.copy(baseBlock).noOcclusion()),
                () -> new StoolItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(id)), new Item.Properties()));
    }
    public static Stream<RegistryObject<StoolBlock>> allStools() {
        Stream<RegistryObject<StoolBlock>> stools = stools();
        if (FarmersAssortment.isCrabbersCompatEnabled()) {
            stools = Stream.concat(stools, FAxCrabbersBlocks.stools());
        }
        if (FarmersAssortment.isForagersCompatEnabled()) {
            stools = Stream.concat(stools, FAxForagersBlocks.stools());
        }
        stools = Stream.concat(stools, FADynamicStools.stools());
        return stools;
    }


    public static Stream<RegistryObject<StoolBlock>> stools() {
        return Stream.of(
                OAK_STOOL,
                SPRUCE_STOOL,
                BIRCH_STOOL,
                JUNGLE_STOOL,
                ACACIA_STOOL,
                DARK_OAK_STOOL,
                MANGROVE_STOOL,
                CHERRY_STOOL,
                BAMBOO_STOOL,
                CRIMSON_STOOL,
                WARPED_STOOL
        );
    }
}
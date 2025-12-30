package com.tiomadre.farmersassortment.core.registry.compat;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import alabaster.crabbersdelight.common.block.CrabTrapBlock;
import alabaster.crabbersdelight.common.registry.CDModBlockEntity;
import alabaster.crabbersdelight.common.registry.CDModBlocks;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
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
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.item.CookingPotItem;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;


public final class FAxCrabbersBlocks {
    private static final String MOD_ID = "crabbersdelight";
    private static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();

    private static final ResourceLocation PEARLESCENT_COOKING_POT_ID = new ResourceLocation(FarmersAssortment.MOD_ID, "pearlescent_cooking_pot");
    public static final RegistryObject<CookingPotBlock> PEARLESCENT_COOKING_POT = registerCookingPot();
    public static final RegistryObject<CuttingBoardBlock> PALM_CUTTING_BOARD = registerCuttingBoard("palm");
    public static final RegistryObject<ButcherBlockCabinetBlock> PALM_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("palm");
    public static final RegistryObject<CrabTrapBlock> SPRUCE_CRAB_TRAP = registerCrabTrap("spruce");
    public static final RegistryObject<CrabTrapBlock> BIRCH_CRAB_TRAP = registerCrabTrap("birch");
    public static final RegistryObject<CrabTrapBlock> JUNGLE_CRAB_TRAP = registerCrabTrap("jungle");
    public static final RegistryObject<CrabTrapBlock> ACACIA_CRAB_TRAP = registerCrabTrap("acacia");
    public static final RegistryObject<CrabTrapBlock> DARK_OAK_CRAB_TRAP = registerCrabTrap("dark_oak");
    public static final RegistryObject<CrabTrapBlock> MANGROVE_CRAB_TRAP = registerCrabTrap("mangrove");
    public static final RegistryObject<CrabTrapBlock> CHERRY_CRAB_TRAP = registerCrabTrap("cherry");
    public static final RegistryObject<CrabTrapBlock> BAMBOO_CRAB_TRAP = registerCrabTrap("bamboo");
    public static final RegistryObject<CrabTrapBlock> CRIMSON_CRAB_TRAP = registerCrabTrap("crimson");
    public static final RegistryObject<CrabTrapBlock> WARPED_CRAB_TRAP = registerCrabTrap("warped");
    public static final RegistryObject<CrabTrapBlock> PALM_CRAB_TRAP = registerCrabTrap("palm");

    private FAxCrabbersBlocks() {
    }

    public static void init() {
    }

    public static Stream<RegistryObject<CuttingBoardBlock>> cuttingBoards() {
        return Stream.of(PALM_CUTTING_BOARD);
    }

    public static Stream<RegistryObject<ButcherBlockCabinetBlock>> butcherBlockCabinets() {
        return Stream.of(PALM_BUTCHER_BLOCK_CABINET);
    }
    public static Stream<RegistryObject<CookingPotBlock>> cookingPots() {
        return Stream.of(PEARLESCENT_COOKING_POT);
    }

    public static Stream<RegistryObject<CrabTrapBlock>> crabTraps() {
        return Stream.of(
                SPRUCE_CRAB_TRAP,
                BIRCH_CRAB_TRAP,
                JUNGLE_CRAB_TRAP,
                ACACIA_CRAB_TRAP,
                DARK_OAK_CRAB_TRAP,
                MANGROVE_CRAB_TRAP,
                CHERRY_CRAB_TRAP,
                BAMBOO_CRAB_TRAP,
                CRIMSON_CRAB_TRAP,
                WARPED_CRAB_TRAP,
                PALM_CRAB_TRAP
        );
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
    private static RegistryObject<CookingPotBlock> registerCookingPot() {
        return BLOCKS.createBlockWithItem("pearlescent_cooking_pot",
                () -> new CookingPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(0.5F, 6.0F).sound(SoundType.AMETHYST)),
                () -> new CookingPotItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(PEARLESCENT_COOKING_POT_ID)), new Item.Properties().stacksTo(1)));
    }


    private static RegistryObject<CrabTrapBlock> registerCrabTrap(String woodType) {
        return BLOCKS.createBlock(woodType + "_crab_trap",
                () -> new CrabTrapBlock(BlockBehaviour.Properties.copy(Objects.requireNonNull(CDModBlocks.CRAB_TRAP.get()))),
                new Item.Properties());
    }

    private static Block compatBlock(String path) {
        ResourceLocation id = new ResourceLocation(MOD_ID, path);
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        return block == null || block.equals(Blocks.AIR) ? Blocks.OAK_PLANKS : block;
    }

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityTypeAccessor crabTrapAccessor = (BlockEntityTypeAccessor) CDModBlockEntity.CRAB_TRAP.get();
            Set<Block> validBlocks = crabTrapAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedValidBlocks = new HashSet<>(validBlocks);

            boolean changed = addBlocksToSet(updatedValidBlocks, List.of(
                    CDModBlocks.CRAB_TRAP.get(),
                    SPRUCE_CRAB_TRAP.get(),
                    BIRCH_CRAB_TRAP.get(),
                    JUNGLE_CRAB_TRAP.get(),
                    ACACIA_CRAB_TRAP.get(),
                    DARK_OAK_CRAB_TRAP.get(),
                    MANGROVE_CRAB_TRAP.get(),
                    CHERRY_CRAB_TRAP.get(),
                    BAMBOO_CRAB_TRAP.get(),
                    CRIMSON_CRAB_TRAP.get(),
                    WARPED_CRAB_TRAP.get(),
                    PALM_CRAB_TRAP.get()
            ));

            if (changed) {
                crabTrapAccessor.farmersassortment$setValidBlocks(updatedValidBlocks);
            }
        });
    }

    private static boolean addBlocksToSet(Set<Block> blocks, List<Block> newBlocks) {
        boolean changed = false;
        for (Block block : newBlocks) {
            if (!blocks.contains(block)) {
                blocks.add(block);
                changed = true;
            }
        }
        return changed;
    }
}
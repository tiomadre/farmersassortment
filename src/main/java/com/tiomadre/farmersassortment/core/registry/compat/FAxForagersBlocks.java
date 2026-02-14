package com.tiomadre.farmersassortment.core.registry.compat;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.tiomadre.farmersassortment.core.mixin.BlockEntityTypeAccessor;
import com.tiomadre.foragersinsight.common.block.DiffuserBlock;
import com.tiomadre.foragersinsight.common.item.DiffuserBlockItem;
import com.tiomadre.foragersinsight.core.registry.FIBlockEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class FAxForagersBlocks {
    private static final String MOD_ID = "foragersinsight";
    private static final BlockSubRegistryHelper BLOCKS = FarmersAssortment.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<CuttingBoardBlock> LILAC_CUTTING_BOARD = registerCuttingBoard("lilac");
    public static final RegistryObject<ButcherBlockCabinetBlock> LILAC_BUTCHER_BLOCK_CABINET = registerButcherBlockCabinet("lilac");
    public static final RegistryObject<DiffuserBlock> AMETHYST_DIFFUSER = registerDiffuser();

    private FAxForagersBlocks() {
    }

    public static void init() {
    }

    public static Stream<RegistryObject<CuttingBoardBlock>> cuttingBoards() {
        return Stream.of(LILAC_CUTTING_BOARD);
    }

    public static Stream<RegistryObject<ButcherBlockCabinetBlock>> butcherBlockCabinets() {
        return Stream.of(LILAC_BUTCHER_BLOCK_CABINET);
    }

    public static Stream<RegistryObject<DiffuserBlock>> diffusers() {
        return Stream.of(AMETHYST_DIFFUSER);
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

    private static RegistryObject<DiffuserBlock> registerDiffuser() {
        return BLOCKS.createBlockWithItem("amethyst_diffuser",
                () -> new DiffuserBlock(BlockBehaviour.Properties.copy(compatBlock("diffuser")).sound(SoundType.AMETHYST)),
                () -> new DiffuserBlockItem(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(FarmersAssortment.MOD_ID, "amethyst_diffuser"))), new Item.Properties()));
    }

    private static Block compatBlock(String path) {
        ResourceLocation id = new ResourceLocation(MOD_ID, path);
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        return block == null || block.equals(Blocks.AIR) ? Blocks.OAK_PLANKS : block;
    }

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityTypeAccessor diffuserAccessor = (BlockEntityTypeAccessor) FIBlockEntityTypes.DIFFUSER.get();
            Set<Block> diffuserValidBlocks = diffuserAccessor.farmersassortment$getValidBlocks();
            Set<Block> updatedDiffusers = new HashSet<>(diffuserValidBlocks);

            boolean changed = addBlocksToSet(updatedDiffusers, List.of(AMETHYST_DIFFUSER.get()));
            if (changed) {
                diffuserAccessor.farmersassortment$setValidBlocks(updatedDiffusers);
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
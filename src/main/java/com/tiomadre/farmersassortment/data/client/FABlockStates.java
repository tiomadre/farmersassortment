package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import java.util.Objects;

public class FABlockStates extends BlockStateProvider {
    public FABlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersAssortment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerButcherBlockCabinet(FABlocks.OAK_BUTCHER_BLOCK_CABINET, "oak", "minecraft:block/oak_planks", modLoc("block/oak_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET, "spruce", "minecraft:block/spruce_planks", modLoc("block/spruce_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.BIRCH_BUTCHER_BLOCK_CABINET, "birch", "minecraft:block/birch_planks", modLoc("block/birch_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET, "jungle", "minecraft:block/jungle_planks", modLoc("block/jungle_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.ACACIA_BUTCHER_BLOCK_CABINET, "acacia", "minecraft:block/acacia_planks", modLoc("block/acacia_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET, "dark_oak", "minecraft:block/dark_oak_planks", modLoc("block/dark_oak_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET, "mangrove", "minecraft:block/mangrove_planks", modLoc("block/mangrove_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.CHERRY_BUTCHER_BLOCK_CABINET, "cherry", "minecraft:block/cherry_planks", modLoc("block/cherry_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET, "bamboo", "minecraft:block/bamboo_planks", modLoc("block/bamboo_butcher_block_cabinet_top"));
        registerButcherBlockCabinet(FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET, "crimson", "minecraft:block/crimson_planks", modLoc("block/crimson_butcher_block_cabinet_front_top"));
        registerButcherBlockCabinet(FABlocks.WARPED_BUTCHER_BLOCK_CABINET, "warped", "minecraft:block/warped_planks", modLoc("block/warped_butcher_block_cabinet_top"));
        registerSolidCuttingBoard(FABlocks.SPRUCE_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.BIRCH_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.JUNGLE_CUTTING_BOARD);
        registerSolidCuttingBoard(FABlocks.ACACIA_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.DARK_OAK_CUTTING_BOARD);
        registerSolidCuttingBoard(FABlocks.MANGROVE_CUTTING_BOARD);
        registerSolidCuttingBoard(FABlocks.CHERRY_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.BAMBOO_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.CRIMSON_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.WARPED_CUTTING_BOARD);
        registerCopperCookingPot();
    }

    private void registerCuttingBoard(RegistryObject<CuttingBoardBlock> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        ModelFile model = models()
                .getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("farmersdelight:block/cutting_board"))
                .texture("particle", modLoc("block/" + name))
                .texture("top", modLoc("block/" + name))
                .renderType("minecraft:cutout");
        FABlockStateHelper.horizontalFacingBlock(this, block.get(), model);
    }

    private void registerButcherBlockCabinet(RegistryObject<? extends Block> block, String woodType, String bottomTexture, ResourceLocation topTexture) {
        String name = block.getId().getPath();
        ModelFile closed = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/orientable_with_bottom"))
                .texture("front", modLoc("block/" + woodType + "_butcher_block_cabinet_front"))
                .texture("side", modLoc("block/" + woodType + "_butcher_block_cabinet_side"))
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
        ModelFile open = models().getBuilder(name + "_open")
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/orientable_with_bottom"))
                .texture("front", modLoc("block/" + woodType + "_butcher_block_cabinet_front_open"))
                .texture("side", modLoc("block/" + woodType + "_butcher_block_cabinet_side"))
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(ButcherBlockCabinetBlock.FACING);
            boolean openState = state.getValue(ButcherBlockCabinetBlock.OPEN);
            ModelFile model = openState ? open : closed;
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) direction.toYRot()) % 360)
                    .build();
        });
    }

    private void registerSolidCuttingBoard(RegistryObject<CuttingBoardBlock> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        ModelFile model = models()
                .getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("farmersdelight:block/cutting_board"))
                .texture("particle", modLoc("block/" + name))
                .texture("top", modLoc("block/" + name));
        FABlockStateHelper.horizontalFacingBlock(this, block.get(), model);
    }

    private void registerCopperCookingPot() {
        ModelFile pot = models().getExistingFile(modLoc("block/copper_cooking_pot"));
        ModelFile tray = models().getExistingFile(modLoc("block/copper_cooking_pot_tray"));
        ModelFile handle = models().getExistingFile(modLoc("block/copper_cooking_pot_handle"));

        getVariantBuilder(FABlocks.COPPER_COOKING_POT.get()).forAllStates(state -> {
            Direction direction = state.getValue(CookingPotBlock.FACING);
            CookingPotSupport support = state.getValue(CookingPotBlock.SUPPORT);
            ModelFile model = switch (support) {
                case TRAY -> tray;
                case HANDLE -> handle;
                default -> pot;
            };
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) direction.toYRot()) % 360)
                    .build();
        });
    }
}
package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
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
        registerCuttingBoard(FABlocks.SPRUCE_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.BIRCH_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.JUNGLE_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.ACACIA_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.DARK_OAK_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.MANGROVE_CUTTING_BOARD);
        registerCuttingBoard(FABlocks.CHERRY_CUTTING_BOARD);
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

package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

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
    }

    private void registerCuttingBoard(RegistryObject<CuttingBoardBlock> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        ModelFile model = models().getExistingFile(modLoc("block/" + name));
        FABlockStateHelper.horizontalFacingBlock(this, block.get(), model);
    }
}

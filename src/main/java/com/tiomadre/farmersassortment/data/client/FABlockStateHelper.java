package com.tiomadre.farmersassortment.data.client;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public final class FABlockStateHelper {
    private FABlockStateHelper() {
    }

    public static void horizontalFacingBlock(BlockStateProvider provider, Block block, ModelFile model) {
        provider.getVariantBuilder(block).forAllStates(state -> buildHorizontalFacingModel(state, model));
    }

    private static ConfiguredModel[] buildHorizontalFacingModel(BlockState state, ModelFile model) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int rotation = (int) direction.toYRot();
        return ConfiguredModel.builder().modelFile(model).rotationY((rotation + 360) % 360).build();
    }
}

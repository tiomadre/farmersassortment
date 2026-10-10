package com.tiomadre.farmersassortment.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiomadre.farmersassortment.core.block.entity.FloatingDrawerBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class FloatingDrawerRenderer implements BlockEntityRenderer<FloatingDrawerBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public FloatingDrawerRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(FloatingDrawerBlockEntity entity, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {
        BlockState slab = entity.getSlabState();
        Level level = entity.getLevel();

        if (slab.isAir() || level == null) {
            return;
        }

        BlockPos pos = entity.getBlockPos();
        BakedModel model = blockRenderer.getBlockModel(slab);
        ModelData modelData = model.getModelData(level, pos, slab, ModelData.EMPTY);
        RandomSource random = RandomSource.create(slab.getSeed(pos));

        for (RenderType renderType : model.getRenderTypes(slab, random, modelData)) {
            random.setSeed(slab.getSeed(pos));
            blockRenderer.renderBatched(
                    slab, pos, level, poseStack,
                    buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false)),
                    false, random, modelData, renderType);
        }
    }
}

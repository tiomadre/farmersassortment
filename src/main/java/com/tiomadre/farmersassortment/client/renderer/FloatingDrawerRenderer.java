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
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
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
        BlockAndTintGetter renderView = new SlabRenderView(level, pos, slab);
        BakedModel model = blockRenderer.getBlockModel(slab);
        ModelData modelData = model.getModelData(renderView, pos, slab, ModelData.EMPTY);
        long seed = slab.getSeed(pos);
        RandomSource random = RandomSource.create(seed);

        for (RenderType renderType : model.getRenderTypes(slab, random, modelData)) {
            random.setSeed(seed);
            blockRenderer.renderBatched(
                    slab, pos, renderView, poseStack,
                    buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)),
                    false, random, modelData, renderType);
        }
    }

    private static final class SlabRenderView implements BlockAndTintGetter {
        private final Level level;
        private final BlockPos slabPos;
        private final BlockState slab;

        private SlabRenderView(Level level, BlockPos slabPos, BlockState slab) {
            this.level = level;
            this.slabPos = slabPos.immutable();
            this.slab = slab;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return slabPos.equals(pos) ? slab : level.getBlockState(pos);
        }

        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return slabPos.equals(pos) ? null : level.getBlockEntity(pos);
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return slabPos.equals(pos) ? slab.getFluidState() : level.getFluidState(pos);
        }

        @Override
        public float getShade(Direction direction, boolean shade) {
            return level.getShade(direction, shade);
        }

        @Override
        public float getShade(float normalX, float normalY, float normalZ, boolean shade) {
            return level.getShade(normalX, normalY, normalZ, shade);
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return level.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver resolver) {
            return level.getBlockTint(pos, resolver);
        }

        @Override
        public int getHeight() {
            return level.getHeight();
        }

        @Override
        public int getMinBuildHeight() {
            return level.getMinBuildHeight();
        }
    }
}
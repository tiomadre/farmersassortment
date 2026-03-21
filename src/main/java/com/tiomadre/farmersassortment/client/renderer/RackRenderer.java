package com.tiomadre.farmersassortment.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tiomadre.farmersassortment.core.block.RackBlock;
import com.tiomadre.farmersassortment.core.block.entity.RackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RackRenderer implements BlockEntityRenderer<RackBlockEntity> {
    private static final float RENDER_X_OFFSET = 0.02F;
    private static final double FLAT_ITEM_BASE_Y = 0.752D;
    private static final double BLOCK_ITEM_BASE_Y = 0.83D;
    private static final double ITEM_STACK_Y_OFFSET = 0.03D;
    private static final double LEANING_ITEM_Y = FLAT_ITEM_BASE_Y + 0.022D;
    private static final float BLOCK_ITEM_SCALE = 0.35F;
    private static final float FLAT_ITEM_SCALE = 0.25F;
    private static final float LEANING_ITEM_SCALE = 0.24F;
    private static final float LEANING_ITEM_X_OFFSET = 0.06F;
    private static final float LEANING_ITEM_Z_OFFSET = 0.035F;
    private static final float LEANING_ITEM_X_ROTATION = 104.0F;
    private static final float LEANING_ITEM_Z_ROTATION = -18.0F;
    private static final int FLAT_ITEM_RENDER_COUNT = 3;
    private static final int SLOT_SEED_OFFSET = 31;
    private static final int LEANING_ITEM_SEED_OFFSET = 99;
    private static final float[][] SLOT_POSITIONS = new float[][]{
            {0.3125F, 0.19F},
            {0.625F, 0.19F},
            {0.3125F, 0.49F},
            {0.625F, 0.49F}
    };

    public RackRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RackBlockEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Direction facing = entity.getBlockState().getValue(RackBlock.FACING);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int light = getPackedLight(entity, combinedLight);
        int baseSeed = (int) entity.getBlockPos().asLong();

        for (int slot = 0; slot < entity.getContainerSize(); slot++) {
            ItemStack stack = entity.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            int slotSeed = baseSeed + slot * SLOT_SEED_OFFSET;
            boolean flatModel = isFlatModel(itemRenderer, stack, entity, slotSeed);
            float[] slotPosition = SLOT_POSITIONS[slot];

            renderStackedItems(entity, poseStack, buffer, itemRenderer, stack, facing, slotPosition, light, combinedOverlay, slotSeed, flatModel);

            if (flatModel && stack.getCount() > 1) {
                renderLeaningItem(entity, poseStack, buffer, itemRenderer, stack, facing, slotPosition, light, combinedOverlay, slotSeed + LEANING_ITEM_SEED_OFFSET);
            }
        }
    }

    private void renderStackedItems(RackBlockEntity entity, PoseStack poseStack, MultiBufferSource buffer, ItemRenderer itemRenderer, ItemStack stack, Direction facing, float[] slotPosition, int light, int overlay, int seed, boolean flatModel) {
        int renderCount = flatModel ? Math.min(FLAT_ITEM_RENDER_COUNT, stack.getCount()) : 1;
        double baseY = flatModel ? FLAT_ITEM_BASE_Y : BLOCK_ITEM_BASE_Y;
        float scale = flatModel ? FLAT_ITEM_SCALE : BLOCK_ITEM_SCALE;

        for (int renderIndex = 0; renderIndex < renderCount; renderIndex++) {
            poseStack.pushPose();
            applySlotTransform(poseStack, facing, slotPosition, baseY + renderIndex * ITEM_STACK_Y_OFFSET);
            if (flatModel) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            poseStack.scale(scale, scale, scale);
            renderItem(itemRenderer, stack, entity, poseStack, buffer, light, overlay, seed + renderIndex);
            poseStack.popPose();
        }
    }

    private void renderLeaningItem(RackBlockEntity entity, PoseStack poseStack, MultiBufferSource buffer, ItemRenderer itemRenderer, ItemStack stack, Direction facing, float[] slotPosition, int light, int overlay, int seed) {
        poseStack.pushPose();
        applySlotTransform(poseStack, facing, slotPosition, LEANING_ITEM_Y);
        poseStack.translate(LEANING_ITEM_X_OFFSET, 0.0D, LEANING_ITEM_Z_OFFSET);
        poseStack.mulPose(Axis.XP.rotationDegrees(LEANING_ITEM_X_ROTATION));
        poseStack.mulPose(Axis.ZP.rotationDegrees(LEANING_ITEM_Z_ROTATION));
        poseStack.scale(LEANING_ITEM_SCALE, LEANING_ITEM_SCALE, LEANING_ITEM_SCALE);
        renderItem(itemRenderer, stack, entity, poseStack, buffer, light, overlay, seed);
        poseStack.popPose();
    }

    private void applySlotTransform(PoseStack poseStack, Direction facing, float[] slotPosition, double y) {
        poseStack.translate(0.5D, y, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(slotPosition[0] - 0.5D + RENDER_X_OFFSET, 0.0D, slotPosition[1] - 0.5D);
    }

    private void renderItem(ItemRenderer itemRenderer, ItemStack stack, RackBlockEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, int seed) {
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, entity.getLevel(), seed);
    }

    private int getPackedLight(RackBlockEntity entity, int combinedLight) {
        if (entity.getLevel() == null) {
            return combinedLight;
        }

        BlockPos abovePos = entity.getBlockPos().above();
        int aboveLight = LevelRenderer.getLightColor(entity.getLevel(), abovePos);
        int blockLight = Math.max(LightTexture.block(aboveLight), LightTexture.block(combinedLight));
        int skyLight = Math.max(LightTexture.sky(aboveLight), LightTexture.sky(combinedLight));
        return LightTexture.pack(blockLight, skyLight);
    }

    private boolean isFlatModel(ItemRenderer itemRenderer, ItemStack stack, RackBlockEntity entity, int seed) {
        PoseStack modelPose = new PoseStack();
        return !itemRenderer.getModel(stack, entity.getLevel(), null, seed)
                .applyTransform(ItemDisplayContext.FIXED, modelPose, false)
                .isGui3d();
    }
}
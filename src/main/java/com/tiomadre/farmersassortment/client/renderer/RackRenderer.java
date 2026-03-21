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
    private static final double BLOCK_ITEM_BASE_Y = 0.75D;
    private static final double ITEM_STACK_Y_OFFSET = 0.03D;
    private static final double LEANING_ITEM_Y = FLAT_ITEM_BASE_Y + 0.022D;
    private static final float BLOCK_ITEM_SCALE = 0.35F;
    private static final float FLAT_ITEM_SCALE = 0.25F;
    private static final float LEANING_ITEM_SCALE = 0.24F;
    private static final float LEANING_ITEM_X_OFFSET = 0.06F;
    private static final float LEANING_ITEM_Z_OFFSET = 0.035F;
    private static final float LEANING_ITEM_X_ROTATION = 104.0F;
    private static final float LEANING_ITEM_Z_ROTATION = -18.0F;
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
        int seed = (int) entity.getBlockPos().asLong();

        int light = combinedLight;
        if (entity.getLevel() != null) {
            BlockPos abovePos = entity.getBlockPos().above();
            light = Math.max(combinedLight, LevelRenderer.getLightColor(entity.getLevel(), abovePos));
            int blockLight = Math.max(LightTexture.block(light), LightTexture.block(combinedLight));
            int skyLight = Math.max(LightTexture.sky(light), LightTexture.sky(combinedLight));
            light = LightTexture.pack(blockLight, skyLight);
        }

        for (int slot = 0; slot < entity.getContainerSize(); slot++) {
            ItemStack stack = entity.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            boolean isFlatModel = isFlatModel(itemRenderer, stack, entity, seed + slot * 31);
            int renderCount = isFlatModel ? Math.min(3, stack.getCount()) : 1;
            float[] slotPos = SLOT_POSITIONS[slot];
            double baseY = isFlatModel ? FLAT_ITEM_BASE_Y : BLOCK_ITEM_BASE_Y;
            float scale = isFlatModel ? FLAT_ITEM_SCALE : BLOCK_ITEM_SCALE;

            for (int i = 0; i < renderCount; i++) {
                poseStack.pushPose();
                poseStack.translate(0.5D, baseY + i * ITEM_STACK_Y_OFFSET, 0.5D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
                poseStack.translate(slotPos[0] - 0.5D + RENDER_X_OFFSET, 0.0D, slotPos[1] - 0.5D);
                if (isFlatModel) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                }
                poseStack.scale(scale, scale, scale);

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, combinedOverlay, poseStack, buffer, entity.getLevel(), seed + slot * 31 + i);
                poseStack.popPose();
            }

            if (isFlatModel && stack.getCount() > 1) {
                poseStack.pushPose();
                poseStack.translate(0.5D, LEANING_ITEM_Y, 0.5D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
                poseStack.translate(slotPos[0] - 0.5D + RENDER_X_OFFSET + LEANING_ITEM_X_OFFSET, 0.0D, slotPos[1] - 0.5D + LEANING_ITEM_Z_OFFSET);
                poseStack.mulPose(Axis.XP.rotationDegrees(LEANING_ITEM_X_ROTATION));
                poseStack.mulPose(Axis.ZP.rotationDegrees(LEANING_ITEM_Z_ROTATION));
                poseStack.scale(LEANING_ITEM_SCALE, LEANING_ITEM_SCALE, LEANING_ITEM_SCALE);

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, combinedOverlay, poseStack, buffer, entity.getLevel(), seed + slot * 31 + 99);
                poseStack.popPose();
            }
        }
    }

    private boolean isFlatModel(ItemRenderer itemRenderer, ItemStack stack, RackBlockEntity entity, int seed) {
        PoseStack modelPose = new PoseStack();
        return !itemRenderer.getModel(stack, entity.getLevel(), null, seed)
                .applyTransform(ItemDisplayContext.FIXED, modelPose, false)
                .isGui3d();
    }
}
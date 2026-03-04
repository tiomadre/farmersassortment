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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RackRenderer implements BlockEntityRenderer<RackBlockEntity> {
    private static final float[][] SLOT_POSITIONS = new float[][]{
            {0.3125F, 0.2375F},
            {0.625F, 0.2375F},
            {0.3125F, 0.4375F},
            {0.625F, 0.4375F}
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

            boolean isBlock = stack.getItem() instanceof BlockItem;
            int renderCount = isBlock ? 1 : Math.min(3, stack.getCount());
            float[] slotPos = SLOT_POSITIONS[slot];

            for (int i = 0; i < renderCount; i++) {
                poseStack.pushPose();
                poseStack.translate(0.5D, 0.86D + (isBlock ? 0.0D : i * 0.03D), 0.5D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
                poseStack.translate(slotPos[0] - 0.5D, 0.0D, slotPos[1] - 0.5D);

                if (isBlock) {
                    poseStack.scale(0.32F, 0.32F, 0.32F);
                } else {
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    poseStack.scale(0.25F, 0.25F, 0.25F);
                }

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, combinedOverlay, poseStack, buffer, entity.getLevel(), seed + slot * 31 + i);
                poseStack.popPose();
            }
        }
    }
}
package com.tiomadre.farmersassortment.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tiomadre.farmersassortment.core.registry.FAPumpkins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class FASnowGolemRenderer extends SnowGolemRenderer {
    public FASnowGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        layers.removeIf(layer -> layer instanceof SnowGolemHeadLayer);
        addLayer(new PumpkinHeadLayer(this, context));
    }

    @Override
    public void render(SnowGolem golem, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        boolean headVisible = getModel().getHead().visible;
        ItemStack pumpkin = golem.getItemBySlot(EquipmentSlot.HEAD);

        if (golem.hasPumpkin()
                && pumpkin.is(FAPumpkins.YELLOW.carved.get().asItem())) {
            getModel().getHead().visible = false;
        }

        try {
            super.render(golem, entityYaw, partialTicks, poseStack, buffer, packedLight);
        } finally {
            getModel().getHead().visible = headVisible;
        }
    }

    private static class PumpkinHeadLayer extends SnowGolemHeadLayer {
        private final BlockRenderDispatcher blockRenderer;
        private final ItemRenderer itemRenderer;

        private PumpkinHeadLayer(FASnowGolemRenderer renderer,
                                 EntityRendererProvider.Context context) {
            super(renderer, context.getBlockRenderDispatcher(), context.getItemRenderer());
            blockRenderer = context.getBlockRenderDispatcher();
            itemRenderer = context.getItemRenderer();
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer,
                           int packedLight, SnowGolem golem,
                           float limbSwing, float limbSwingAmount,
                           float partialTicks, float ageInTicks,
                           float netHeadYaw, float headPitch) {
            ItemStack pumpkin = golem.getItemBySlot(EquipmentSlot.HEAD);
            if (!(pumpkin.getItem() instanceof BlockItem blockItem)
                    || !(blockItem.getBlock()
                    instanceof FAPumpkins.VariantCarvedPumpkinBlock)) {
                super.render(poseStack, buffer, packedLight, golem,
                        limbSwing, limbSwingAmount, partialTicks,
                        ageInTicks, netHeadYaw, headPitch);
                return;
            }

            if (!golem.hasPumpkin()) {
                return;
            }

            boolean outline = Minecraft.getInstance()
                    .shouldEntityAppearGlowing(golem) && golem.isInvisible();

            if (golem.isInvisible() && !outline) {
                return;
            }

            poseStack.pushPose();
            getParentModel().getHead().translateAndRotate(poseStack);
            poseStack.translate(0.0F, -0.34375F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(0.625F, -0.625F, -0.625F);

            int overlay = LivingEntityRenderer.getOverlayCoords(golem, 0.0F);

            if (outline) {
                BlockState state = blockItem.getBlock().defaultBlockState();
                BakedModel model = blockRenderer.getBlockModel(state);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                blockRenderer.getModelRenderer().renderModel(
                        poseStack.last(),
                        buffer.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)),
                        state, model, 0.0F, 0.0F, 0.0F, packedLight, overlay);
            } else {
                itemRenderer.renderStatic(
                        golem, pumpkin, ItemDisplayContext.HEAD, false,
                        poseStack, buffer, golem.level(),
                        packedLight, overlay, golem.getId());
            }

            poseStack.popPose();
        }
    }
}

package com.tiomadre.farmersassortment.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TridentItem;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("ALL")
public class ButcherBlockCabinetRenderer implements BlockEntityRenderer<ButcherBlockCabinetBlockEntity> {

    public ButcherBlockCabinetRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ButcherBlockCabinetBlockEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack boardStack = entity.getBoardItem();
        if (boardStack.isEmpty()) {
            return;
        }

        Direction blockFacing = entity.getBlockState().getValue(ButcherBlockCabinetBlock.FACING);
        int seed = (int) entity.getBlockPos().asLong();
        int blockLight = LightTexture.block(combinedLight);
        int skyLight = LightTexture.sky(combinedLight);
        if (entity.getLevel() != null) {
            BlockPos blockPos = entity.getBlockPos();
            int topLight = LevelRenderer.getLightColor(entity.getLevel(), blockPos.above());
            BlockPos boardPos = blockPos.relative(blockFacing).above();
            int boardLight = LevelRenderer.getLightColor(entity.getLevel(), boardPos);

            blockLight = Math.max(blockLight, LightTexture.block(topLight));
            blockLight = Math.max(blockLight, LightTexture.block(boardLight));
            skyLight = Math.max(skyLight, LightTexture.sky(topLight));
            skyLight = Math.max(skyLight, LightTexture.sky(boardLight));
        }
        int light = LightTexture.pack(blockLight, skyLight);

        poseStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        boolean isBlockItem = itemRenderer.getModel(boardStack, entity.getLevel(), null, seed)
                .applyTransform(ItemDisplayContext.FIXED, poseStack, false)
                .isGui3d();
        poseStack.popPose();

        if (entity.isItemCarvingBoard()) {
            renderItemCarved(poseStack, blockFacing, boardStack.getItem().getDefaultInstance());
        } else if (isBlockItem && !boardStack.is(ModTags.FLAT_ON_CUTTING_BOARD)) {
            renderBlock(poseStack, blockFacing);
        } else {
            renderItemLayingDown(poseStack, blockFacing);
        }
        try {
            itemRenderer.renderStatic(boardStack, ItemDisplayContext.FIXED, light, combinedOverlay, poseStack, buffer, entity.getLevel(), seed);
        } finally {
            poseStack.popPose();
        }
    }

    private void renderItemLayingDown(PoseStack poseStack, Direction direction) {
        poseStack.translate(0.5D, 1.02D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    private void renderBlock(PoseStack poseStack, Direction direction) {
        poseStack.translate(0.5D, 1.21D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
        matrixStackIn.translate(0.5D, 1.17D, 0.5D);
        float f = -direction.toYRot() + 180;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        Item toolItem = itemStack.getItem();
        float poseAngle;
        if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem) {
            poseAngle = 225.0F;
        } else if (toolItem instanceof TridentItem) {
            poseAngle = 135.0F;
        } else {
            poseAngle = 180.0F;
        }
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));

        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }
}

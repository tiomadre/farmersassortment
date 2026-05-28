package com.tiomadre.farmersassortment.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TridentItem;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

@SuppressWarnings("ALL")
public class ButcherBlockCabinetRenderer implements BlockEntityRenderer<ButcherBlockCabinetBlockEntity> {
    private static final double CABINET_TOP_OFFSET = 0.9D;
    private final Random random = new Random();

    public ButcherBlockCabinetRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ButcherBlockCabinetBlockEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack boardStack = entity.getBoardItem();
        if (boardStack.isEmpty()) {
            return;
        }

        Direction blockFacing = entity.getBlockState().getValue(ButcherBlockCabinetBlock.FACING);
        int posLong = (int) entity.getBlockPos().asLong();
        int seed = Item.getId(boardStack.getItem()) + boardStack.getDamageValue();
        this.random.setSeed(seed);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int itemRenderCount = this.getModelCount(boardStack);
        int light = getTopFaceLight(entity, combinedLight, blockFacing);
        for (int i = 0; i < itemRenderCount; i++) {
            poseStack.pushPose();
            poseStack.pushPose();
            boolean isBlockItem = itemRenderer.getModel(boardStack, entity.getLevel(), null, 0)
                    .applyTransform(ItemDisplayContext.FIXED, poseStack, false)
                    .isGui3d();
            poseStack.popPose();

            float xOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            float zOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

            if (entity.isItemCarvingBoard()) {
                renderItemCarved(poseStack, blockFacing, boardStack);
            } else if (isBlockItem && !boardStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
                renderBlock(poseStack, blockFacing, xOffset, i, zOffset);
            } else {
                renderItemLayingDown(poseStack, blockFacing, xOffset, i, zOffset);
            }
            itemRenderer.renderStatic(boardStack, ItemDisplayContext.FIXED, light, combinedOverlay, poseStack, buffer, entity.getLevel(), posLong);
            poseStack.popPose();
        }
    }

    public void renderItemLayingDown(PoseStack poseStack, Direction direction, float xOffset, int yIndex, float zOffset) {
        poseStack.translate(0.5D + xOffset, CABINET_TOP_OFFSET + 0.08D + 0.03 * (yIndex + 1), 0.5D + zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    public void renderBlock(PoseStack poseStack, Direction direction, float xOffset, int yIndex, float zOffset) {
        poseStack.translate(0.5D + xOffset, CABINET_TOP_OFFSET + 0.27D + 0.03 * (yIndex + 1), 0.5D + zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    public void renderItemCarved(PoseStack poseStack, Direction direction, ItemStack itemStack) {
        poseStack.translate(0.5D, CABINET_TOP_OFFSET + 0.23D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot() + 180));
        Item toolItem = itemStack.getItem();
        float poseAngle;
        if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem) {
            poseAngle = 225.0F;
        } else if (toolItem instanceof TridentItem) {
            poseAngle = 135.0F;
        } else {
            poseAngle = 180.0F;
        }
        poseStack.mulPose(Axis.ZP.rotationDegrees(poseAngle));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    protected int getModelCount(ItemStack stack) {
        int modelCount = 1;
        if (stack.getCount() > 1) {
            modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
        }
        return modelCount;
    }

    private int getTopFaceLight(ButcherBlockCabinetBlockEntity entity, int combinedLight, Direction blockFacing) {
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
        return LightTexture.pack(blockLight, skyLight);
    }
}
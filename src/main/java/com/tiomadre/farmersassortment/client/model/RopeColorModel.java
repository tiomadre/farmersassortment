package com.tiomadre.farmersassortment.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RopeColorModel extends BakedModelWrapper<BakedModel> {
    private final boolean dyedItem;

    private RopeColorModel(BakedModel model, boolean dyedItem) {
        super(model);
        this.dyedItem = dyedItem;
    }

    @SubscribeEvent
    public static void wrapModels(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> id.getNamespace().equals("farmersdelight")
                && (id.getPath().equals("rope_fence") || id.getPath().equals("rope_fence_gate"))
                ? new RopeColorModel(model, false) : model);
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Override
            public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level,
                                      @Nullable LivingEntity entity, int seed) {
                BakedModel selected = originalModel.getOverrides().resolve(originalModel, stack, level, entity, seed);
                BakedModel delegate = selected == null ? originalModel : selected;
                return new RopeColorModel(delegate, DyeColors.getColor(stack) >= 0);
            }
        };
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext context, PoseStack poses, boolean leftHand) {
        originalModel.applyTransform(context, poses, leftHand);
        return this;
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack stack, boolean fabulous) {
        return List.of(this);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        return getQuads(state, side, random, ModelData.EMPTY, null);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random,
                                    ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = originalModel.getQuads(state, side, random, data, renderType);
        boolean dyed = state == null ? dyedItem : Boolean.TRUE.equals(data.get(RopeColorBlockEntity.DYED));
        if (!dyed) return quads;
        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            ResourceLocation texture = quad.getSprite().contents().name();
            if (quad.getTintIndex() != 0 || !texture.getNamespace().equals("farmersdelight")
                    || !texture.getPath().startsWith("block/rope_fence")) {
                result.add(quad);
                continue;
            }
            TextureAtlasSprite target = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(new ResourceLocation(FarmersAssortment.MOD_ID, texture.getPath() + "_dye"));
            result.add(retexture(quad, target));
        }
        return result;
    }

    private static BakedQuad retexture(BakedQuad quad, TextureAtlasSprite target) {
        int[] vertices = quad.getVertices().clone();
        TextureAtlasSprite source = quad.getSprite();
        int stride = vertices.length / 4;
        for (int vertex = 0; vertex < 4; vertex++) {
            int offset = vertex * stride;
            float u = Float.intBitsToFloat(vertices[offset + 4]);
            float v = Float.intBitsToFloat(vertices[offset + 5]);
            vertices[offset + 4] = Float.floatToRawIntBits(target.getU(
                    (u - source.getU0()) / (source.getU1() - source.getU0()) * 16));
            vertices[offset + 5] = Float.floatToRawIntBits(target.getV(
                    (v - source.getV0()) / (source.getV1() - source.getV0()) * 16));
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), target, quad.isShade());
    }
}

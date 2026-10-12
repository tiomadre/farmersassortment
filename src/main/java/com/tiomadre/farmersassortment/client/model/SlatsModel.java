package com.tiomadre.farmersassortment.client.model;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import com.tiomadre.farmersassortment.core.block.entity.SlatsBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlatsModel extends BakedModelWrapper<BakedModel> {
    private static final ResourceLocation PLACEHOLDER = new ResourceLocation("minecraft", "block/white_wool");
    private static final ModelProperty<Map<Direction, List<Surface>>> SURFACES = new ModelProperty<>();

    public SlatsModel(BakedModel model) {
        super(model);
    }

    @SubscribeEvent
    public static void wrapModels(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> id.getNamespace().equals(FarmersAssortment.MOD_ID)
                && id.getPath().endsWith("_slats") ? new SlatsModel(model) : model);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        ModelData inherited = originalModel.getModelData(level, pos, state, data);
        if (!SlatsBlock.embedded(state)) {
            return inherited;
        }
        BlockState soil = inherited.get(SlatsBlockEntity.SOIL);
        if (soil == null) {
            soil = SlatsBlock.soilAt(level, pos);
        }
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(soil);
        ModelData soilData = model.getModelData(level, pos, soil, ModelData.EMPTY);
        Map<Direction, List<Surface>> surfaces = new EnumMap<>(Direction.class);
        long seed = soil.getSeed(pos);
        for (Direction face : Direction.values()) {
            List<BakedQuad> quads = new ArrayList<>(
                    model.getQuads(soil, face, RandomSource.create(seed), soilData, null));
            for (BakedQuad quad : model.getQuads(soil, null, RandomSource.create(seed), soilData, null)) {
                if (quad.getDirection() == face) {
                    quads.add(quad);
                }
            }
            List<Surface> faces = new ArrayList<>();
            for (BakedQuad quad : quads) {
                int color = quad.isTinted() ? Minecraft.getInstance().getBlockColors()
                        .getColor(soil, level, pos, quad.getTintIndex()) : -1;
                faces.add(new Surface(quad.getSprite(), color));
            }
            if (faces.isEmpty()) {
                faces.add(new Surface(model.getParticleIcon(soilData), -1));
            }
            surfaces.put(face, faces);
        }
        return inherited.derive().with(SURFACES, surfaces).build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        return getQuads(state, side, random, ModelData.EMPTY, null);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random,
                                    ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = originalModel.getQuads(state, side, random, data, renderType);
        Map<Direction, List<Surface>> surfaces = data.get(SURFACES);
        if (state == null || !SlatsBlock.embedded(state) || surfaces == null) {
            return quads;
        }
        List<BakedQuad> result = new ArrayList<>();
        for (BakedQuad quad : quads) {
            if (!quad.getSprite().contents().name().equals(PLACEHOLDER)) {
                result.add(quad);
                continue;
            }
            for (Surface surface : surfaces.get(quad.getDirection())) {
                result.add(retexture(quad, surface));
            }
        }
        return result;
    }

    private static BakedQuad retexture(BakedQuad quad, Surface surface) {
        int[] vertices = quad.getVertices().clone();
        TextureAtlasSprite source = quad.getSprite();
        TextureAtlasSprite target = surface.sprite();
        int stride = vertices.length / 4;
        for (int vertex = 0; vertex < 4; vertex++) {
            int offset = vertex * stride;
            float u = Float.intBitsToFloat(vertices[offset + 4]);
            float v = Float.intBitsToFloat(vertices[offset + 5]);
            vertices[offset + 4] = Float.floatToRawIntBits(target.getU(
                    (u - source.getU0()) / (source.getU1() - source.getU0()) * 16));
            vertices[offset + 5] = Float.floatToRawIntBits(target.getV(
                    (v - source.getV0()) / (source.getV1() - source.getV0()) * 16));
            if (surface.color() != -1) {
                int original = vertices[offset + 3];
                int color = surface.color();
                int red = (original & 255) * (color >> 16 & 255) / 255;
                int green = (original >> 8 & 255) * (color >> 8 & 255) / 255;
                int blue = (original >> 16 & 255) * (color & 255) / 255;
                vertices[offset + 3] = original & 0xFF000000 | blue << 16 | green << 8 | red;
            }
        }
        return new BakedQuad(vertices, -1, quad.getDirection(), target, quad.isShade());
    }

    private record Surface(TextureAtlasSprite sprite, int color) {
    }
}

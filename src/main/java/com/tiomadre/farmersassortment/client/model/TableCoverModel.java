package com.tiomadre.farmersassortment.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.TableBlock;
import com.tiomadre.farmersassortment.core.block.entity.TableBlockEntity;
import com.tiomadre.farmersassortment.core.block.state.TableCoverType;
import com.tiomadre.farmersassortment.core.item.TableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.item.Item;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TableCoverModel extends BakedModelWrapper<BakedModel> {
    private static final ResourceLocation PLACEHOLDER = new ResourceLocation("minecraft", "block/white_wool");
    private static final ModelProperty<Integer> COVER_COLOR = new ModelProperty<>();
    private static final Map<Item, CoverSurface> SURFACES = new ConcurrentHashMap<>();
    private final ItemStack itemCover;

    public TableCoverModel(BakedModel model) {
        this(model, ItemStack.EMPTY);
    }

    private TableCoverModel(BakedModel model, ItemStack cover) {
        super(model);
        itemCover = cover.copy();
    }

    @SubscribeEvent
    public static void wrapModels(ModelEvent.ModifyBakingResult event) {
        SURFACES.clear();
        event.getModels().replaceAll((id, model) -> id.getNamespace().equals(FarmersAssortment.MOD_ID)
                && id.getPath().contains("_table") ? new TableCoverModel(model) : model);
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Override
            public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level,
                                      @Nullable LivingEntity entity, int seed) {
                BakedModel selected = originalModel.getOverrides().resolve(originalModel, stack, level, entity, seed);
                BakedModel delegate = selected == null ? originalModel : selected;
                if (delegate instanceof TableCoverModel wrapped) {
                    delegate = wrapped.originalModel;
                }
                ItemStack cover = TableItem.getRug(stack) == TableCoverType.CUSTOM_CARPET
                        ? TableItem.getCovering(stack) : ItemStack.EMPTY;
                return new TableCoverModel(delegate, cover);
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
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        ModelData inherited = originalModel.getModelData(level, pos, state, data);
        ItemStack cover = inherited.get(TableBlockEntity.COVER);
        if (cover != null && cover.getItem() instanceof BlockItem blockItem) {
            CoverSurface surface = surface(cover);
            int color = surface.tintIndex() < 0 ? -1 : Minecraft.getInstance().getBlockColors()
                    .getColor(blockItem.getBlock().defaultBlockState(), level, pos, surface.tintIndex());
            return inherited.derive().with(COVER_COLOR, color).build();
        }
        return inherited;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        return getQuads(state, side, random, ModelData.EMPTY, null);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random,
                                    ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = originalModel.getQuads(state, side, random, data, renderType);
        ItemStack cover = itemCover;
        if (state != null && state.hasProperty(TableBlock.RUG)
                && state.getValue(TableBlock.RUG) == TableCoverType.CUSTOM_CARPET) {
            ItemStack stored = data.get(TableBlockEntity.COVER);
            cover = stored == null ? ItemStack.EMPTY : stored;
        }
        if (!(cover.getItem() instanceof BlockItem)) {
            return quads;
        }
        CoverSurface surface = surface(cover);
        Integer worldColor = data.get(COVER_COLOR);
        int color = worldColor != null ? worldColor : surface.tintIndex() < 0 ? -1
                : Minecraft.getInstance().getItemColors().getColor(cover, surface.tintIndex());
        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            result.add(quad.getSprite().contents().name().equals(PLACEHOLDER)
                    ? retexture(quad, surface.sprite(), color) : quad);
        }
        return result;
    }

    private static CoverSurface surface(ItemStack cover) {
        return SURFACES.computeIfAbsent(cover.getItem(), TableCoverModel::findSurface);
    }

    private static CoverSurface findSurface(Item item) {
        BlockState carpet = ((BlockItem) item).getBlock().defaultBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(carpet);
        RandomSource random = RandomSource.create(42);
        List<BakedQuad> faces = model.getQuads(carpet, Direction.UP, random, ModelData.EMPTY, null);
        if (faces.isEmpty()) {
            faces = model.getQuads(carpet, null, random, ModelData.EMPTY, null).stream()
                    .filter(quad -> quad.getDirection() == Direction.UP).toList();
        }
        return faces.isEmpty() ? new CoverSurface(model.getParticleIcon(ModelData.EMPTY), -1)
                : new CoverSurface(faces.get(0).getSprite(), faces.get(0).getTintIndex());
    }

    private static BakedQuad retexture(BakedQuad quad, TextureAtlasSprite target, int color) {
        int[] vertices = quad.getVertices().clone();
        TextureAtlasSprite source = quad.getSprite();
        int stride = vertices.length / 4;
        for (int vertex = 0; vertex < 4; vertex++) {
            int offset = vertex * stride;
            float u = Float.intBitsToFloat(vertices[offset + 4]);
            float v = Float.intBitsToFloat(vertices[offset + 5]);
            vertices[offset + 4] = Float.floatToRawIntBits(target.getU((u - source.getU0())
                    / (source.getU1() - source.getU0()) * 16));
            vertices[offset + 5] = Float.floatToRawIntBits(target.getV((v - source.getV0())
                    / (source.getV1() - source.getV0()) * 16));
            if (color != -1) {
                int original = vertices[offset + 3];
                int red = (original & 255) * (color >> 16 & 255) / 255;
                int green = (original >> 8 & 255) * (color >> 8 & 255) / 255;
                int blue = (original >> 16 & 255) * (color & 255) / 255;
                vertices[offset + 3] = original & 0xFF000000 | blue << 16 | green << 8 | red;
            }
        }
        return new BakedQuad(vertices, -1, quad.getDirection(), target, quad.isShade());
    }

    private record CoverSurface(TextureAtlasSprite sprite, int tintIndex) {
    }
}

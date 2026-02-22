package com.tiomadre.farmersassortment.client.renderer;

import com.tiomadre.farmersassortment.core.block.state.SlabRugState;
import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SlabTableclothBakedModel extends BakedModelWrapper<BakedModel> {
    private final Map<StoolRugType, BakedModel> overlayModels;

    public SlabTableclothBakedModel(BakedModel originalModel, Map<StoolRugType, BakedModel> overlayModels) {
        super(originalModel);
        this.overlayModels = overlayModels;
    }

    @Override
    public List<net.minecraft.client.renderer.block.model.BakedQuad> getQuads(@Nullable BlockState state,
                                                                              @Nullable Direction side,
                                                                              RandomSource random,
                                                                              ModelData modelData,
                                                                              @Nullable net.minecraft.client.renderer.RenderType renderType) {
        List<net.minecraft.client.renderer.block.model.BakedQuad> base = super.getQuads(state, side, random, modelData, renderType);
        if (state == null || !(state.getBlock() instanceof SlabBlock) || state.getValue(SlabBlock.TYPE) != SlabType.TOP) {
            return base;
        }

        StoolRugType rugType = state.getValue(SlabRugState.RUG);
        if (!rugType.hasRug()) {
            return base;
        }

        BakedModel overlayModel = overlayModels.get(rugType);
        if (overlayModel == null) {
            return base;
        }

        List<net.minecraft.client.renderer.block.model.BakedQuad> overlay = overlayModel.getQuads(state, side, random, modelData, renderType);
        if (overlay.isEmpty()) {
            return base;
        }

        List<net.minecraft.client.renderer.block.model.BakedQuad> merged = new ArrayList<>(base.size() + overlay.size());
        merged.addAll(base);
        merged.addAll(overlay);
        return merged;
    }
}
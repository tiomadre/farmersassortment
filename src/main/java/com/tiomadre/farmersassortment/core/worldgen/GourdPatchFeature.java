package com.tiomadre.farmersassortment.core.worldgen;

import com.tiomadre.farmersassortment.core.registry.FAGourds;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public final class GourdPatchFeature extends Feature<NoneFeatureConfiguration> {
    public GourdPatchFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        int target = 4 + random.nextInt(2);
        List<BlockPos> positions = new ArrayList<>();

        for (int attempt = 0; attempt < 64 && positions.size() < target; attempt++) {
            int x = origin.getX() + random.nextInt(7) - 3;
            int z = origin.getZ() + random.nextInt(7) - 3;
            BlockPos pos = level.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, origin.getY(), z));

            if (Math.abs(pos.getY() - origin.getY()) > 3
                    || positions.contains(pos)
                    || !level.isEmptyBlock(pos)
                    || !level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)
                    || !FAGourds.PILE.get().defaultBlockState().canSurvive(level, pos)) {
                continue;
            }

            positions.add(pos);
        }

        if (positions.size() < 4) {
            return false;
        }

        int variant = random.nextInt(FAGourds.Variant.values().length);
        int pileIndex = random.nextBoolean() ? random.nextInt(positions.size()) : -1;

        for (int index = 0; index < positions.size(); index++) {
            int count = index == pileIndex ? 2 + random.nextInt(2) : 1;
            BlockState state = FAGourds.PILE.get().defaultBlockState();

            for (int slot = 0; slot < FAGourds.GourdPileBlock.SLOTS.size(); slot++) {
                state = state.setValue(
                        FAGourds.GourdPileBlock.SLOTS.get(slot),
                        slot < count ? variant : FAGourds.GourdPileBlock.EMPTY);
            }

            setBlock(level, positions.get(index), state);
        }

        return true;
    }
}
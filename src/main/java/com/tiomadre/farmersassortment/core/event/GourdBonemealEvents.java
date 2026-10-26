package com.tiomadre.farmersassortment.core.event;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.config.FAConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID)
public class GourdBonemealEvents {
    private GourdBonemealEvents() {
    }

    @SubscribeEvent
    public static void growGourd(BonemealEvent event) {
        if (!FAConfig.BONEMEAL_MATURE_GOURD_STEMS.get()
                || event.getResult() != Event.Result.DEFAULT) {
            return;
        }

        var state = event.getBlock();
        if (!(state.getBlock() instanceof StemBlock stem)
                || state.getValue(StemBlock.AGE) != StemBlock.MAX_AGE) {
            return;
        }

        var level = event.getLevel();
        BlockPos pos = event.getPos();
        StemGrownBlock fruit = stem.getFruit();
        List<Direction> availableDirections = new ArrayList<>();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos fruitPos = pos.relative(direction);
            BlockPos soilPos = fruitPos.below();

            if (!level.hasChunkAt(fruitPos) || !level.hasChunkAt(soilPos)) {
                continue;
            }

            var soil = level.getBlockState(soilPos);
            if (level.isEmptyBlock(fruitPos)
                    && (soil.canSustainPlant(level, soilPos, Direction.UP, fruit)
                    || soil.is(Blocks.FARMLAND)
                    || soil.is(BlockTags.DIRT))) {
                availableDirections.add(direction);
            }
        }

        if (availableDirections.isEmpty()) {
            return;
        }

        if (!level.isClientSide) {
            Direction direction = availableDirections.get(
                    level.random.nextInt(availableDirections.size()));

            if (!level.setBlockAndUpdate(
                    pos.relative(direction), fruit.defaultBlockState())) {
                return;
            }

            // when stem is attached, it will stop another fruit from growing.
            level.setBlockAndUpdate(pos, fruit.getAttachedStem().defaultBlockState()
                    .setValue(HorizontalDirectionalBlock.FACING, direction));
        }

        event.setResult(Event.Result.ALLOW);
    }
}

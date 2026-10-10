package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.FloatingDrawerBlock;
import com.tiomadre.farmersassortment.core.block.entity.FloatingDrawerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FloatingDrawerItem extends BlockItem {
    public FloatingDrawerItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());

        if (context.getClickedFace() == Direction.UP
                && FloatingDrawerBlock.isBottomSlab(clickedState)) {
            BlockPlaceContext placement = new BlockPlaceContext(context) {
                {
                    replaceClicked = true;
                }
            };

            return place(placement);
        }

        return super.useOn(context);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState previousState = level.getBlockState(pos);

        if (!super.placeBlock(context, state)) {
            return false;
        }

        if (state.getValue(FloatingDrawerBlock.HAS_SLAB)
                && level.getBlockEntity(pos) instanceof FloatingDrawerBlockEntity drawer) {
            drawer.setSlabState(previousState);
        }

        return true;
    }
}

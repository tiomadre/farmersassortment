package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import com.tiomadre.farmersassortment.core.block.entity.SlatsBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SlatsItem extends BlockItem {
    private final String fallbackName;

    public SlatsItem(Block block, Properties properties, String fallbackName) {
        super(block, properties);
        this.fallbackName = fallbackName;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatableWithFallback(getDescriptionId(stack), fallbackName);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState clicked = context.getLevel().getBlockState(context.getClickedPos());
        if (context.getClickedFace() == Direction.UP && SlatsBlock.canEmbed(clicked)) {
            return place(new EmbeddingContext(context, clicked));
        }
        return super.useOn(context);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        if (!(context instanceof EmbeddingContext embedding)) {
            return super.placeBlock(context, state);
        }
        if (!context.getLevel().setBlock(context.getClickedPos(), state, 2)) {
            return false;
        }
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof SlatsBlockEntity slats) {
            slats.setSoil(embedding.soil);
            state.updateNeighbourShapes(context.getLevel(), context.getClickedPos(), 3);
            context.getLevel().updateNeighborsAt(context.getClickedPos(), state.getBlock());
        }
        return true;
    }

    public static class EmbeddingContext extends BlockPlaceContext {
        private final BlockState soil;

        public EmbeddingContext(UseOnContext context, BlockState soil) {
            super(context);
            this.soil = soil;
            replaceClicked = true;
        }
    }
}

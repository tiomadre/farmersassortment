package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class StoolItem extends BlockItem {
    private static final String BLOCK_STATE_TAG = "BlockStateTag";
    private static final String RUG_TAG = "rug";

    public StoolItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static ItemStack applyRugToStack(ItemStack stack, StoolRugType rug) {
        if (!rug.hasRug()) {
            CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
            if (stateTag != null) {
                stateTag.remove(RUG_TAG);
                if (stateTag.isEmpty()) {
                    stack.removeTagKey(BLOCK_STATE_TAG);
                }
            }
            return stack;
        }

        stack.getOrCreateTagElement(BLOCK_STATE_TAG).putString(RUG_TAG, rug.getSerializedName());
        return stack;
    }

    public static StoolRugType getRug(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        if (stateTag != null && stateTag.contains(RUG_TAG, Tag.TAG_STRING)) {
            String rugName = stateTag.getString(RUG_TAG);
            for (StoolRugType rugType : StoolRugType.values()) {
                if (rugType.getSerializedName().equals(rugName)) {
                    return rugType;
                }
            }
        }
        return StoolRugType.NONE;
    }
}
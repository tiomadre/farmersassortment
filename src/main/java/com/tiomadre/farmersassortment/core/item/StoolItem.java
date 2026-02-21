package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        StoolRugType rugType = getRug(stack);
        if (!rugType.hasRug()) {
            return;
        }

        tooltip.add(Component.translatable("tooltip.farmersassortment.stool.covered",
                Component.translatable(rugType.rugItem().getDescriptionId())));
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
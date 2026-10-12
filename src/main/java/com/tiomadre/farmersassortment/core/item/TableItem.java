package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.state.TableCoverType;
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

public class TableItem extends BlockItem {
    private static final String BLOCK_STATE_TAG = "BlockStateTag";
    private static final String RUG_TAG = "rug";
    private static final String COVER_TAG = "TableCovering";

    public TableItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static ItemStack applyRugToStack(ItemStack stack, TableCoverType rug) {
        stack.removeTagKey(COVER_TAG);
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

    public static ItemStack applyCoverToStack(ItemStack stack, TableCoverType rug, ItemStack covering) {
        applyRugToStack(stack, rug);
        if (rug == TableCoverType.CUSTOM_CARPET && TableCoverType.isCarpet(covering.getItem())) {
            stack.getOrCreateTag().put(COVER_TAG, covering.copyWithCount(1).save(new CompoundTag()));
        }
        return stack;
    }

    public static ItemStack getCovering(ItemStack stack) {
        TableCoverType rug = getRug(stack);
        if (rug == TableCoverType.CUSTOM_CARPET) {
            ItemStack covering = stack.hasTag() ? ItemStack.of(stack.getTag().getCompound(COVER_TAG)) : ItemStack.EMPTY;
            return TableCoverType.isCarpet(covering.getItem()) ? covering.copyWithCount(1) : ItemStack.EMPTY;
        }
        return rug.rugItem() == null ? ItemStack.EMPTY : new ItemStack(rug.rugItem());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        TableCoverType rugType = getRug(stack);
        if (!rugType.hasRug()) {
            return;
        }

        ItemStack covering = getCovering(stack);
        if (!covering.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.farmersassortment.table.covered", covering.getHoverName()));
        }
    }

    public static TableCoverType getRug(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        if (stateTag != null && stateTag.contains(RUG_TAG, Tag.TAG_STRING)) {
            String rugName = stateTag.getString(RUG_TAG);
            for (TableCoverType rugType : TableCoverType.values()) {
                if (rugType.getSerializedName().equals(rugName)) {
                    return rugType;
                }
            }
        }
        return TableCoverType.NONE;
    }
}

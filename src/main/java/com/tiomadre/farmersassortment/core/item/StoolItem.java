package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.state.FurnitureCoverType;
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
    private static final String COVER_TAG = "StoolCovering";

    @Nullable
    private final String fallbackName;

    public StoolItem(Block block, Properties properties) {
        this(block, properties, null);
    }

    public StoolItem(Block block, Properties properties, @Nullable String fallbackName) {
        super(block, properties);
        this.fallbackName = fallbackName;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return fallbackName == null
                ? super.getName(stack)
                : Component.translatableWithFallback(getDescriptionId(stack), fallbackName);
    }

    public static ItemStack applyRugToStack(ItemStack stack, FurnitureCoverType rug) {
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

    public static ItemStack applyCoverToStack(ItemStack stack, FurnitureCoverType rug, ItemStack covering) {
        applyRugToStack(stack, rug);
        if (rug == FurnitureCoverType.CUSTOM_CARPET && FurnitureCoverType.isCarpet(covering.getItem())) {
            stack.getOrCreateTag().put(COVER_TAG, covering.copyWithCount(1).save(new CompoundTag()));
        }
        return stack;
    }

    public static ItemStack getCovering(ItemStack stack) {
        FurnitureCoverType rug = getRug(stack);
        if (rug == FurnitureCoverType.CUSTOM_CARPET) {
            CompoundTag tag = stack.getTag();
            ItemStack covering = tag == null ? ItemStack.EMPTY : ItemStack.of(tag.getCompound(COVER_TAG));
            return FurnitureCoverType.isCarpet(covering.getItem()) ? covering.copyWithCount(1) : ItemStack.EMPTY;
        }
        return rug.rugItem() == null ? ItemStack.EMPTY : new ItemStack(rug.rugItem());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ItemStack covering = getCovering(stack);
        if (!covering.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.farmersassortment.stool.covered", covering.getHoverName()));
        }
    }
    public static FurnitureCoverType getRug(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        if (stateTag != null && stateTag.contains(RUG_TAG, Tag.TAG_STRING)) {
            String rugName = stateTag.getString(RUG_TAG);
            for (FurnitureCoverType rugType : FurnitureCoverType.values()) {
                if (rugType.getSerializedName().equals(rugName)) {
                    return rugType;
                }
            }
        }
        return FurnitureCoverType.NONE;
    }
}
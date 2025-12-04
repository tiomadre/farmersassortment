package com.tiomadre.farmersassortment.core.item;

import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.CookingPotItem;

import java.util.List;

public class TerracottaCookingPotItem extends CookingPotItem {
    private static final String BLOCK_STATE_TAG = "BlockStateTag";
    private static final String COLOR_TAG = "color";

    public TerracottaCookingPotItem(net.minecraft.world.level.block.Block block, Item.Properties properties) {
        super(block, properties);
    }

    public static ItemStack applyColorToStack(ItemStack stack, TerracottaCookingPotColor color) {
        if (color == TerracottaCookingPotColor.NONE) {
            CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
            if (stateTag != null) {
                stateTag.remove(COLOR_TAG);
                if (stateTag.isEmpty()) {
                    stack.removeTagKey(BLOCK_STATE_TAG);
                }
            }
            return stack;
        }

        CompoundTag stateTag = stack.getOrCreateTagElement(BLOCK_STATE_TAG);
        stateTag.putString(COLOR_TAG, color.getSerializedName());
        return stack;
    }

    public static TerracottaCookingPotColor getColor(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        if (stateTag != null && stateTag.contains(COLOR_TAG, Tag.TAG_STRING)) {
            return TerracottaCookingPotColor.byName(stateTag.getString(COLOR_TAG));
        }
        return TerracottaCookingPotColor.NONE;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        TerracottaCookingPotColor color = getColor(stack);
        if (color.isDyed() && color.dyeColor() != null) {
            tooltip.add(Component.translatable("tooltip.farmersassortment.terracotta_cooking_pot.dyed",
                    Component.translatable("color.minecraft." + color.dyeColor().getName())));
        } else {
            tooltip.add(Component.translatable("tooltip.farmersassortment.terracotta_cooking_pot.undyed"));
        }
    }
}

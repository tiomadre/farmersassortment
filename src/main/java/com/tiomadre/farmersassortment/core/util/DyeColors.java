package com.tiomadre.farmersassortment.core.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public final class DyeColors {
    private DyeColors() {}

    public static int getColor(ItemStack stack) {
        CompoundTag display = stack.getTagElement("display");
        return display != null && display.contains("color", Tag.TAG_ANY_NUMERIC)
                ? display.getInt("color") & 0xFFFFFF : -1;
    }

    public static void setColor(ItemStack stack, int color) {
        if (color >= 0) {
            stack.getOrCreateTagElement("display").putInt("color", color & 0xFFFFFF);
        } else {
            CompoundTag display = stack.getTagElement("display");
            if (display != null) {
                display.remove("color");
                if (display.isEmpty()) stack.removeTagKey("display");
            }
        }
    }

    public static int blend(List<ItemStack> stacks, DyeItem dye) {
        int red = 0, green = 0, blue = 0, samples = 0;
        float brightness = 0;
        for (ItemStack stack : stacks) {
            int color = getColor(stack);
            if (color < 0) continue;
            int r = color >> 16 & 255, g = color >> 8 & 255, b = color & 255;
            red += r;
            green += g;
            blue += b;
            brightness += Math.max(r, Math.max(g, b));
            samples++;
        }
        float[] components = dye.getDyeColor().getTextureDiffuseColors();
        int r = (int) (components[0] * 255);
        int g = (int) (components[1] * 255);
        int b = (int) (components[2] * 255);
        red += r;
        green += g;
        blue += b;
        brightness += Math.max(r, Math.max(g, b));
        samples++;
        red /= samples;
        green /= samples;
        blue /= samples;
        float maximum = Math.max(red, Math.max(green, blue));
        if (maximum > 0) {
            float average = brightness / samples;
            red = (int) (red * average / maximum);
            green = (int) (green * average / maximum);
            blue = (int) (blue * average / maximum);
        }
        return red << 16 | green << 8 | blue;
    }
}

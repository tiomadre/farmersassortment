package com.tiomadre.farmersassortment.core.block.state;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;

public enum TerracottaCookingPotColor implements StringRepresentable {
    NONE("none", null),
    WHITE("white", DyeColor.WHITE),
    ORANGE("orange", DyeColor.ORANGE),
    MAGENTA("magenta", DyeColor.MAGENTA),
    LIGHT_BLUE("light_blue", DyeColor.LIGHT_BLUE),
    YELLOW("yellow", DyeColor.YELLOW),
    LIME("lime", DyeColor.LIME),
    PINK("pink", DyeColor.PINK),
    GRAY("gray", DyeColor.GRAY),
    LIGHT_GRAY("light_gray", DyeColor.LIGHT_GRAY),
    CYAN("cyan", DyeColor.CYAN),
    PURPLE("purple", DyeColor.PURPLE),
    BLUE("blue", DyeColor.BLUE),
    BROWN("brown", DyeColor.BROWN),
    GREEN("green", DyeColor.GREEN),
    RED("red", DyeColor.RED),
    BLACK("black", DyeColor.BLACK);

    private final String name;
    @Nullable
    private final DyeColor dyeColor;

    TerracottaCookingPotColor(String name, @Nullable DyeColor dyeColor) {
        this.name = name;
        this.dyeColor = dyeColor;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean isDyed() {
        return this.dyeColor != null;
    }

    @Nullable
    public DyeColor dyeColor() {
        return this.dyeColor;
    }

    public String textureName() {
        return this == NONE ? "terracotta_cooking_pot" : "terracotta_cooking_pot_" + this.name;
    }

    public ResourceLocation sideTexture() {
        return new ResourceLocation(FarmersAssortment.MOD_ID, "block/" + textureName() + "_side");
    }

    public ResourceLocation topTexture() {
        return new ResourceLocation(FarmersAssortment.MOD_ID, "block/" + textureName() + "_top");
    }

    public ResourceLocation bottomTexture() {
        return new ResourceLocation(FarmersAssortment.MOD_ID, "block/" + textureName() + "_bottom");
    }

    public ResourceLocation partsTexture() {
        return new ResourceLocation(FarmersAssortment.MOD_ID, "block/" + textureName() + "_parts");
    }

    public static TerracottaCookingPotColor fromDyeColor(DyeColor dyeColor) {
        return Arrays.stream(values())
                .filter(color -> color.dyeColor == dyeColor)
                .findFirst()
                .orElse(NONE);
    }

    public static TerracottaCookingPotColor byName(String name) {
        return Arrays.stream(values())
                .filter(color -> color.name.equals(name.toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElse(NONE);
    }
}

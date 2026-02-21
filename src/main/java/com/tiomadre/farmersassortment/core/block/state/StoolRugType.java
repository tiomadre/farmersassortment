package com.tiomadre.farmersassortment.core.block.state;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum StoolRugType implements StringRepresentable {
    NONE("none", null, null),
    CANVAS("canvas", "farmersdelight:canvas_rug", "farmersdelight:block/canvas_rug"),
    WHITE("white", "farmersassortment:white_canvas_rug", "farmersassortment:block/white_canvas_rug"),
    ORANGE("orange", "farmersassortment:orange_canvas_rug", "farmersassortment:block/orange_canvas_rug"),
    MAGENTA("magenta", "farmersassortment:magenta_canvas_rug", "farmersassortment:block/magenta_canvas_rug"),
    LIGHT_BLUE("light_blue", "farmersassortment:light_blue_canvas_rug", "farmersassortment:block/light_blue_canvas_rug"),
    YELLOW("yellow", "farmersassortment:yellow_canvas_rug", "farmersassortment:block/yellow_canvas_rug"),
    LIME("lime", "farmersassortment:lime_canvas_rug", "farmersassortment:block/lime_canvas_rug"),
    PINK("pink", "farmersassortment:pink_canvas_rug", "farmersassortment:block/pink_canvas_rug"),
    GRAY("gray", "farmersassortment:gray_canvas_rug", "farmersassortment:block/gray_canvas_rug"),
    LIGHT_GRAY("light_gray", "farmersassortment:light_gray_canvas_rug", "farmersassortment:block/light_gray_canvas_rug"),
    CYAN("cyan", "farmersassortment:cyan_canvas_rug", "farmersassortment:block/cyan_canvas_rug"),
    PURPLE("purple", "farmersassortment:purple_canvas_rug", "farmersassortment:block/purple_canvas_rug"),
    BLUE("blue", "farmersassortment:blue_canvas_rug", "farmersassortment:block/blue_canvas_rug"),
    BROWN("brown", "farmersassortment:brown_canvas_rug", "farmersassortment:block/brown_canvas_rug"),
    GREEN("green", "farmersassortment:green_canvas_rug", "farmersassortment:block/green_canvas_rug"),
    RED("red", "farmersassortment:red_canvas_rug", "farmersassortment:block/red_canvas_rug"),
    BLACK("black", "farmersassortment:black_canvas_rug", "farmersassortment:block/black_canvas_rug");

    private final String name;
    @Nullable
    private final String itemId;
    @Nullable
    private final String texturePath;

    StoolRugType(String name, @Nullable String itemId, @Nullable String texturePath) {
        this.name = name;
        this.itemId = itemId;
        this.texturePath = texturePath;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean hasRug() {
        return this != NONE;
    }

    @Nullable
    public String texturePath() {
        return this.texturePath;
    }

    @Nullable
    public Item rugItem() {
        if (this.itemId == null) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(this.itemId));
    }

    public String extrudeTexturePath() {
        if (this == CANVAS) {
            return "farmersdelight:block/canvas_rug_extrudes";
        }
        return FarmersAssortment.MOD_ID + ":block/" + this.name + "_canvas_rug_extrudes";
    }
}
package com.tiomadre.farmersassortment.core.block.state;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum TableCoverType implements StringRepresentable {
    NONE(StoolRugType.NONE),
    CANVAS(StoolRugType.CANVAS),
    WHITE(StoolRugType.WHITE),
    ORANGE(StoolRugType.ORANGE),
    MAGENTA(StoolRugType.MAGENTA),
    LIGHT_BLUE(StoolRugType.LIGHT_BLUE),
    YELLOW(StoolRugType.YELLOW),
    LIME(StoolRugType.LIME),
    PINK(StoolRugType.PINK),
    GRAY(StoolRugType.GRAY),
    LIGHT_GRAY(StoolRugType.LIGHT_GRAY),
    CYAN(StoolRugType.CYAN),
    PURPLE(StoolRugType.PURPLE),
    BLUE(StoolRugType.BLUE),
    BROWN(StoolRugType.BROWN),
    GREEN(StoolRugType.GREEN),
    RED(StoolRugType.RED),
    BLACK(StoolRugType.BLACK),
    WHITE_CARPET("white_carpet", "minecraft:white_carpet", "minecraft:block/white_wool"),
    ORANGE_CARPET("orange_carpet", "minecraft:orange_carpet", "minecraft:block/orange_wool"),
    MAGENTA_CARPET("magenta_carpet", "minecraft:magenta_carpet", "minecraft:block/magenta_wool"),
    LIGHT_BLUE_CARPET("light_blue_carpet", "minecraft:light_blue_carpet", "minecraft:block/light_blue_wool"),
    YELLOW_CARPET("yellow_carpet", "minecraft:yellow_carpet", "minecraft:block/yellow_wool"),
    LIME_CARPET("lime_carpet", "minecraft:lime_carpet", "minecraft:block/lime_wool"),
    PINK_CARPET("pink_carpet", "minecraft:pink_carpet", "minecraft:block/pink_wool"),
    GRAY_CARPET("gray_carpet", "minecraft:gray_carpet", "minecraft:block/gray_wool"),
    LIGHT_GRAY_CARPET("light_gray_carpet", "minecraft:light_gray_carpet", "minecraft:block/light_gray_wool"),
    CYAN_CARPET("cyan_carpet", "minecraft:cyan_carpet", "minecraft:block/cyan_wool"),
    PURPLE_CARPET("purple_carpet", "minecraft:purple_carpet", "minecraft:block/purple_wool"),
    BLUE_CARPET("blue_carpet", "minecraft:blue_carpet", "minecraft:block/blue_wool"),
    BROWN_CARPET("brown_carpet", "minecraft:brown_carpet", "minecraft:block/brown_wool"),
    GREEN_CARPET("green_carpet", "minecraft:green_carpet", "minecraft:block/green_wool"),
    RED_CARPET("red_carpet", "minecraft:red_carpet", "minecraft:block/red_wool"),
    BLACK_CARPET("black_carpet", "minecraft:black_carpet", "minecraft:block/black_wool"),
    MOSS_CARPET("moss_carpet", "minecraft:moss_carpet", "minecraft:block/moss_block");
    private final String name;
    private final String itemId;
    private final String texturePath;
    private final StoolRugType canvasRug;

    TableCoverType(StoolRugType canvasRug) {
        this.name = canvasRug.getSerializedName();
        this.itemId = null;
        this.texturePath = canvasRug.texturePath();
        this.canvasRug = canvasRug;
    }

    TableCoverType(String name, String itemId, String texturePath) {
        this.name = name;
        this.itemId = itemId;
        this.texturePath = texturePath;
        this.canvasRug = null;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public boolean hasRug() {
        return this != NONE;
    }

    @Nullable
    public String texturePath() {
        return texturePath;
    }

    public String extrudeTexturePath() {
        return canvasRug != null ? canvasRug.extrudeTexturePath() : texturePath;
    }

    @Nullable
    public Item rugItem() {
        return canvasRug != null ? canvasRug.rugItem()
                : ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
    }

    @Nullable
    public static TableCoverType fromItem(Item item) {
        for (TableCoverType cover : values()) {
            if (cover.hasRug() && cover.rugItem() == item) {
                return cover;
            }
        }
        return null;
    }
}
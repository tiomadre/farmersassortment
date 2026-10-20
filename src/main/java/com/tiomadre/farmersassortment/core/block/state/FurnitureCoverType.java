package com.tiomadre.farmersassortment.core.block.state;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum FurnitureCoverType implements StringRepresentable {
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
    BLACK("black", "farmersassortment:black_canvas_rug", "farmersassortment:block/black_canvas_rug"),
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
    MOSS_CARPET("moss_carpet", "minecraft:moss_carpet", "minecraft:block/moss_block"),
    CUSTOM_CARPET("custom_carpet", null, "minecraft:block/white_wool");

    private static final TagKey<Block> CARPETS = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "carpets"));
    private static final TagKey<Block> FORGE_CARPETS = TagKey.create(Registries.BLOCK, new ResourceLocation("forge", "carpets"));
    private static final TagKey<Block> COMMON_CARPETS = TagKey.create(Registries.BLOCK, new ResourceLocation("c", "carpets"));

    private final String name;
    @Nullable
    private final String itemId;
    @Nullable
    private final String texturePath;

    FurnitureCoverType(String name, @Nullable String itemId, @Nullable String texturePath) {
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
            return "farmersdelight:block/canvas_rug_fraying";
        }
        if (this.name.endsWith("_carpet")) {
            return this.texturePath;
        }
        return FarmersAssortment.MOD_ID + ":block/" + this.name + "_canvas_rug_extrudes";
    }

    public boolean isCanvas() {
        return hasRug() && !name.endsWith("_carpet");
    }

    @Nullable
    public static FurnitureCoverType fromItem(Item item) {
        for (FurnitureCoverType cover : values()) {
            if (cover.hasRug() && cover.rugItem() == item) {
                return cover;
            }
        }
        return isCarpet(item) ? CUSTOM_CARPET : null;
    }

    public static boolean isCarpet(Item item) {
        return item instanceof BlockItem blockItem
                && (blockItem.getBlock() instanceof CarpetBlock
                || blockItem.getBlock().defaultBlockState().is(BlockTags.WOOL_CARPETS)
                || blockItem.getBlock().defaultBlockState().is(CARPETS)
                || blockItem.getBlock().defaultBlockState().is(FORGE_CARPETS)
                || blockItem.getBlock().defaultBlockState().is(COMMON_CARPETS));
    }
}

package com.tiomadre.farmersassortment.core.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public final class FARugs {
    public static final RegistryObject<CarpetBlock> WHITE_CANVAS_RUG = registerCanvasRug("white");
    public static final RegistryObject<CarpetBlock> ORANGE_CANVAS_RUG = registerCanvasRug("orange");
    public static final RegistryObject<CarpetBlock> MAGENTA_CANVAS_RUG = registerCanvasRug("magenta");
    public static final RegistryObject<CarpetBlock> LIGHT_BLUE_CANVAS_RUG = registerCanvasRug("light_blue");
    public static final RegistryObject<CarpetBlock> YELLOW_CANVAS_RUG = registerCanvasRug("yellow");
    public static final RegistryObject<CarpetBlock> LIME_CANVAS_RUG = registerCanvasRug("lime");
    public static final RegistryObject<CarpetBlock> PINK_CANVAS_RUG = registerCanvasRug("pink");
    public static final RegistryObject<CarpetBlock> GRAY_CANVAS_RUG = registerCanvasRug("gray");
    public static final RegistryObject<CarpetBlock> LIGHT_GRAY_CANVAS_RUG = registerCanvasRug("light_gray");
    public static final RegistryObject<CarpetBlock> CYAN_CANVAS_RUG = registerCanvasRug("cyan");
    public static final RegistryObject<CarpetBlock> PURPLE_CANVAS_RUG = registerCanvasRug("purple");
    public static final RegistryObject<CarpetBlock> BLUE_CANVAS_RUG = registerCanvasRug("blue");
    public static final RegistryObject<CarpetBlock> BROWN_CANVAS_RUG = registerCanvasRug("brown");
    public static final RegistryObject<CarpetBlock> GREEN_CANVAS_RUG = registerCanvasRug("green");
    public static final RegistryObject<CarpetBlock> RED_CANVAS_RUG = registerCanvasRug("red");
    public static final RegistryObject<CarpetBlock> BLACK_CANVAS_RUG = registerCanvasRug("black");

    private FARugs() {
    }

    public static void init() {
    }

    private static RegistryObject<CarpetBlock> registerCanvasRug(String color) {
        return FABlocks.BLOCKS.createBlock(color + "_canvas_rug",
                () -> new CarpetBlock(Block.Properties.copy(Blocks.MOSS_CARPET)),
                new Item.Properties());
    }

    public static Stream<RegistryObject<CarpetBlock>> canvasRugs() {
        return Stream.of(
                WHITE_CANVAS_RUG,
                ORANGE_CANVAS_RUG,
                MAGENTA_CANVAS_RUG,
                LIGHT_BLUE_CANVAS_RUG,
                YELLOW_CANVAS_RUG,
                LIME_CANVAS_RUG,
                PINK_CANVAS_RUG,
                GRAY_CANVAS_RUG,
                LIGHT_GRAY_CANVAS_RUG,
                CYAN_CANVAS_RUG,
                PURPLE_CANVAS_RUG,
                BLUE_CANVAS_RUG,
                BROWN_CANVAS_RUG,
                GREEN_CANVAS_RUG,
                RED_CANVAS_RUG,
                BLACK_CANVAS_RUG
        );
    }
}
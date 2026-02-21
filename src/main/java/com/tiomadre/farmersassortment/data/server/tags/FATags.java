package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class FATags {
    private FATags() {
    }

    public static final class Items {


        private Items() {
        }
    }

    public static final class Blocks {
        public static final TagKey<Block> CUTTING_BOARDS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "cutting_boards");
        public static final TagKey<Block> BUTCHER_BLOCK_CABINETS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "butcher_block_cabinets");
        public static final TagKey<Block> COOKING_POTS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "cooking_pots");
        public static final TagKey<Block> SKILLETS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "skillets");
        public static final TagKey<Block> FLOATING_COUNTERS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "floating_counters");
        public static final TagKey<Block> STOOLS = TagUtil.blockTag(FarmersAssortment.MOD_ID, "stools");
        private Blocks() {
        }
    }
}
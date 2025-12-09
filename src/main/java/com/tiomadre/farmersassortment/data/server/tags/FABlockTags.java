package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;

import static com.tiomadre.farmersassortment.data.server.tags.FATags.Blocks.*;
public class FABlockTags extends BlockTagsProvider {
    public FABlockTags(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), FarmersAssortment.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.tag(CUTTING_BOARDS)
                .add(FABlocks.SPRUCE_CUTTING_BOARD.get(), FABlocks.BIRCH_CUTTING_BOARD.get(),
                        FABlocks.JUNGLE_CUTTING_BOARD.get(), FABlocks.ACACIA_CUTTING_BOARD.get(), FABlocks.DARK_OAK_CUTTING_BOARD.get(),
                        FABlocks.MANGROVE_CUTTING_BOARD.get(), FABlocks.CHERRY_CUTTING_BOARD.get(), FABlocks.BAMBOO_CUTTING_BOARD.get(),
                        FABlocks.CRIMSON_CUTTING_BOARD.get(), FABlocks.WARPED_CUTTING_BOARD.get());

        this.tag(BUTCHER_BLOCK_CABINETS)
                .add(FABlocks.OAK_BUTCHER_BLOCK_CABINET.get(), FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.BIRCH_BUTCHER_BLOCK_CABINET.get(), FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.ACACIA_BUTCHER_BLOCK_CABINET.get(), FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET.get(), FABlocks.CHERRY_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET.get(), FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.WARPED_BUTCHER_BLOCK_CABINET.get());

        this.tag(COOKING_POTS)
                .add(FABlocks.COPPER_COOKING_POT.get(), FABlocks.GOLDEN_COOKING_POT.get(), FABlocks.ALABASTER_COOKING_POT.get(),
                        FABlocks.TERRACOTTA_COOKING_POT.get(), FABlocks.ALABASTER_STOVE.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(FABlocks.OAK_BUTCHER_BLOCK_CABINET.get(), FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.BIRCH_BUTCHER_BLOCK_CABINET.get(), FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.ACACIA_BUTCHER_BLOCK_CABINET.get(), FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET.get(), FABlocks.CHERRY_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET.get(), FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET.get(),
                        FABlocks.WARPED_BUTCHER_BLOCK_CABINET.get(), FABlocks.SPRUCE_CUTTING_BOARD.get(),
                        FABlocks.BIRCH_CUTTING_BOARD.get(), FABlocks.JUNGLE_CUTTING_BOARD.get(),
                        FABlocks.ACACIA_CUTTING_BOARD.get(), FABlocks.DARK_OAK_CUTTING_BOARD.get(),
                        FABlocks.MANGROVE_CUTTING_BOARD.get(), FABlocks.CHERRY_CUTTING_BOARD.get(),
                        FABlocks.BAMBOO_CUTTING_BOARD.get(), FABlocks.CRIMSON_CUTTING_BOARD.get(),
                        FABlocks.WARPED_CUTTING_BOARD.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FABlocks.COPPER_COOKING_POT.get(), FABlocks.GOLDEN_COOKING_POT.get(),
                        FABlocks.ALABASTER_COOKING_POT.get(), FABlocks.TERRACOTTA_COOKING_POT.get(),
                        FABlocks.ALABASTER_STOVE.get());

        this.tag(ModTags.HEAT_SOURCES)
                .add(FABlocks.ALABASTER_STOVE.get());
    }
}
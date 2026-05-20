package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;

import static com.tiomadre.farmersassortment.data.server.tags.FATags.Items.*;


public class FAItemTags extends ItemTagsProvider {
    public FAItemTags(GatherDataEvent event, FABlockTags blockTags) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ModTags.Items.KNIVES).add(FAItems.AMETHYST_KNIFE.get(), FAItems.CLAMSHELL_KNIFE.get(), FAItems.QUARTZ_KNIFE.get(), FAItems.ALABASTER_KNIFE.get());

        tag(CUTTING_BOARDS)
                .add(FABlocks.allCuttingBoards().map(block -> block.get().asItem()).toArray(Item[]::new));

        tag(COOKING_POTS)
                .add(FABlocks.allCookingPots().map(block -> block.get().asItem()).toArray(Item[]::new));
    }

}
package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import static com.tiomadre.farmersassortment.data.server.tags.FATags.Items.FARMERS_DELIGHT_KNIVES;
import static com.tiomadre.farmersassortment.data.server.tags.FATags.Items.KNIVES;

public class FAItemTags extends ItemTagsProvider {
    public FAItemTags(GatherDataEvent event, FABlockTags blockTags) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.tag(KNIVES).add(FAItems.AMETHYST_KNIFE.get(), FAItems.QUARTZ_KNIFE.get());
        this.tag(FARMERS_DELIGHT_KNIVES).addTag(KNIVES);
    }
}
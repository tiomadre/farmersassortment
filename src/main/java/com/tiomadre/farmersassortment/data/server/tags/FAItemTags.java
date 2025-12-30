package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;


public class FAItemTags extends ItemTagsProvider {
    public FAItemTags(GatherDataEvent event, FABlockTags blockTags) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
    tag(ModTags.KNIVES).add(FAItems.AMETHYST_KNIFE.get(), FAItems.CLAMSHELL_KNIFE.get(), FAItems.QUARTZ_KNIFE.get());

    }
}
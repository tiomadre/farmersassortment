package com.tiomadre.farmersassortment.data.server.tags;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
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
                .add(FABlocks.allCuttingBoards().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(BUTCHER_BLOCK_CABINETS)
                .add(FABlocks.allButcherBlockCabinets().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(COOKING_POTS)
                .add(FABlocks.allCookingPots().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(SKILLETS)
                .add(FAxCrabbersBlocks.skillets().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(FLOATING_COUNTERS)
                .add(FABlocks.floatingCounters().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(STOOLS)
                .add(FABlocks.stools().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(FABlocks.allButcherBlockCabinets().map(RegistryObject::get).toArray(Block[]::new))
                .add(FABlocks.allCuttingBoards().map(RegistryObject::get).toArray(Block[]::new))
                .add(FABlocks.floatingCounters().map(RegistryObject::get).toArray(Block[]::new))
                .add(FABlocks.stools().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FABlocks.allCookingPots().map(RegistryObject::get).toArray(Block[]::new))
                .add(FAxCrabbersBlocks.skillets().map(RegistryObject::get).toArray(Block[]::new))
                .add(FABlocks.ALABASTER_STOVE.get())
                .add(FAxForagersBlocks.diffusers().map(RegistryObject::get).toArray(Block[]::new));

        this.tag(ModTags.HEAT_SOURCES)
                .add(FABlocks.ALABASTER_STOVE.get());
    }
}
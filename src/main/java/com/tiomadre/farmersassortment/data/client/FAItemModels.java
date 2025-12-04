package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

public class FAItemModels extends ItemModelProvider {
    public FAItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersAssortment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Items
        handheldItem(FAItems.AMETHYST_KNIFE);
        handheldItem(FAItems.QUARTZ_KNIFE);
        //Block Items
        block(FABlocks.COPPER_COOKING_POT);
        block(FABlocks.GOLDEN_COOKING_POT);
        block(FABlocks.TERRACOTTA_COOKING_POT);
        FABlocks.cuttingBoards().forEach(this::block);
        FABlocks.butcherBlockCabinets().forEach(this::block);
    }

    private void handheldItem(RegistryObject<?> item) {
        String name = Objects.requireNonNull(item.getId()).getPath();
        withExistingParent(name, mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + name));
    }


    private void block(RegistryObject<? extends Block> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }
}
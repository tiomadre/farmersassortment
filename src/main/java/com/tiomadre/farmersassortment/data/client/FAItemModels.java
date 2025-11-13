package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
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
        cookingPotItem(FABlocks.COPPER_COOKING_POT);
        cookingPotItem(FABlocks.GOLDEN_COOKING_POT);
    }

    private void cookingPotItem(RegistryObject<? extends Block> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }
}

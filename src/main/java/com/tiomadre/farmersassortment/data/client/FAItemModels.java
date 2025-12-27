package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
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
        block(FABlocks.ALABASTER_COOKING_POT);
        terracottaCookingPot();
        block(FABlocks.ALABASTER_STOVE);
        FABlocks.allCuttingBoards().forEach(this::block);
        FABlocks.allButcherBlockCabinets().forEach(this::block);
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

    private void terracottaCookingPot() {
        ItemModelBuilder builder = withExistingParent("terracotta_cooking_pot", modLoc("block/terracotta_cooking_pot"));

        for (TerracottaCookingPotColor color : TerracottaCookingPotColor.values()) {
            if (color == TerracottaCookingPotColor.NONE) {
                continue;
            }

            builder.override()
                    .predicate(modLoc("color"), (float) color.ordinal())
                    .model(getModelForTerracottaColor(color))
                    .end();
        }
    }
    private ModelFile getModelForTerracottaColor(TerracottaCookingPotColor color) {
        return getExistingFile(modLoc("block/" + color.textureName()));
    }
}
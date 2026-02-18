package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FAItems;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.stream.Stream;

public class FAItemModels extends ItemModelProvider {
    public FAItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersAssortment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerKnives();
        registerCookingPots();
        registerStoves();
        registerCuttingBoards();
        registerButcherBlockCabinets();
        registerDiffusers();
        registerCrabTraps();
        registerSkillets();
        registerFloatingCounters();
    }
    private void registerFloatingCounters() {
        FABlocks.floatingCounters().forEach(this::block);
    }

    private void registerKnives() {
        Stream.of(FAItems.AMETHYST_KNIFE, FAItems.QUARTZ_KNIFE, FAItems.CLAMSHELL_KNIFE)
                .forEach(this::handheldItem);
    }

    private void registerCookingPots() {
        Stream.of(FABlocks.COPPER_COOKING_POT, FABlocks.GOLDEN_COOKING_POT, FABlocks.ALABASTER_COOKING_POT, FAxCrabbersBlocks.PEARLESCENT_COOKING_POT)
                .forEach(this::block);
        terracottaCookingPot();
    }

    private void registerStoves() {
        Stream.of(FABlocks.ALABASTER_STOVE).forEach(this::block);
    }

    private void registerCuttingBoards() {
        Stream.concat(
                Stream.concat(FABlocks.cuttingBoards(), FAxCrabbersBlocks.cuttingBoards()),
                FAxForagersBlocks.cuttingBoards()
        ).forEach(this::block);
    }

    private void registerButcherBlockCabinets() {
        Stream.concat(
                Stream.concat(FABlocks.butcherBlockCabinets(), FAxCrabbersBlocks.butcherBlockCabinets()),
                FAxForagersBlocks.butcherBlockCabinets()
        ).forEach(this::block);
    }

    private void registerDiffusers() {
        FAxForagersBlocks.diffusers().forEach(this::block);
    }

    private void registerCrabTraps() {
        FAxCrabbersBlocks.crabTraps().forEach(this::crabTrap);
    }

    private void registerSkillets() {
        FAxCrabbersBlocks.skillets().forEach(this::skillet);
    }

    private void handheldItem(RegistryObject<?> item) {
        String name = Objects.requireNonNull(item.getId()).getPath();
        withExistingParent(name, mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + name));
    }

    private void crabTrap(RegistryObject<? extends Block> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }

    private void block(RegistryObject<? extends Block> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }

    private void skillet(RegistryObject<? extends Block> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("farmersdelight", "item/skillet")))
                .texture("top", modLoc("block/" + name + "_top"))
                .texture("side", modLoc("block/" + name + "_side"))
                .texture("bottom", modLoc("block/" + name + "_bottom"));
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
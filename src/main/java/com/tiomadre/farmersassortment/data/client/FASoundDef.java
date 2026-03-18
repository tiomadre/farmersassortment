package com.tiomadre.farmersassortment.data.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FASoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class FASoundDef extends SoundDefinitionsProvider {
    public FASoundDef(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersAssortment.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        add(FASoundEvents.ALABASTER_BREAK, definition().with(
                sound("minecraft:block.calcite.break").volume(0.5).pitch(0.94F),
                sound("minecraft:block.amethyst_block.break").volume(0.32F).pitch(1.12F)
        ));
        add(FASoundEvents.ALABASTER_STEP, definition().with(
                sound("minecraft:block.calcite.step").volume(0.5).pitch(0.94F),
                sound("minecraft:block.amethyst_block.step").volume(0.26F).pitch(1.08F)
        ));
        add(FASoundEvents.ALABASTER_PLACE, definition().with(
                sound("minecraft:block.calcite.place").volume(0.5).pitch(0.94F),
                sound("minecraft:block.amethyst_block.place").volume(0.26F).pitch(1.08F)
        ));
        add(FASoundEvents.ALABASTER_HIT, definition().with(
                sound("minecraft:block.calcite.hit").volume(0.5).pitch(0.94F),
                sound("minecraft:block.amethyst_block.hit").volume(0.26F).pitch(1.08F)
        ));
        add(FASoundEvents.ALABASTER_FALL, definition().with(
                sound("minecraft:block.calcite.fall").volume(0.5).pitch(0.94F),
                sound("minecraft:block.amethyst_block.fall").volume(0.26F).pitch(1.08F)
        ));
    }
}
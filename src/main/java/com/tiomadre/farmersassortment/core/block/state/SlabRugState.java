package com.tiomadre.farmersassortment.core.block.state;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class SlabRugState {
    public static final EnumProperty<StoolRugType> RUG = EnumProperty.create("rug", StoolRugType.class);

    private SlabRugState() {
    }
}
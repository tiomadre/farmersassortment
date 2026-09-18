package com.tiomadre.farmersassortment.core.block.state;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum DividerState implements StringRepresentable {
    CLOSED("closed"),
    HALF_OPEN("half_open"),
    OPEN("open");

    private final String name;

    DividerState(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
package com.tiomadre.farmersassortment.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FAConfig {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.DoubleValue ALABASTER_STOVE_COOKING_BOOST;
    public static final ForgeConfigSpec.BooleanValue ALABASTER_STOVE_ALABASTER_POTS_ONLY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("general");

        ALABASTER_STOVE_COOKING_BOOST = builder
                .comment("Additional cooking progress granted each tick when a pot sits on a lit Alabaster Stove.",
                        "Set to 0 to disable the speed boost entirely."
                )
                .defineInRange("alabasterStoveCookingBoost", 0.25D, 0.0D, 100.0D);

        ALABASTER_STOVE_ALABASTER_POTS_ONLY = builder
                .comment("When true, only the Alabaster Cooking Pot receives the stove's speed bonus."
                        ,"When false, any Cooking Pot will benefit.")
                .define("alabasterStoveAlabasterPotsOnly", true);

        builder.pop();

        COMMON_SPEC = builder.build();
    }
}
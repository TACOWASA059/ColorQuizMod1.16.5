package com.github.tacowasa059.colorquizmod.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ColorConfig {
    public static final ForgeConfigSpec.Builder builder =new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec spec;

    public static  ForgeConfigSpec.ConfigValue<Integer> TargetColor;
    public static  ForgeConfigSpec.ConfigValue<Float> R;
    public static  ForgeConfigSpec.ConfigValue<Float> G;
    public static  ForgeConfigSpec.ConfigValue<Float> B;

    static {
        builder.push("Config for ColorQuizMod");
        TargetColor= builder.comment("目標色").define("TargetColor",0xFF234567);
        R= builder.comment("赤色").define("Red",0.5f);
        G= builder.comment("緑色").define("Green",0.5f);
        B= builder.comment("青色").define("Blue",0.5f);
        builder.pop();
        spec= builder.build();
    }
}

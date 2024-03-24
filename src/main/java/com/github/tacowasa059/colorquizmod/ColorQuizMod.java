package com.github.tacowasa059.colorquizmod;

import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import com.github.tacowasa059.colorquizmod.Network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(ColorQuizMod.MOD_ID)
public class ColorQuizMod {
    public static final String MOD_ID="colorquizmod";
    public static Map<UUID,Integer> target_color_list=new HashMap<>();

    public static Map<UUID,Integer> color_list=new HashMap<>();

    public static int TargetColor;
    public static float R;
    public static float G;
    public static float B;

    public static boolean isServerTesting=false;


    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public ColorQuizMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ColorConfig.spec,ColorQuizMod.MOD_ID+".toml");
        MinecraftForge.EVENT_BUS.register(this);
        PacketHandler.registerPackets();
    }
    private void doClientStuff(final FMLClientSetupEvent event) {
        TargetColor=ColorConfig.TargetColor.get();
        R=ColorConfig.R.get();
        G=ColorConfig.G.get();
        B=ColorConfig.B.get();
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().debug);
    }

}

package com.github.tacowasa059.colorquizmod.Network;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.Network.packets.ColorPacket;
import com.github.tacowasa059.colorquizmod.Network.packets.PlayerDataPacket;
import com.github.tacowasa059.colorquizmod.Network.packets.PlayersListDataPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ColorQuizMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, ColorPacket.class, ColorPacket::encode, ColorPacket::decode, ColorPacket::handle);
        INSTANCE.registerMessage(id++, PlayersListDataPacket.class, PlayersListDataPacket::encode, PlayersListDataPacket::decode, PlayersListDataPacket::handle);
        INSTANCE.registerMessage(id++, PlayerDataPacket.class, PlayerDataPacket::encode, PlayerDataPacket::decode, PlayerDataPacket::handle);

    }
}


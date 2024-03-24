package com.github.tacowasa059.colorquizmod.Network.packets;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

//client to server
public class PlayerDataPacket {
    private UUID playeruuid;
    private int target_color;
    private int color;

    public PlayerDataPacket(UUID uuid, int target_color, int color) {
        this.playeruuid=uuid;
        this.target_color=target_color;
        this.color=color;
    }

    public static void encode(PlayerDataPacket msg, PacketBuffer buffer) {
        buffer.writeUniqueId(msg.playeruuid).writeInt(msg.target_color).writeInt(msg.color);
    }

    public static PlayerDataPacket decode(PacketBuffer buffer) {
        return new PlayerDataPacket(buffer.readUniqueId(), buffer.readInt(), buffer.readInt());
    }

    public static void handle(PlayerDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerDataPacket.handlePacket(msg, contextSupplier);
        });
        contextSupplier.get().setPacketHandled(true);
    }

    private static void handlePacket(PlayerDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ColorQuizMod.target_color_list.put(msg.playeruuid,msg.target_color);
        ColorQuizMod.color_list.put(msg.playeruuid,msg.color);
    }
}

package com.github.tacowasa059.colorquizmod.Network.packets;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

//server to client
public class ColorPacket {
    private int r;
    private int g;
    private int b;

    public ColorPacket(int r,int g,int b) {
        this.r=r;
        this.g=g;
        this.b=b;
    }

    public static void encode(ColorPacket msg, PacketBuffer buffer) {
        buffer.writeInt(msg.r).writeInt(msg.g).writeInt(msg.b);
    }

    public static ColorPacket decode(PacketBuffer buffer) {
        return new ColorPacket(buffer.readInt(),buffer.readInt(),buffer.readInt());
    }
    public static void handle(ColorPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT.CLIENT, () -> () -> ColorPacket.handlePacket(msg, contextSupplier));
        });
        contextSupplier.get().setPacketHandled(true);
    }
    private static void handlePacket(ColorPacket msg, Supplier<NetworkEvent.Context> contextSupplier){
        ColorQuizMod.TargetColor=(255 << 24) +(msg.r << 16) + (msg.g << 8) + msg.b;
        ColorQuizMod.isServerTesting=true;
        Minecraft.getInstance().currentScreen=null;
    }
}

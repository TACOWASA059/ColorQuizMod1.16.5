package com.github.tacowasa059.colorquizmod.Network.packets;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

//server to client
public class PlayersListDataPacket {
    private Map<UUID,Integer> target_color_list;
    private Map<UUID,Integer> color_list;

    public PlayersListDataPacket(Map<UUID,Integer> target_color_list,Map<UUID,Integer> color_list) {
        this.target_color_list=target_color_list;
        this.color_list=color_list;
    }

    public static void encode(PlayersListDataPacket msg, PacketBuffer buffer) {
        // プレイヤーリストのサイズを最初にエンコード
        buffer.writeInt(msg.target_color_list.size());
        // 次に、各UUIDとIntegerのペアをエンコード
        msg.target_color_list.forEach((uuid, value) -> {
            buffer.writeUniqueId(uuid);
            buffer.writeInt(value);
        });
        buffer.writeInt(msg.color_list.size());
        msg.color_list.forEach((uuid, value) -> {
            buffer.writeUniqueId(uuid);
            buffer.writeInt(value);
        });
    }

    public static PlayersListDataPacket decode(PacketBuffer buffer) {
        int size = buffer.readInt(); // リストのサイズを読み取り
        Map<UUID, Integer> target_color_list = new HashMap<>();
        // サイズの数だけループして、UUIDとIntegerのペアを読み取る
        for (int i = 0; i < size; i++) {
            UUID uuid = buffer.readUniqueId();
            Integer value = buffer.readInt();
            target_color_list.put(uuid, value);
        }

        Map<UUID, Integer> color_list = new HashMap<>();
        size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            UUID uuid = buffer.readUniqueId();
            Integer value = buffer.readInt();
            color_list.put(uuid, value);
        }
        return new PlayersListDataPacket(target_color_list,color_list);
    }
    public static void handle(PlayersListDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT.CLIENT, () -> () -> PlayersListDataPacket.handlePacket(msg, contextSupplier));
        });
        contextSupplier.get().setPacketHandled(true);
    }
    private static void handlePacket(PlayersListDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ColorQuizMod.color_list=msg.color_list;
        ColorQuizMod.target_color_list=msg.target_color_list;
    }
}

package com.github.tacowasa059.colorquizmod.command;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.Network.PacketHandler;
import com.github.tacowasa059.colorquizmod.Network.packets.ColorPacket;
import com.github.tacowasa059.colorquizmod.Network.packets.PlayersListDataPacket;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ColorQuizMod.MOD_ID)
public class CommandRegister {
    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("colortest")
                .requires(context -> context.hasPermissionLevel(2))
                .then(Commands.literal("test")
                        .then(Commands.literal("random")
                                .executes(context -> {
                                    Random random =new Random();
                                    int red=random.nextInt(255);
                                    int green=random.nextInt(255);
                                    int blue=random.nextInt(255);
                                    ColorPacket packet=new ColorPacket(red,green,blue);
                                    PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
                                    context.getSource().sendFeedback(new StringTextComponent("共通のランダムな値を送信しました。"),false);
                                    for (ServerPlayerEntity player : context.getSource().getServer().getPlayerList().getPlayers()) {
                                        player.sendMessage(new StringTextComponent(TextFormatting.AQUA+"サーバーからテストが送信されました。チャット欄を開いて送信してください。"), UUID.randomUUID());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("color")
                                .then(Commands.argument("Red", IntegerArgumentType.integer()).then(Commands.argument("Green", IntegerArgumentType.integer()).then(Commands.argument("Blue", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int red=IntegerArgumentType.getInteger(context,"Red");
                                        int green=IntegerArgumentType.getInteger(context,"Green");
                                        int blue=IntegerArgumentType.getInteger(context,"Blue");
                                        if(red<0||red>255||green<0||blue<0||green>255||blue>255){
                                            context.getSource().sendFeedback(new StringTextComponent("色の値は0から255にしてください。"),false);
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        ColorPacket packet=new ColorPacket(red,green,blue);
                                        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
                                        context.getSource().sendFeedback(new StringTextComponent("カラー"+red+" "+green+" "+blue+"を送信しました。"),false);
                                        return Command.SINGLE_SUCCESS;
                                    })))
                                )
                                )
                ).then(Commands.literal("visualize")
                        .executes(context -> {
                            PlayersListDataPacket packet=new PlayersListDataPacket(ColorQuizMod.target_color_list,ColorQuizMod.color_list);
                            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
                            context.getSource().sendFeedback(new StringTextComponent("各々の手持ちの色が変更されました"),false);
                            return Command.SINGLE_SUCCESS;
                        })
                ).then(Commands.literal("getColor")
                        .then(Commands.argument("Players", EntityArgument.players())
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"Players");
                            Scoreboard scoreboard=context.getSource().getServer().getScoreboard();
                            ScoreObjective objective;
                            try{
                                objective=scoreboard.addObjective("result", ScoreCriteria.DUMMY,new StringTextComponent("色の距離"), ScoreCriteria.RenderType.INTEGER);
                            }catch(IllegalArgumentException e){objective=scoreboard.getObjective("result");
                            }

                            for(ServerPlayerEntity playerEntity: playerEntitylist) {
                                UUID uuid=playerEntity.getUniqueID();
                                if(ColorQuizMod.target_color_list.containsKey(uuid)&&ColorQuizMod.color_list.containsKey(uuid)){
                                    int target_color=ColorQuizMod.target_color_list.get(uuid);
                                    int target_red = ((target_color >> 16) & 0xFF);   // 赤成分を抽出
                                    int target_green = ((target_color >> 8) & 0xFF);  // 緑成分を抽出
                                    int target_blue = (target_color & 0xFF);
                                    source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+" 目標色:"+target_red+" "+target_green+" "+target_blue),false);
                                    int color=ColorQuizMod.color_list.get(uuid);
                                    int red = ((color >> 16) & 0xFF);   // 赤成分を抽出
                                    int green = ((color >> 8) & 0xFF);  // 緑成分を抽出
                                    int blue = (color & 0xFF);
                                    source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+" 結果:"+red+" "+green+" "+blue),false);
                                    int distance=Math.abs(target_green-green)+Math.abs(target_blue-blue)+Math.abs(target_red-red);
                                    Score score=scoreboard.getOrCreateScore(playerEntity.getName().getString(),objective);
                                    score.setScorePoints(distance);
                                }
                            }
                            source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+"スコアボードへの反映が完了しました"),false);
                            return Command.SINGLE_SUCCESS;
                        }))
                ).then(Commands.literal("feedback")
                        .then(Commands.argument("Players", EntityArgument.players())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"Players");
                                    for(ServerPlayerEntity playerEntity: playerEntitylist) {
                                        UUID uuid=playerEntity.getUniqueID();
                                        if(ColorQuizMod.target_color_list.containsKey(uuid)&&ColorQuizMod.color_list.containsKey(uuid)){
                                            int target_color=ColorQuizMod.target_color_list.get(uuid);
                                            int target_red = ((target_color >> 16) & 0xFF);   // 赤成分を抽出
                                            int target_green = ((target_color >> 8) & 0xFF);  // 緑成分を抽出
                                            int target_blue = (target_color & 0xFF);
                                            playerEntity.sendMessage(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+" 目標色:"+target_red+" "+target_green+" "+target_blue),UUID.randomUUID());
                                            int color=ColorQuizMod.color_list.get(uuid);
                                            int red = ((color >> 16) & 0xFF);   // 赤成分を抽出
                                            int green = ((color >> 8) & 0xFF);  // 緑成分を抽出
                                            int blue = (color & 0xFF);
                                            playerEntity.sendMessage(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+" 結果:"+red+" "+green+" "+blue),UUID.randomUUID());
                                        }
                                    }
                                    source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+"各々に結果を送りました。"),false);
                                    return Command.SINGLE_SUCCESS;
                                }))
                )
                ;
        dispatcher.register(builder);
    }

}

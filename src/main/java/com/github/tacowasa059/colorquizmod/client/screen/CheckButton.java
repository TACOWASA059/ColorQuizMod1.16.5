package com.github.tacowasa059.colorquizmod.client.screen;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.Network.PacketHandler;
import com.github.tacowasa059.colorquizmod.Network.packets.PlayerDataPacket;
import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import com.github.tacowasa059.colorquizmod.mixin.MixinChatScreen;
import com.github.tacowasa059.colorquizmod.mixin.MixinScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.transformer.Config;

import java.util.Random;

public class CheckButton extends AbstractButton {
    ChatScreenSlider[] sliders;
    public CheckButton(ChatScreenSlider[] sliders, int p_i232251_1_, int p_i232251_2_, int p_i232251_3_, int p_i232251_4_, ITextComponent p_i232251_5_) {
        super(p_i232251_1_, p_i232251_2_, p_i232251_3_, p_i232251_4_, p_i232251_5_);
        this.sliders=sliders;
    }

    @Override
    public void onPress() {
        int r=this.sliders[0].getSliderValue();
        int g=this.sliders[1].getSliderValue();
        int b=this.sliders[2].getSliderValue();
        int color=getColor(r,g,b);
        int target_color= ColorQuizMod.TargetColor;
        if(!ColorQuizMod.isServerTesting){
            if(color==target_color){
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.GREEN+(TextFormatting.BOLD+"正解!")), Minecraft.getInstance().player.getUniqueID());
            }else{
                Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED+(TextFormatting.BOLD+"不正解")), Minecraft.getInstance().player.getUniqueID());
                int[] colors=getColor(target_color);
                Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED+(TextFormatting.BOLD+"答え:")+TextFormatting.AQUA+colors[0]+" "+colors[1]+" "+colors[2]), Minecraft.getInstance().player.getUniqueID());
            }
        }else{
            PlayerDataPacket packet=new PlayerDataPacket(Minecraft.getInstance().player.getUniqueID(),target_color,color);
            PacketHandler.INSTANCE.sendToServer(packet);
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.GREEN+(TextFormatting.BOLD+"結果を送信しました!")), Minecraft.getInstance().player.getUniqueID());
            Minecraft.getInstance().currentScreen=null;
            ColorQuizMod.isServerTesting=false;
        }

    }
    private int getColor(int r, int g, int b) {
        return (255 << 24) +(r << 16) + (g << 8) + b;
    }
    private int[] getColor(int color) {
        int red = (color >> 16) & 0xFF;   // 赤成分を抽出
        int green = (color >> 8) & 0xFF;  // 緑成分を抽出
        int blue = color & 0xFF;          // 青成分を抽出

        int[] colors=new int[]{red,green,blue};
        return colors;
    }
}

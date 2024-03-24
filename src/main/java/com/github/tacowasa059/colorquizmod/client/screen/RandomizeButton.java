package com.github.tacowasa059.colorquizmod.client.screen;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import com.github.tacowasa059.colorquizmod.mixin.MixinChatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public class RandomizeButton extends AbstractButton {
    public RandomizeButton(int p_i232251_1_, int p_i232251_2_, int p_i232251_3_, int p_i232251_4_, ITextComponent p_i232251_5_) {
        super(p_i232251_1_, p_i232251_2_, p_i232251_3_, p_i232251_4_, p_i232251_5_);
    }

    @Override
    public void onPress() {
        Random random=new Random();
        int r=random.nextInt(255);
        int g=random.nextInt(255);
        int b=random.nextInt(255);
        int color=getColor(r,g,b);
        ColorQuizMod.TargetColor=color;
        PlayerEntity player= Minecraft.getInstance().player;
        if(player!=null)ColorQuizMod.target_color_list.put(player.getUniqueID(), ColorQuizMod.TargetColor);
    }
    private int getColor(int r, int g, int b) {
        return (255 << 24) +(r << 16) + (g << 8) + b;
    }
}

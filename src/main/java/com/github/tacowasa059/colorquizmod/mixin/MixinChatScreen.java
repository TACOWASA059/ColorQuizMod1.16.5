package com.github.tacowasa059.colorquizmod.mixin;


import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import com.github.tacowasa059.colorquizmod.client.screen.ChatScreenButton;
import com.github.tacowasa059.colorquizmod.client.screen.ChatScreenSlider;
import com.github.tacowasa059.colorquizmod.client.screen.CheckButton;
import com.github.tacowasa059.colorquizmod.client.screen.RandomizeButton;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen{
    public ChatScreenSlider[] slider=new ChatScreenSlider[3];
    private ChatScreenButton[] buttons=new ChatScreenButton[6];
    private RandomizeButton randomizeButton;
    private CheckButton checkButton;

    @Inject(method = "init()V", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        PlayerEntity player=Minecraft.getInstance().player;
        ColorQuizMod.target_color_list.put(player.getUniqueID(), ColorQuizMod.TargetColor);
        ColorQuizMod.color_list.put(player.getUniqueID(), getColor((int)(ColorQuizMod.R*255.0),(int)(ColorQuizMod.G*255.0),(int)(ColorQuizMod.B*255.0)));

        ChatScreen chatScreen = (ChatScreen)(Object)this;
        this.slider[2] = new ChatScreenSlider(
                chatScreen.width-120, // X座標
                chatScreen.height - 70, // Y座標
                110, // 幅
                20, // 高さ
                new StringTextComponent(""), // タイトル
                ColorQuizMod.B, // 初期値
                0,0, 1
        );
        this.slider[1] = new ChatScreenSlider(
                chatScreen.width-120, // X座標
                chatScreen.height - 110, // Y座標
                110, // 幅
                20, // 高さ
                new StringTextComponent(""), // タイトル
                ColorQuizMod.G, // 初期値
                0,1,0
        );
        this.slider[0] = new ChatScreenSlider(
                chatScreen.width-120, // X座標
                chatScreen.height - 150, // Y座標
                110, // 幅
                20, // 高さ
                new StringTextComponent(""), // タイトル
                ColorQuizMod.R, // 初期値
                1,0,0
        );
        for(int i=0;i<3;i++){
            this.buttons[2*i]=new ChatScreenButton(this.slider[i],true,chatScreen.width-40,chatScreen.height-2 - 160+40*i,10,10,new StringTextComponent("+"));
            this.buttons[2*i+1]=new ChatScreenButton(this.slider[i],false,chatScreen.width-50,chatScreen.height-2 - 160+40*i,10,10,new StringTextComponent("-"));

        }
        if(!ColorQuizMod.isServerTesting){
            this.randomizeButton=new RandomizeButton(chatScreen.width-110,chatScreen.height - 40,50,20,new StringTextComponent("ランダム"));
            this.checkButton=new CheckButton(this.slider,chatScreen.width-60,chatScreen.height - 40,50,20,new StringTextComponent("判定"));
        }else{
            this.checkButton=new CheckButton(this.slider,chatScreen.width-60,chatScreen.height - 40,50,20,new StringTextComponent("送信"));
        }

        MixinScreen mixinScreen = (MixinScreen) chatScreen;
        mixinScreen.InvokeAddButton(this.slider[0]);
        mixinScreen.InvokeAddButton(this.slider[1]);
        mixinScreen.InvokeAddButton(this.slider[2]);
    }
    @Inject(method="onClose",at=@At("HEAD"))
    public void onClose(CallbackInfo ci) {
        this.slider[0].update();
        this.slider[1].update();
        this.slider[2].update();
        ColorConfig.TargetColor.set(ColorQuizMod.TargetColor);
        ColorConfig.R.set(ColorQuizMod.R);
        ColorConfig.G.set(ColorQuizMod.G);
        ColorConfig.B.set(ColorQuizMod.B);
        ColorConfig.spec.save();
    }
    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_, CallbackInfo ci) {
        MixinScreen mixinScreen = (MixinScreen) (Object)this;
        ChatScreen chatScreen =(ChatScreen)(Object)this;
        chatScreen.fill(p_230430_1_, chatScreen.width-145, chatScreen.height - 235, chatScreen.width-5, chatScreen.height - 20, 0x6F000000);
        //target
        chatScreen.fill(p_230430_1_, chatScreen.width-120, chatScreen.height - 215, chatScreen.width-70, chatScreen.height - 175, ColorQuizMod.TargetColor);
        //color
        chatScreen.fill(p_230430_1_, chatScreen.width-60, chatScreen.height - 215, chatScreen.width-10, chatScreen.height - 175, getSliderColor());

        mixinScreen.getFont().drawString(p_230430_1_, TextFormatting.BOLD+"R:赤", chatScreen.width-140, chatScreen.height - 140 - (((MixinScreen) this).getFont().FONT_HEIGHT / 2), 0xFFFF0000);
        mixinScreen.getFont().drawString(p_230430_1_, TextFormatting.BOLD+"G:緑", chatScreen.width-140, chatScreen.height - 100 - (((MixinScreen) this).getFont().FONT_HEIGHT / 2), 0xFF00FF00);
        mixinScreen.getFont().drawString(p_230430_1_, TextFormatting.BOLD+"B:青", chatScreen.width-140, chatScreen.height - 60 - (((MixinScreen) this).getFont().FONT_HEIGHT / 2), 0xFF0000FF);

        mixinScreen.getFont().drawString(p_230430_1_, String.valueOf(this.slider[2].getSliderValue()),
                chatScreen.width-65-mixinScreen.getFont().getStringWidth(String.valueOf(this.slider[2].getSliderValue()))/2, chatScreen.height - 80, 0xFFFFFFFF);
        mixinScreen.getFont().drawString(p_230430_1_, String.valueOf(this.slider[1].getSliderValue()),
                chatScreen.width-65-mixinScreen.getFont().getStringWidth(String.valueOf(this.slider[1].getSliderValue()))/2, chatScreen.height - 120, 0xFFFFFFFF);
        mixinScreen.getFont().drawString(p_230430_1_, String.valueOf(this.slider[0].getSliderValue()),
                chatScreen.width-65-mixinScreen.getFont().getStringWidth(String.valueOf(this.slider[0].getSliderValue()))/2, chatScreen.height - 160, 0xFFFFFFFF);
        for(ChatScreenSlider slider:this.slider){
            mixinScreen.InvokeAddListener(slider);
            slider.renderBg(p_230430_1_, Minecraft.getInstance(),p_230430_2_,p_230430_3_);
        }
        for(ChatScreenButton button:this.buttons){
            mixinScreen.InvokeAddListener(button);
            button.render(p_230430_1_,p_230430_2_,p_230430_3_,p_230430_4_);
        }
        if(randomizeButton!=null){
            mixinScreen.InvokeAddListener(randomizeButton);
            randomizeButton.render(p_230430_1_,p_230430_2_,p_230430_3_,p_230430_4_);
        }
        mixinScreen.InvokeAddListener(checkButton);
        checkButton.render(p_230430_1_,p_230430_2_,p_230430_3_,p_230430_4_);
    }
    private int getColor(int r, int g, int b) {
        return (255 << 24) +(r << 16) + (g << 8) + b;
    }
    private int getSliderColor() {
        int r=slider[0].getSliderValue();
        int g=slider[1].getSliderValue();
        int b=slider[2].getSliderValue();
        return getColor(r,g,b);
    }
}

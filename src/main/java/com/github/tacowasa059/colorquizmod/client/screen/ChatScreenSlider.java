package com.github.tacowasa059.colorquizmod.client.screen;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class ChatScreenSlider extends AbstractSlider {
    int red;
    int green;
    int blue;

    public ChatScreenSlider(int x, int y, int width, int height, ITextComponent title, double value,int red,int green,int blue) {
        super(x, y, width, height, title, value);
        this.red=red;
        this.green=green;
        this.blue=blue;
    }
    @Override
    public void renderBg(MatrixStack p_230441_1_, Minecraft p_230441_2_, int p_230441_3_, int p_230441_4_) {
        p_230441_2_.getTextureManager().bindTexture(WIDGETS_LOCATION);
        int lvt_5_1_ = (this.isHovered() ? 2 : 1) * 20;
        RenderSystem.color4f(red, green, blue, 1.0F); // 赤色
        RenderSystem.disableAlphaTest();
        this.blit(p_230441_1_, this.x + (int)(this.sliderValue * (double)(this.width - 8)), this.y, 0, 46 + lvt_5_1_, 4, 20);
        RenderSystem.enableAlphaTest();
        this.blit(p_230441_1_, this.x + (int)(this.sliderValue * (double)(this.width - 8)) + 4, this.y, 196, 46 + lvt_5_1_, 4, 20);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
        double newSliderValue = (mouseX - (this.x + 4)) / (double)(this.width - 8);
        newSliderValue = MathHelper.clamp(newSliderValue, 0.0, 1.0); // 新しい値を0.0と1.0の間に制限
        if (newSliderValue != this.sliderValue) {
            this.sliderValue = newSliderValue;
            this.update();
        }
    }

    @Override
    protected void func_230979_b_() {
        update();}
    @Override
    protected void func_230972_a_() {}
    public int getSliderValue(){
        return (int)(this.sliderValue*255);
    }
    public void setSliderValue(int color){
        if(color>255)this.sliderValue=1.0f;
        else if(color<0)this.sliderValue=0.0f;
        else this.sliderValue=(float)(color)/255.0;
        update();
    }

    public void update(){
        if(red==1){
            ColorQuizMod.R=(float) this.sliderValue;
        }else if(green==1){
            ColorQuizMod.G=(float) this.sliderValue;
        }else{
            ColorQuizMod.B=(float) this.sliderValue;
        }
        PlayerEntity player=Minecraft.getInstance().player;
        if(player!=null) ColorQuizMod.color_list.put(player.getUniqueID(), getColor((int)(ColorQuizMod.R*255.0),(int)(ColorQuizMod.G*255.0),(int)(ColorQuizMod.B*255.0)));
    }
    private static int getColor(int r, int g, int b) {
        return (255 << 24) +(r << 16) + (g << 8) + b;
    }
}


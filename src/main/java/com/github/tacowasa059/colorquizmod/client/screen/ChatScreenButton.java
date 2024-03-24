package com.github.tacowasa059.colorquizmod.client.screen;

import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.ITextComponent;

public class ChatScreenButton extends AbstractButton {
    private ChatScreenSlider slider;
    private boolean isIncrement;

    public ChatScreenButton(ChatScreenSlider slider,boolean isIncrement,int p_i232251_1_, int p_i232251_2_, int p_i232251_3_, int p_i232251_4_, ITextComponent p_i232251_5_) {
        super(p_i232251_1_, p_i232251_2_, p_i232251_3_, p_i232251_4_, p_i232251_5_);
        this.slider=slider;
        this.isIncrement=isIncrement;
    }

    @Override
    public void onPress() {
        if(isIncrement)slider.setSliderValue(slider.getSliderValue()+1);
        else slider.setSliderValue(slider.getSliderValue()-1);
    }
}

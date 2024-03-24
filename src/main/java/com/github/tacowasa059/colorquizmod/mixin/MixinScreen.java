package com.github.tacowasa059.colorquizmod.mixin;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface MixinScreen {
    @Invoker("addButton")
    <T extends Widget> T InvokeAddButton(T widget);
    @Invoker("addListener")
    <T extends IGuiEventListener> T InvokeAddListener(T p_230481_1_);
    @Accessor("font")
    FontRenderer getFont();
}
package com.github.tacowasa059.colorquizmod.mixin;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.client.render.ModRenderType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemLayer.class)
public abstract class MixinHeldItemLayer <T extends LivingEntity, M extends EntityModel<T> & IHasArm>{
    @Shadow
    abstract void func_229135_a_(LivingEntity p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack p_229135_5_, IRenderTypeBuffer p_229135_6_, int p_229135_7_);
    @Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",at=@At("HEAD"),cancellable = true)
    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_, CallbackInfo ci) {
        HeldItemLayer heldItemLayer=(HeldItemLayer) (Object)this;
        boolean lvt_11_1_ = p_225628_4_.getPrimaryHand() == HandSide.RIGHT;
        ItemStack lvt_12_1_ = lvt_11_1_ ? p_225628_4_.getHeldItemOffhand() : p_225628_4_.getHeldItemMainhand();
        ItemStack lvt_13_1_ = lvt_11_1_ ? p_225628_4_.getHeldItemMainhand() : p_225628_4_.getHeldItemOffhand();
        if (lvt_12_1_.isEmpty() || lvt_13_1_.isEmpty()) {
            p_225628_1_.push();
            if (heldItemLayer.getEntityModel().isChild) {
                p_225628_1_.translate(0.0, 0.75, 0.0);
                p_225628_1_.scale(0.5F, 0.5F, 0.5F);
            }

            this.func_229135_a_(p_225628_4_, lvt_13_1_, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, p_225628_1_, p_225628_2_, p_225628_3_);
            this.func_229135_a_(p_225628_4_, lvt_12_1_, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, p_225628_1_, p_225628_2_, p_225628_3_);
            p_225628_1_.pop();
            ci.cancel();
        }
    }
    @Inject(method = "func_229135_a_",at=@At("HEAD"),cancellable = true)
    private void func_229135_a_(LivingEntity p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_229135_7_, CallbackInfo ci) {
        if (p_229135_2_.isEmpty()) {
            matrixStack.push();
            HeldItemLayer heldItemLayer=(HeldItemLayer) (Object)this;
            ((IHasArm)heldItemLayer.getEntityModel()).translateHand(p_229135_4_, matrixStack);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
            boolean lvt_8_1_ = p_229135_4_ == HandSide.LEFT;
            matrixStack.translate((double)((float)(lvt_8_1_ ? -1 : 1) / 16.0F), 0.125, -0.625);

            IVertexBuilder builder=buffer.getBuffer(ModRenderType.OVERLAY_FACE);
            Matrix4f matrix4f=matrixStack.getLast().getMatrix();
            matrixStack.translate(0,-0.25f,0);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0F));

            if(ColorQuizMod.color_list.containsKey(p_229135_1_.getUniqueID())&&lvt_8_1_){
                int color=ColorQuizMod.color_list.get(p_229135_1_.getUniqueID());
                renderColor(builder, matrix4f, color);
            }
            else if(ColorQuizMod.target_color_list.containsKey(p_229135_1_.getUniqueID())&&!lvt_8_1_){
                int color=ColorQuizMod.target_color_list.get(p_229135_1_.getUniqueID());
                renderColor(builder, matrix4f, color);
            }


            matrixStack.pop();
            ci.cancel();
        }
    }

    private static void renderColor(IVertexBuilder builder, Matrix4f matrix4f, int color) {
        float red = (float)((color >> 16) & 0xFF)/255;   // 赤成分を抽出
        float green = (float)((color >> 8) & 0xFF)/255;  // 緑成分を抽出
        float blue = (float)(color & 0xFF)/255;          // 青成分を抽出

        builder.pos(matrix4f,0.0f,0.2f,-0.2f).color(red,green,blue,1f).endVertex();
        builder.pos(matrix4f,+0.0f,0.2f,0.2f).color(red,green,blue,1f).endVertex();
        builder.pos(matrix4f,+0.0f,-0.2f,0.2f).color(red,green,blue,1f).endVertex();
        builder.pos(matrix4f,-0.0f,-0.2f,-0.2f).color(red,green,blue,1f).endVertex();
    }
}

package com.github.tacowasa059.colorquizmod.mixin;

import com.github.tacowasa059.colorquizmod.ColorQuizMod;
import com.github.tacowasa059.colorquizmod.client.ColorConfig;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedModel.class)
public class MixinBipedModel<T extends LivingEntity> {
    @Shadow
    public BipedModel.ArmPose leftArmPose;
    @Shadow
    public BipedModel.ArmPose rightArmPose;
    @Inject(method = "func_241654_b_",at=@At("HEAD"),cancellable = true)
    private void func_241654_b_(T p_241654_1_, CallbackInfo ci) {
        BipedModel<T> bipedModel = (BipedModel<T>) (Object) this;
        if(ColorQuizMod.target_color_list.containsKey(p_241654_1_.getUniqueID())&&rightArmPose.equals(BipedModel.ArmPose.EMPTY)){
            bipedModel.bipedRightArm.rotateAngleX = bipedModel.bipedRightArm.rotateAngleX * 0.5F - 3.1415927F;
            bipedModel.bipedRightArm.rotateAngleY = 0.0F;
            ci.cancel();
        }
    }
    @Inject(method = "func_241655_c_",at=@At("HEAD"),cancellable = true)
    private void func_241655_c_(T p_241655_1_,CallbackInfo ci){
        BipedModel<T> bipedModel = (BipedModel<T>) (Object) this;
        if(ColorQuizMod.color_list.containsKey(p_241655_1_.getUniqueID()) &&leftArmPose.equals(BipedModel.ArmPose.EMPTY)){
            bipedModel.bipedLeftArm.rotateAngleX = bipedModel.bipedLeftArm.rotateAngleX * 0.5F - 3.1415927F;
            bipedModel.bipedLeftArm.rotateAngleY = 0.0F;
            ci.cancel();
        }
    }
}

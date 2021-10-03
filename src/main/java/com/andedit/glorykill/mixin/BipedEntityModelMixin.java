package com.andedit.glorykill.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.andedit.glorykill.extansion.GloryExt;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;

@Mixin(BipedEntityModel.class)
@Environment(EnvType.CLIENT)
abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
	
	@Shadow
	public @Final ModelPart head;
	
	@Shadow
	public @Final ModelPart hat;
	
	@Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
	void animation(T entity, float limbAngle, float limbDist, float progress, float headYaw, float headPitch, CallbackInfo info) {
		if (((GloryExt)entity).getGloryHandle() != null) {
			((GloryExt)entity).getGloryHandle().animate(this, entity, limbAngle, limbDist, progress);
			hat.copyTransform(head);
			info.cancel();
		}
	}
}

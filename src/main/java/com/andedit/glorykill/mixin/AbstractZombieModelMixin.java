package com.andedit.glorykill.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.andedit.glorykill.extansion.GloryExt;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AbstractZombieModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.HostileEntity;

@Mixin(AbstractZombieModel.class)
@Environment(EnvType.CLIENT)
abstract class AbstractZombieModelMixin<T extends HostileEntity> extends BipedEntityModel<T> {

	AbstractZombieModelMixin(ModelPart root) {
		super(root);
	}
	
	@Inject(
	method = "setAngles", 
	at = @At(
		value = "INVOKE", 
		target = 
		"Lnet/minecraft/client/render/entity/model/CrossbowPosing;" +
		"meleeAttack(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;ZFF)V"
	), 
	cancellable = true)
	void canceller(T entity, float f, float g, float h, float i, float j, CallbackInfo info) {
		if (((GloryExt)entity).getGloryTick() > 0) {
			info.cancel();
		}
	}
}

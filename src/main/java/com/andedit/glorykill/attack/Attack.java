package com.andedit.glorykill.attack;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

// I don't know what to put yet.
public class Attack {
	public final int ticks = 40;
	public byte id;
	
	public boolean canGloryKill(PlayerEntity player, LivingEntity entity) {
		return true;
	}
	
	public void animateAttacker(BipedEntityModel<?> model, PlayerEntity player, float limbAngle, float limbDist, float progress) {
		model.rightArm.pitch = -MathHelper.PI / 2.0f;
		model.leftArm.pitch = -MathHelper.PI / 2.0f;
		model.rightArm.pitch += MathHelper.sin(progress);
		model.leftArm.pitch  -= MathHelper.sin(progress);
	}
	
	public void animateVictim(BipedEntityModel<?> model, LivingEntity player, float limbAngle, float limbDist, float progress) {
		model.rightArm.pitch = -MathHelper.PI / 2.0f;
		model.leftArm.pitch = -MathHelper.PI / 2.0f;
		model.rightArm.pitch += MathHelper.sin(progress);
		model.leftArm.pitch  -= MathHelper.sin(progress);
		model.head.yaw = progress;
	}
}
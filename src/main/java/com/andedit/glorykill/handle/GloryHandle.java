package com.andedit.glorykill.handle;

import com.andedit.glorykill.attack.Attack;
import com.andedit.glorykill.extansion.GloryExt;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldAccess;

public class GloryHandle {
	public final PlayerEntity player;
	public final LivingEntity entity;
	public final Attack attack;
	
	public GloryHandle(PlayerEntity player, LivingEntity entity, Attack attack) {
		this.player = player;
		this.entity = entity;
		this.attack = attack;
	}
	
	public boolean canStart(WorldAccess world) {
		if (player.isRemoved() ||
			player.isDead()) {
			return false;
		}
		
		if (entity.isRemoved() ||
			entity.isDead()) {
			return false;
		}
		
		return true;
	}
	
	public boolean canContinue(WorldAccess world) {
		return canStart(world) && ((GloryExt)player).isGlory() && ((GloryExt)entity).isGlory();
	}
	
	public void start() {
		((GloryExt)entity).setGloryHandle(this);
		((GloryExt)player).setGloryHandle(this);
	}

	public void remove() {
		((GloryExt)entity).setGloryHandle(null);
		((GloryExt)player).setGloryHandle(null);
	}

	@Environment(EnvType.CLIENT)
	public void animate(AnimalModel<?> model, LivingEntity living, float limbAngle, float limbDist, float progress) {
		if (living == player) {
			attack.animateAttacker((BipedEntityModel<?>)model, player, limbAngle, limbDist, progress);
		} else if (living == entity) {
			attack.animateVictim((BipedEntityModel<?>)model, entity, limbAngle, limbDist, progress);
		}
	}
}

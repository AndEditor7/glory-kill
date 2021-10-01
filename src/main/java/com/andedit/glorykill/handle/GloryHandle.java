package com.andedit.glorykill.handle;

import com.andedit.glorykill.attack.Attack;
import com.andedit.glorykill.extansion.GloryExt;

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
	
	public void setTicks() {
		((GloryExt)entity).setGloryTick(attack.ticks);
		((GloryExt)player).setGloryTick(attack.ticks);
	}
}

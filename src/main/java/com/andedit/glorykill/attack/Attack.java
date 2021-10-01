package com.andedit.glorykill.attack;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

// I don't know what to put yet.
public class Attack {
	public final int ticks = 40;
	public byte id;
	
	public boolean canGloryKill(PlayerEntity player, LivingEntity entity) {
		return true;
	}
}

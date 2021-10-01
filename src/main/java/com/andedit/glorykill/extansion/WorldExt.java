package com.andedit.glorykill.extansion;

import java.util.List;

import com.andedit.glorykill.handle.GloryHandle;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface WorldExt {
	List<GloryHandle> getGloryList();
	
	void addGlory(GloryHandle handle);
	
	default boolean containEntity(LivingEntity entity) {
		for (var handle : getGloryList()) {
			if (handle.entity == entity) {
				return true;
			}
		}
		
		return false;
	}
	
	default boolean containPlayer(PlayerEntity player) {
		for (var handle : getGloryList()) {
			if (handle.player == player) {
				return true;
			}
		}
		
		return false;
	}
}

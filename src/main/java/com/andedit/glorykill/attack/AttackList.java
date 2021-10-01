package com.andedit.glorykill.attack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AttackList {
	private static final Random RANDOM = new Random();
	
	private final List<Attack> list = new ArrayList<>(5);
	
	public AttackList() {
	}
	
	public AttackList(Attack attack) {
		add(attack);
	}
	
	public void add(Attack attack) {
		attack.id = (byte)list.size();
		list.add(attack);
	}
	
	public Attack get(int id) {
		return list.get(id);
	}

	@Nullable
	public Attack get(PlayerEntity player, LivingEntity entity) {
		var attacks = new ArrayList<Attack>(list.size());
		for (var attack : list) {
			if (attack.canGloryKill(player, entity)) 
				attacks.add(attack);
		}
		
		if (attacks.isEmpty()) {
			return null;
		}
		
		return attacks.get(RANDOM.nextInt(attacks.size()));
	}
}

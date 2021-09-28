package com.andedit.glorykill;

import static com.andedit.glorykill.Main.LOGGER;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.ActionResult;

final class Events {
	static void ints() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (player.isSpectator() || entity.getClass() != ZombieEntity.class) {
				return ActionResult.PASS;
			}
			
			if (!world.isClient) {
				LOGGER.info(entity.getName().getString() + " " + world.isClient);
			}
			
			return ActionResult.SUCCESS;
		});
	}
}

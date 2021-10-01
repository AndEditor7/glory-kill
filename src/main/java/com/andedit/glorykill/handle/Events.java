package com.andedit.glorykill.handle;

import com.andedit.glorykill.Glory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public final class Events {
	public static void ints() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (player.isSpectator()) {
				return ActionResult.PASS;
			}
			
			var canKill = Glory.canKill(entity);
			if (!world.isClient && canKill) {
				Glory.execute((ServerWorld)world, (ServerPlayerEntity)player, (LivingEntity)entity);
			}
			
			return canKill ? ActionResult.SUCCESS : ActionResult.PASS;
		});
		ServerTickEvents.START_WORLD_TICK.register(world -> {
			Glory.tick(world);
		});
	}
	
	@Environment(EnvType.CLIENT)
	public static void intsClient() {
		ClientTickEvents.START_WORLD_TICK.register(world -> {
			Glory.tick(world);
		});
	}
}

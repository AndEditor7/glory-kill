package com.andedit.glorykill;

import static com.andedit.glorykill.Main.LOGGER;

import java.util.HashMap;
import java.util.Map;

import com.andedit.glorykill.attack.Attack;
import com.andedit.glorykill.attack.AttackList;
import com.andedit.glorykill.extansion.GloryExt;
import com.andedit.glorykill.extansion.WorldExt;
import com.andedit.glorykill.handle.GloryHandle;
import com.andedit.glorykill.networking.Networks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/** Glory kill system. */
public final class Glory {
	
	/** List of mob that can be glory kill. */
	private static final Map<Class<? extends LivingEntity>, AttackList> ATTACKS = new HashMap<>();
	
	public static void ints() {
		put(ZombieEntity.class,   new Attack());
		put(CreeperEntity.class,  new Attack());
		put(SkeletonEntity.class, new Attack());
	}
	
	private static void put(Class<? extends LivingEntity> clazz, Attack attack) {
		ATTACKS.put(clazz, new AttackList(attack));
	}
	
	private static void put(Class<? extends LivingEntity> clazz, AttackList attacks) {
		ATTACKS.put(clazz, attacks);
	}
	
	/** Get attack animation. Client side. */
	private static Attack get(Entity entity, int attackId) {
		return ATTACKS.get(entity.getClass()).get(attackId);
	}
	
	/** Get attack animation. Server side. */
	private static Attack get(PlayerEntity player, LivingEntity entity) {
		return ATTACKS.get(entity.getClass()).get(player, entity);
	}
	
	@Environment(EnvType.CLIENT)
	public static void intsClient() {
		ClientPlayNetworking.registerGlobalReceiver(Networks.GLORYKILL, (client, handler, buffer, responseSender) -> {
			var attackId = buffer.readByte();
			var playerId = buffer.readUuid();
			var entityId = buffer.readInt();
			
			client.execute(() -> {
				var world = client.world;
				if (world == null) return;
				
				var player = world.getPlayerByUuid(playerId);
				var entity = world.getEntityById(entityId);
				if (player == null || entity == null) return;
				
				handle(client, new GloryHandle(player, (LivingEntity)entity, get(entity, attackId)));
			});
		});
	}
	
	@Environment(EnvType.CLIENT)
	private static void handle(MinecraftClient client, GloryHandle handle) {
		if (!handle.canStart(client.world)) {
			return;
		}
		
		handle.entity.setOnFire(true);
		var world = client.world;
		((WorldExt)world).addGlory(handle);
	}
	
	public static void tick(World world) {
		var worldExt = (WorldExt)world;
		var list = worldExt.getGloryList();
		
		for (int i = 0; i < list.size(); i++) {
			var handle = list.get(i);
			if (!handle.canContinue(world)) {
				list.remove(i--);
				if (world.isClient) handle.entity.setOnFire(false);
				continue;
			}
			
			LOGGER.info(handle.entity.getClass().getSimpleName());
		}
	}

	public static boolean canKill(Entity entity) {
		// Check it its a living entity.
		if (!entity.isLiving()) return false;
		
		// Check entity status.
		var living = (LivingEntity)entity;
		if (living.isDead() || 
			living.isSpectator() || 
			living.isInvulnerable() ||
			living.isSleeping() ||
			living.hasVehicle() ||
			((GloryExt)entity).isGlory()) {
			return false;
		}
		
		// Check if entity is listed in glory kill.
		return ATTACKS.containsKey(entity.getClass());
	}

	public static void execute(ServerWorld world, ServerPlayerEntity player, LivingEntity entity) {
		// Check if player is in range to entity.
		if (!player.isInRange(entity, 6)) {
			return;
		}
		
		// Check player's status.
		if (player.hasVehicle() || 
			player.isSpectator() ||
			player.isSleeping() ||
			player.isDead() ||
			player.isDisconnected() ||
			((GloryExt)player).isGlory()) {
			return;
		}
		
		// Get attack animation.
		var attack = get(player, entity);
		if (attack == null) return;
		
		// Add glory handles.
		((WorldExt)world).addGlory(new GloryHandle(player, entity, attack));
		
		// Create and write the package for nearby players.
		var buffer = PacketByteBufs.create();
		buffer.writeByte(attack.id);
		buffer.writeUuid(player.getUuid());
		buffer.writeInt(entity.getId());
		
		// Get all nearby players in 128 radius.
		var players = world.getPlayers(client->client.isInRange(entity, 128));
		
		// Send glory kill package to all nearby players.
		for (var client : players) {
			ServerPlayNetworking.send(client, Networks.GLORYKILL, buffer);
		}
	}
}

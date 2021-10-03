package com.andedit.glorykill.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.andedit.glorykill.extansion.GloryExt;
import com.andedit.glorykill.handle.GloryHandle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity implements GloryExt {

	private static final TrackedData<Boolean> GLORY_KILL = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private int gloryTicks;
	private GloryHandle gloryHandle;
	
	LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public int getGloryTick() {
		return gloryTicks;
	}
	
	@Override
	public void setGloryTick(int tick) {
		gloryTicks = tick;
	}
	
	public void setGloryHandle(GloryHandle gloryHandle) {
		this.gloryHandle = gloryHandle;
		if (gloryHandle != null) {
			gloryTicks = gloryHandle.attack.ticks;
		}
	}
	
	public GloryHandle getGloryHandle() {
		return gloryHandle;
	}
	
	@Override
	public boolean isGlory() {
		boolean isClient = world != null && world.isClient;
		return gloryTicks > 0 || (isClient && dataTracker.get(GLORY_KILL));
	}
	
	@Inject(method = "initDataTracker", at = @At("HEAD"))
	void intsTracker(CallbackInfo info) {
		dataTracker.startTracking(GLORY_KILL, false);
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	void ticking(CallbackInfo info) {
		if (gloryTicks > 0) {
			gloryTicks--;
		}
		if (!world.isClient) {
			dataTracker.set(GLORY_KILL, gloryTicks > 0);
		}
	}
}

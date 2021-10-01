package com.andedit.glorykill.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import com.andedit.glorykill.extansion.WorldExt;
import com.andedit.glorykill.handle.GloryHandle;

import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExt, WorldAccess {
	
	private final List<GloryHandle> gloryList = new ArrayList<>();

	@Override
	public List<GloryHandle> getGloryList() {
		return gloryList;
	}

	@Override
	public void addGlory(GloryHandle handle) {
		if (handle.canStart(this)) {
			handle.setTicks();
			gloryList.add(handle);
		}
	}
}

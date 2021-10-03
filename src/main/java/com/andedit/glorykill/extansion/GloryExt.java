package com.andedit.glorykill.extansion;

import org.jetbrains.annotations.Nullable;

import com.andedit.glorykill.handle.GloryHandle;

public interface GloryExt {
	int getGloryTick();
	
	void setGloryTick(int tick);
	
	boolean isGlory();
	
	void setGloryHandle(@Nullable GloryHandle gloryHandle);
	
	@Nullable
	GloryHandle getGloryHandle();
}

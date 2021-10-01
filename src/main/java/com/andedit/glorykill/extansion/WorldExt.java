package com.andedit.glorykill.extansion;

import java.util.List;

import com.andedit.glorykill.handle.GloryHandle;

public interface WorldExt {
	List<GloryHandle> getGloryList();
	void addGlory(GloryHandle handle);
}

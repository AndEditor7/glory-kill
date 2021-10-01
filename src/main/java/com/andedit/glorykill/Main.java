package com.andedit.glorykill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andedit.glorykill.handle.Events;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer, ClientModInitializer {
	public static final String ID = "glorykill";
	
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("GloryKill");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Events.ints();
		Glory.ints();
	}

	@Override
	public void onInitializeClient() {
		Events.intsClient();
		Glory.intsClient();
	}
}

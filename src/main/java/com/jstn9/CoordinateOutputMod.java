package com.jstn9;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class CoordinateOutputMod implements ClientModInitializer {

	private KeyBinding coordinatesKey;
	private KeyBinding selfCoordinatesKey;

	@Override
	public void onInitializeClient() {
		coordinatesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.coordinatemod.send_coordinates",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_Z,
				"category.coordinatemod"
		));

		selfCoordinatesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.coordinatemod.send_coordinates_self",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				"category.coordinatemod"
		));

		ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
		ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);

		KeyInputHandler.loadConfig();
	}

	private void onEndTick(MinecraftClient client) {
		if (coordinatesKey.wasPressed()) {
			KeyInputHandler.handleKeyInput(client, false);
		}
		if (selfCoordinatesKey.wasPressed()) {
			KeyInputHandler.handleKeyInput(client, true);
		}
	}

	private void onStartTick(MinecraftClient client) {
		if (client.player != null) {
			if (client.player.getHealth() <= 0) {
				KeyInputHandler.showDeathCoordinates(client);
			} else {
				KeyInputHandler.resetDeathCoordinatesFlag();
			}
		}
	}
}

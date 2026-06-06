package dev.wrrulosdev.mcpclient.client;

import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.SessionAccessor;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import dev.wrrulosdev.mcpclient.client.screens.MenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class MCPClient implements ClientModInitializer {
	private static PluginChannelStorage pluginChannelStorage;

	@Override
	public void onInitializeClient() {
		start();
		pluginChannelStorage.loadVulnerablePluginMessages();

		// Debug
		User user = Minecraft.getInstance().getUser();
		((SessionAccessor) user).setUsername("MCPTool");

		KeyMapping openMenuInGameTempKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.mcpclient.open_overlay",
			GLFW.GLFW_KEY_V,
			KeyMapping.Category.DEBUG
		));

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			CommandManager.registerCommands(dispatcher);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openMenuInGameTempKey.consumeClick()) {
				if (client.screen == null) {
					client.setScreen(new MenuScreen());
				}
			}
		});

		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			Identifier.fromNamespaceAndPath(ClientConstants.IDENTIFIER, "notifications"),
			NotificationManager::render
		);
	}

	private void start() {
		pluginChannelStorage = new PluginChannelStorage();
	}

	public static PluginChannelStorage getPluginChannelStorage() {
		return pluginChannelStorage;
	}
}
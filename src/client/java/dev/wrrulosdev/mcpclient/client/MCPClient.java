package dev.wrrulosdev.mcpclient.client;

import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class MCPClient implements ClientModInitializer {
	private static PluginChannelStorage pluginChannelStorage;

	@Override
	public void onInitializeClient() {
		start();
		pluginChannelStorage.loadVulnerablePluginMessages();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			CommandManager.registerCommands(dispatcher);
		});
	}

	private void start() {
		pluginChannelStorage = new PluginChannelStorage();
	}

	public static PluginChannelStorage getPluginChannelStorage() {
		return pluginChannelStorage;
	}
}
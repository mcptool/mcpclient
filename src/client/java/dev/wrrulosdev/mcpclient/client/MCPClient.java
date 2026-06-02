package dev.wrrulosdev.mcpclient.client;

import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import net.fabricmc.api.ClientModInitializer;

public class MCPClient implements ClientModInitializer {
	private static PluginChannelStorage pluginChannelStorage;

	@Override
	public void onInitializeClient() {
		start();
		pluginChannelStorage.loadVulnerablePluginMessages();
	}

	private void start() {
		pluginChannelStorage = new PluginChannelStorage();
	}

	public static PluginChannelStorage getPluginChannelStorage() {
		return pluginChannelStorage;
	}
}
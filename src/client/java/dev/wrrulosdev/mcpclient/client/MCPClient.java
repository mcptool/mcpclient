package dev.wrrulosdev.mcpclient.client;

import dev.wrrulosdev.mcpclient.client.cheats.*;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.exploits.*;
import dev.wrrulosdev.mcpclient.client.keybinds.KeyBindManager;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.SessionAccessor;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.options.*;
import dev.wrrulosdev.mcpclient.client.payloads.*;
import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import dev.wrrulosdev.mcpclient.client.screens.MenuScreen;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import dev.wrrulosdev.mcpclient.client.settings.ExploitsSettings;
import dev.wrrulosdev.mcpclient.client.settings.SettingsManager;
import dev.wrrulosdev.mcpclient.client.spoofing.SpoofingManager;
import dev.wrrulosdev.mcpclient.client.utilities.connection.ServerAddress;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class MCPClient implements ClientModInitializer {

	private static PluginChannelStorage pluginChannelStorage;
	private static SettingsManager settingsManager;
	private static KeyBindManager keyBindManager;
	private static ServerAddress lastServerAddress;
	private static SpoofingManager spoofingManager;

    @Override
	public void onInitializeClient() {
		try {
			settingsManager = new SettingsManager();
			settingsManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		start();

		// Debug
		User user = Minecraft.getInstance().getUser();
		((SessionAccessor) user).setUsername("MCPTool");

		PayloadTypeRegistry.serverboundPlay().register(MultiChatPayload.TYPE, MultiChatPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(EasyCommandBlockerPayload.TYPE, EasyCommandBlockerPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(CloudSyncPayload.TYPE, CloudSyncPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(T2CPayload.TYPE, T2CPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(AtlasPayload.TYPE, AtlasPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(CommandBridgePayload.TYPE, CommandBridgePayload.CODEC);

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
				if (client.gui.screen() == null) {
					client.setScreenAndShow(new MenuScreen());
				}
			}

			if (MCPClient.getKeyBindManager() != null) {
				MCPClient.getKeyBindManager().tick();
			}
		});

		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			Identifier.fromNamespaceAndPath(ClientConstants.IDENTIFIER, "notifications"),
			NotificationManager::render
		);
	}

	private void start() {
		User user = Minecraft.getInstance().getUser();

		CheatsSettings cheatsSettings = getSettingsManager().getCheatsSettings();
		ClientSettings clientSettings = getSettingsManager().getClientSettings();
		ExploitsSettings exploitsSettings = getSettingsManager().getExploitsSettings();

		// Cheats
		AntiKB.init(cheatsSettings);
		BlockTracker.init(cheatsSettings);
		FakeCreative.init(cheatsSettings);
		Fly.init(cheatsSettings);
		FullBright.init(cheatsSettings);
		HClip.init(cheatsSettings);
		VClip.init(cheatsSettings);
		Jesus.init(cheatsSettings);
		NoFall.init(cheatsSettings);
		Spider.init(cheatsSettings);
		WallHack.init(cheatsSettings);

		// Exploits
		CloudSync.init(exploitsSettings);
		T2C.init(exploitsSettings);
		EasyCommandBlocker.init(exploitsSettings);
		CommandBridge.init(exploitsSettings);
		Atlas.init(exploitsSettings);
		MultiChat.init(exploitsSettings);
		LiteBans.init(exploitsSettings);
		HolographicDisplays.init(exploitsSettings);

		// Options
		Anonymous.init(clientSettings);
		ClientHud.init(clientSettings);
		NameTag.init(clientSettings);
		ChatAnimation.init(clientSettings);
		Notifications.init(clientSettings);
		PlayerModel.init(clientSettings);

		// Objects
		pluginChannelStorage = new PluginChannelStorage();
		keyBindManager = new KeyBindManager();
		spoofingManager = new SpoofingManager(user.getName(), user.getProfileId());
	}

	public static void saveSettings() {
		try {
			getSettingsManager().save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PluginChannelStorage getPluginChannelStorage() {
		return pluginChannelStorage;
	}

	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public static ServerAddress getLastServerAddress() {
		return lastServerAddress;
	}

	public static SpoofingManager getSpoofingManager() {
		return spoofingManager;
	}

	public static KeyBindManager getKeyBindManager() {
		return keyBindManager;
	}

	public static void updateLastServerAddress() {
		MCPClient.lastServerAddress = new ServerAddress(Minecraft.getInstance());
	}
}
package dev.wrrulos.mcpclient;

import dev.wrrulos.mcpclient.command.CommandManager;
import dev.wrrulos.mcpclient.keybinds.KeyBindManager;
import dev.wrrulos.mcpclient.payloads.PayloadRegistry;
import dev.wrrulos.mcpclient.render.HudRenderer;
import dev.wrrulos.mcpclient.session.SessionController;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import dev.wrrulos.mcpclient.utils.PluginMessageStorage;
import dev.wrrulos.mcpclient.utils.PluginTabStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class    Mcpclient implements ModInitializer {
    private static final PluginMessageStorage pluginMessageStorage = new PluginMessageStorage();
    private static final PluginTabStorage pluginTabStorage = new PluginTabStorage();
    private static final KeyBindManager keyBindManager = new KeyBindManager();
    private static final SessionController sessionController = new SessionController();
    private static MCPToolSettings settings;

    @Override
    public void onInitialize() {
        // Set the UUID and original UUID in the session controller
        String originalUUID = sessionController.getOriginalUUIDFromSession();
        sessionController.setUUID(originalUUID);
        sessionController.setOriginalUUID(originalUUID);

        // Load the settings
        settings = MCPToolSettings.load();

        // Load the vulnerable plugins
        pluginMessageStorage.loadVulnerablePluginMessages();

        // Register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            CommandManager.registerCommands(dispatcher);
        });

        // Register plugin message payloads
        PayloadRegistry.registerAllPayloads();

        // Register the HUD renderer
        HudRenderCallback.EVENT.register(new HudRenderer());

        // Initialize keybinds
        keyBindManager.initialize();
        keyBindManager.setKeyBindCommands();
    }

    /**
     * Get the plugin message storage instance.
     *
     * @return The plugin message storage instance.
     */
    public static PluginMessageStorage getPluginMessageStorage() {
        return pluginMessageStorage;
    }

    /**
     * Get the plugin tab storage instance.
     *
     * @return The plugin tab storage instance.
     */
    public static PluginTabStorage getPluginTabStorage() {
        return pluginTabStorage;
    }

    /**
     * Get the keybind manager
     * @return Keybind manager
     */
    public static KeyBindManager getKeyBindManager() {
        return keyBindManager;
    }

    /**
     * Get the session controller
     * @return Session controller
     */
    public static SessionController getSessionController() {
        return sessionController;
    }

    /**
     * Get the settings
     * @return Settings
     */
    public static MCPToolSettings getSettings() {
        return settings;
    }
}
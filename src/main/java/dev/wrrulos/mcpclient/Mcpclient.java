package dev.wrrulos.mcpclient;

import dev.wrrulos.mcpclient.utils.PluginMessageStorage;
import net.fabricmc.api.ModInitializer;

public class Mcpclient implements ModInitializer {
    private static final PluginMessageStorage pluginMessageStorage = new PluginMessageStorage();

    @Override
    public void onInitialize() {
        pluginMessageStorage.loadVulnerablePluginMessages();
        System.out.println("Hello Fabric world!");
    }

    /**
     * Get the plugin message storage instance.
     *
     * @return The plugin message storage instance.
     */
    public static PluginMessageStorage getPluginMessageStorage() {
        return pluginMessageStorage;
    }
}
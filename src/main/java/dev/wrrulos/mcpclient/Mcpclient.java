package dev.wrrulos.mcpclient;

import dev.wrrulos.mcpclient.command.CommandManager;
import dev.wrrulos.mcpclient.payloads.AuthMeVelocityPayloadPacket;
import dev.wrrulos.mcpclient.payloads.PayloadRegistry;
import dev.wrrulos.mcpclient.utils.PluginMessageStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class Mcpclient implements ModInitializer {
    private static final PluginMessageStorage pluginMessageStorage = new PluginMessageStorage();

    @Override
    public void onInitialize() {
        pluginMessageStorage.loadVulnerablePluginMessages();
        System.out.println("Hello Fabric world!");

        // Register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            CommandManager.registerCommands(dispatcher);
        });

        // Register plugin message payloads
        PayloadRegistry.registerAllPayloads();
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
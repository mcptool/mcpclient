package dev.wrrulosdev.mcpclient.client.pluginschannel;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PluginChannelStorage {

    private static final List<String> storedPluginMessages = new ArrayList<>();
    private static final List<String> vulnerablePluginMessages = new ArrayList<>();
    private static final List<String> vulnerablePluginMessagesFounded = new ArrayList<>();
    private static final AtomicBoolean sending = new AtomicBoolean(false);

    /**
     * Adds a plugin message to the storage list if it's not already stored.
     * This ensures that each plugin message is only stored once.
     *
     * @param message The plugin message to be stored.
     */
    public void addPluginMessage(String message) {
        if (message.startsWith("fabric:") || message.startsWith("fabric-")) return;
        if (storedPluginMessages.contains(message)) return;
        System.out.println("[LOG] Adding plugin message to storage: " + message);
        storedPluginMessages.add(message);
    }

    /**
     * Sends all stored plugin messages to the player and clears the storage list.
     * This is typically called after the player has connected to the server.
     */
    public void sendStoredPluginMessages() {
        if (!sending.compareAndSet(false, true)) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        try {
            if (storedPluginMessages.isEmpty()) {
                return;
            }

            vulnerablePluginMessagesFounded.clear();

            client.execute(() -> {
                Msg.sendFormattedMessage(ClientConstants.PREFIX + "&cRegistered plugin message channels:");
                Msg.sendFormattedMessage("&cFounded plugin channels: &6" + storedPluginMessages.size());

                for (String pluginChannel : storedPluginMessages) {
                    if (vulnerablePluginMessages.contains(pluginChannel)) {
                        Msg.sendFormattedMessage("&f• &6" + pluginChannel + " &f(&6VULNERABLE&7)");
                        vulnerablePluginMessagesFounded.add(pluginChannel);
                    } else {
                        Msg.sendFormattedMessage("&f• &6" + pluginChannel);
                    }
                }

                if (!vulnerablePluginMessagesFounded.isEmpty()) {
                    NotificationManager.show(
                        "Vulnerable Plugin Channel",
                        String.format("%d potentially vulnerable plugin channels were detected.", vulnerablePluginMessagesFounded.size()),
                        NotificationType.INFO
                    );
                }

                storedPluginMessages.clear();
            });

        } finally {
            sending.set(false);
        }
    }

    /**
     * Loads known vulnerable plugin messages into the list of vulnerable messages.
     * This method is called during initialization to set up vulnerable channels.
     */
    public void loadVulnerablePluginMessages() {
        vulnerablePluginMessages.add("authmevelocity:main");
        vulnerablePluginMessages.add("signedvelocity:main");
    }
}
package dev.wrrulos.mcpclient.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the storage of plugin message channels until the player is connected.
 * The stored messages will be sent to the player's chat once the connection is established.
 */
public class PluginMessageStorage {

    // List to store plugin message channels temporarily
    private static final List<String> storedPluginMessages = new ArrayList<>();

    /**
     * Adds a plugin message to the storage list.
     *
     * @param message The plugin message to be stored.
     */
    public static void addPluginMessage(String message) {
        storedPluginMessages.add(message);
    }

    /**
     * Sends all stored plugin messages to the player and clears the storage.
     */
    public static void sendStoredPluginMessages() {
        for (String message : storedPluginMessages) {
            ChatUtils.sendMessage(message);
        }

        // Clear the list after sending the messages
        storedPluginMessages.clear();
    }
}

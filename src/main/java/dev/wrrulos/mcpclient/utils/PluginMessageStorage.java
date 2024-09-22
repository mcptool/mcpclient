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
    // List to store vulnerable plugin message channels
    public static final List<String> vulnerablePluginMessages = new ArrayList<>();

    /**
     * Adds a plugin message to the storage list.
     *
     * @param message The plugin message to be stored.
     */
    public void addPluginMessage(String message) {
        if (storedPluginMessages.contains(message)) {
            return;
        }

        System.out.println("[LOG] Adding plugin message to storage: " + message);
        storedPluginMessages.add(message);
    }

    /**
     * Sends all stored plugin messages to the player and clears the storage.
     */
    public void sendStoredPluginMessages() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Error while sleeping thread: " + e.getMessage());
        }

        if (storedPluginMessages.isEmpty()) {
            ChatUtils.sendMessage("§fNo plugin message channels registered.");
            return;
        }

        ChatUtils.sendMessage("§fRegistered plugin message channels:");

        for (String message : storedPluginMessages) {
            System.out.println("[LOG] Sending message: " + message);

            if (vulnerablePluginMessages.contains(message)) {
                ChatUtils.sendMessage("§f• " + "§d" + message + " &cVULNERABLE");
                continue;
            }

            ChatUtils.sendMessage("§f• " + "§a" + message);
        }

        // Clear the list after sending the messages
        storedPluginMessages.clear();
    }

    /**
     * Adds known vulnerable plugin messages to the list.
     */
    public void loadVulnerablePluginMessages() {
        vulnerablePluginMessages.add("authmevelocity:main");
        vulnerablePluginMessages.add("signedvelocity:main");
    }
}

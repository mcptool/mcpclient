package dev.wrrulosdev.mcpclient.client.pluginschannel;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static dev.wrrulosdev.mcpclient.client.constants.PayloadConstants.PLUGINS_CHANNELS_VULNERABLES;

public class PluginChannelStorage {

    private static final List<String> storedPluginMessages = new ArrayList<>();
    private static final List<String> vulnerablePluginMessagesFounded = new ArrayList<>();

    private static final AtomicBoolean sending = new AtomicBoolean(false);
    private static final AtomicBoolean loginHandled = new AtomicBoolean(false);

    /**
     * Stores a discovered plugin message channel if it is not already registered
     * and does not belong to Fabric internal communication namespaces.
     *
     * @param message Plugin channel identifier received from the server.
     */
    public void addPluginMessage(String message) {
        if (message.startsWith("fabric:") || message.startsWith("fabric-")) return;
        if (storedPluginMessages.contains(message)) return;

        System.out.println("[LOG] Adding plugin message to storage: " + message);
        storedPluginMessages.add(message);
    }

    /**
     * Processes all collected plugin message channels, displays them in chat,
     * highlights known vulnerable channels, and generates a notification when
     * potentially exploitable integrations are detected.
     */
    public void sendStoredPluginMessages() {
        if (storedPluginMessages.isEmpty()) return;
        if (!sending.compareAndSet(false, true)) return;

        Minecraft client = Minecraft.getInstance();

        try {
            List<String> snapshot = new ArrayList<>(storedPluginMessages);
            vulnerablePluginMessagesFounded.clear();

            client.execute(() -> {
                Msg.sendFormattedMessage(ClientConstants.PREFIX + "&cRegistered plugin message channels:");
                Msg.sendFormattedMessage("&cFounded plugin channels: &6" + snapshot.size());

                for (String pluginChannel : snapshot) {
                    if (PLUGINS_CHANNELS_VULNERABLES.contains(pluginChannel)) {
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
            resetForNewConnection();
        }
    }

    /**
     * Performs an atomic validation to determine whether login-related
     * processing can be executed for the current connection session.
     *
     * @return True if the login action has not yet been executed; otherwise false.
     */
    public boolean canRunLoginOnce() {
        return loginHandled.compareAndSet(false, true);
    }

    /**
     * Clears all runtime tracking state associated with the current server
     * connection and prepares the storage system for a future login session.
     */
    public void resetForNewConnection() {
        loginHandled.set(false);
        sending.set(false);
        vulnerablePluginMessagesFounded.clear();
        storedPluginMessages.clear();
    }
}
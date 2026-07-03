package dev.wrrulosdev.mcpclient.client.utilities.connection;

import net.minecraft.client.player.LocalPlayer;

public class CommandUtils {

    public static void execute(LocalPlayer player, String command) {
        player.connection.sendCommand(command);
    }

    public static void sendMessage(LocalPlayer player, String message) {
        player.connection.sendChat(message);
    }
}

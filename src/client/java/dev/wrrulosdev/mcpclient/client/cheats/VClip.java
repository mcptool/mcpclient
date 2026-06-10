package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class VClip {

    /**
     * Teleports the local player vertically by the specified distance,
     * displays a formatted chat message, and shows a success notification
     * indicating the direction and amount of movement performed.
     *
     * @param distance The vertical offset applied to the player's current position.
     *                 Positive values move upward while negative values move downward.
     */
    public static void execute(double distance) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        player.setPos(
            player.getX(),
            player.getY() + distance,
            player.getZ()
        );

        String direction = distance >= 0 ? "up" : "down";

        Msg.sendFormattedMessage(
            ClientConstants.PREFIX +
                "&aTeleported &d" + Math.abs(distance) +
                " &ablocks &d" + direction
        );

        NotificationManager.show(
            "VClip",
            "Teleported " + Math.abs(distance) + " blocks " + direction,
            NotificationType.SUCCESS
        );
    }
}
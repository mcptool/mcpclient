package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class VClip {

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
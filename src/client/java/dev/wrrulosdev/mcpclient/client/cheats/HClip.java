package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class HClip {

    /**
     * Teleports the local player horizontally relative to their current direction.
     * A positive distance moves in one horizontal direction while a negative
     * distance moves in the opposite direction. After teleporting, a chat
     * message and notification are displayed to the user.
     *
     * @param distance Number of blocks to move horizontally
     */
    public static void execute(double distance) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println("..");
            return;
        }

        float yaw = player.getYRot();
        double yawRad = Math.toRadians(yaw);
        double deltaX = -Math.sin(yawRad) * distance;
        double deltaZ = Math.cos(yawRad) * distance;
        String direction = distance >= 0 ? "right" : "left";

        player.setPos(
            (player.position().x() - deltaZ),
            player.position().y(),
            (float) (player.position().z() + deltaX)
        );
        Msg.sendFormattedMessage(
            ClientConstants.PREFIX +
                "&cTeleported &f" +
                Math.abs(distance) +
                " &cblocks &fto" +
                direction
        );
        NotificationManager.show(
            "HClip",
            "Teleported " + Math.abs(distance) + " blocks to" + direction,
            NotificationType.SUCCESS
        );
    }
}

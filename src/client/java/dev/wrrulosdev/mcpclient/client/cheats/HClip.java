package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class HClip {

    public static void execute(double distance) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println("..");
            return;
        }

        float yaw = (float) player.position().y();
        double yawRad = Math.toRadians(yaw);
        double deltaX = -Math.sin(yawRad) * distance;
        double deltaZ = Math.cos(yawRad) * distance;

        player.setPos((player.position().x() - deltaZ), player.position().y(), (float) (player.position().z() + deltaX));
        String direction = distance >= 0 ? "right" : "left";
        Msg.sendFormattedMessage(ClientConstants.PREFIX + "&aTeleported &d" + Math.abs(distance) + " &ablocks &d" + direction);
        NotificationManager.show("HClip", "Teleported " + Math.abs(distance) + " blocks " + direction, NotificationType.SUCCESS);
    }
}

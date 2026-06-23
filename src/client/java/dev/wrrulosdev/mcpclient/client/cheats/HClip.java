package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.player.LocalPlayer;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;

public class HClip extends CheatBase {

    public static HClip INSTANCE;

    protected HClip(CheatsSettings cheatsSettings) {
        super(cheatsSettings);
    }

    public static void init(CheatsSettings cheatsSettings) {
        INSTANCE = new HClip(cheatsSettings);
    }

    /**
     * Returns the unique identifier used to reference this cheat.
     *
     * @return The cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "hclip";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "HClip";
    }

    /**
     * Returns the default keyboard key assigned to this module.
     *
     * @return The GLFW key code used as the default keybind.
     */
    @Override
    public int getDefaultKey() {
        return ClientConstants.DEFAULT_INVALID_KEYBIND;
    }

    /**
     * Determines whether the HClip module is currently enabled.
     *
     * @return True if HClip is enabled.
     */
    @Override
    public boolean isEnabled() {
        return cheatsSettings.isHClipEnabled();
    }

    /**
     * Updates the enabled state of the HClip module.
     *
     * @param enabled The new HClip state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        cheatsSettings.setHClipEnabled(enabled);
    }

    /**
     * Determine whether the trick should be executed when it is
     * disabled using the toggle function.
     */
    @Override
    public boolean runOnToggle() {
        return false;
    }

    /**
     * Executes a horizontal clip by moving the player sideways relative to
     * their current viewing direction. Positive values move to the right
     * while negative values move to the left.
     *
     * @param player The local player instance.
     * @param args Additional execution arguments where the first value
     *             represents the clipping distance.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        int distance = (int) ((args.length != 0) ? ((Number) args[0]).doubleValue() : cheatsSettings.gethClipDistance());

        if (player == null) {
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
                " &cblocks &fto " +
                direction
        );
        NotificationManager.show(
            getName(),
            "Teleported " + Math.abs(distance) + " blocks to " + direction,
            NotificationType.SUCCESS
        );
    }
}
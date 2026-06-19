package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.minecraft.client.player.LocalPlayer;

public class VClip extends CheatBase {

    public static final VClip INSTANCE = new VClip();

    /**
     * Retrieves the cheat configuration container.
     *
     * @return The cheat settings instance.
     */
    private CheatsSettings getSettings() {
        return MCPClient.getSettingsManager().getCheatsSettings();
    }

    /**
     * Returns the internal identifier used for command registration,
     * configuration storage and cheat lookup.
     *
     * @return The VClip cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "vclip";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "VClip";
    }

    /**
     * Returns the default keyboard key assigned to this module.
     *
     * @return The default keybind identifier.
     */
    @Override
    public int getDefaultKey() {
        return ClientConstants.DEFAULT_INVALID_KEYBIND;
    }

    /**
     * Determines whether the cheat is currently enabled.
     *
     * @return True if VClip is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isVClipEnabled();
    }

    /**
     * Updates the enabled state of the cheat.
     *
     * @param enabled The new enabled state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setVClipEnabled(enabled);
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
     * Performs a vertical teleport by instantly moving the player up or down
     * by the specified distance. Positive values move upward while negative
     * values move downward.
     *
     * @param player The local player instance.
     * @param args Additional execution arguments where the first value
     *             represents the vertical clipping distance.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        int distance = (int) ((args.length != 0) ? ((Number) args[0]).doubleValue() : this.getSettings().getvClipDistance());

        if (player == null) {
            return;
        }

        player.setPos(
            player.getX(),
            player.getY() + distance,
            player.getZ()
        );

        String direction = distance >= 0
            ? "up"
            : "down";
        Msg.sendFormattedMessage(
            ClientConstants.PREFIX +
                "&cTeleported &f" +
                Math.abs(distance) +
                " &cblocks &fto " +
                direction
        );
        NotificationManager.show(
            "HClip",
            "Teleported " + Math.abs(distance) + " blocks to " + direction,
            NotificationType.SUCCESS
        );
    }
}
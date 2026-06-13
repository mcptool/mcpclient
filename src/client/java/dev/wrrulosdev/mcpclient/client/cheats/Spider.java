package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class Spider extends CheatBase {

    public static final Spider INSTANCE = new Spider();

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
     * @return The spider cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "spider";
    }

    /**
     * Returns the default key assigned to this cheat.
     * Spider is disabled by default and does not have a keybind.
     *
     * @return The default key identifier.
     */
    @Override
    public int getDefaultKey() {
        return ClientConstants.DEFAULT_INVALID_KEYBIND;
    }

    /**
     * Determines whether the cheat is currently enabled.
     *
     * @return True if spider mode is active.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isSpiderEnabled();
    }

    /**
     * Updates the enabled state of the cheat.
     *
     * @param enabled The new enabled state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setSpiderEnabled(enabled);
    }

    /**
     * Applies the wall climbing behavior by pushing the player upward whenever
     * they are moving against a solid surface. The player's ground state is
     * also maintained to prevent fall damage accumulation while climbing.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        if (!isEnabled()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        boolean isTryingToMove =
            mc.options.keyUp.isDown()
                || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown();

        if (!player.horizontalCollision || !isTryingToMove) {
            return;
        }

        if (player.getDeltaMovement().y < 0.2D) {
            player.setDeltaMovement(
                player.getDeltaMovement().x,
                this.getSettings().getSpiderSpeed(),
                player.getDeltaMovement().z
            );
        }

        player.setOnGround(true);
        player.fallDistance = 0.0F;
    }
}
package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class FakeCreative extends CheatBase {

    public static final FakeCreative INSTANCE = new FakeCreative();

    /**
     * Retrieves the cheat settings instance used by this module.
     *
     * @return The current cheats settings configuration.
     */
    private CheatsSettings getSettings() {
        return MCPClient.getSettingsManager().getCheatsSettings();
    }

    /**
     * Returns the unique identifier used to reference this cheat.
     *
     * @return The cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "fakegm";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "FakeCreative";
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
     * Determines whether the fake creative module is currently enabled.
     *
     * @return True if fake creative is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isFakeGmEnabled();
    }

    /**
     * Updates the enabled state of the fake creative module.
     *
     * @param enabled The new module state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setFakeGmEnabled(enabled);
    }

    /**
     * Updates the client's local game mode to simulate Creative mode when
     * enabled, or restores Survival mode when disabled. This modification
     * only affects the client-side game state and does not grant actual
     * server-side creative permissions.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        GameType targetGameType = isEnabled()
            ? GameType.CREATIVE
            : GameType.SURVIVAL;

        var gameMode = Minecraft.getInstance().gameMode;

        if (gameMode != null) {
            gameMode.setLocalMode(targetGameType);
        }

        player.onUpdateAbilities();
    }
}
package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.player.LocalPlayer;

public class Fly extends CheatBase {

    public static Fly INSTANCE;

    protected Fly(CheatsSettings cheatsSettings) {
        super(cheatsSettings);
    }

    public static void init(CheatsSettings cheatsSettings) {
        INSTANCE = new Fly(cheatsSettings);
    }

    /**
     * Returns the unique identifier used to reference this cheat.
     *
     * @return The cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "fly";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "Fly";
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
     * Determines whether the fly module is currently enabled.
     *
     * @return True if fly is enabled.
     */
    @Override
    public boolean isEnabled() {
        return this.cheatsSettings.isFlyEnabled();
    }

    /**
     * Updates the enabled state of the fly module.
     *
     * @param enabled The new fly state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.cheatsSettings.setFlyEnabled(enabled);
    }

    /**
     * Applies the fly state to the player by updating their flight abilities
     * and configured movement speed. When enabled, the player gains the
     * ability to fly freely; otherwise all flight permissions are removed.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        boolean enabled = isEnabled();
        float flySpeed = this.cheatsSettings.getFlySpeed();

        player.getAbilities().flying = enabled;
        player.getAbilities().mayfly = enabled;
        player.getAbilities().setFlyingSpeed(flySpeed);

        player.onUpdateAbilities();
    }
}
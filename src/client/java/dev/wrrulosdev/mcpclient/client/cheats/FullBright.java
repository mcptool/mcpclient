package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;

public class FullBright extends CheatBase {

    public static final FullBright INSTANCE = new FullBright();

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
        return "fullbright";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "FullBright";
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
     * Determines whether the fullbright module is currently enabled.
     *
     * @return True if fullbright is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isFullBrightEnabled();
    }

    /**
     * Updates the enabled state of the fullbright module.
     *
     * @param enabled The new fullbright state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setFullBrightEnabled(enabled);
    }

    /**
     * Executes the FullBright logic by adjusting the client's
     * gamma setting.
     *
     * @param player The local player instance.
     * @param args   Optional execution arguments.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        Options options = Minecraft.getInstance().options;
        System.out.println(isEnabled());
        double gammaValue = isEnabled() ? 100.0D : 1.0D;
        System.out.println(gammaValue);
        options.gamma().set(gammaValue);
    }
}
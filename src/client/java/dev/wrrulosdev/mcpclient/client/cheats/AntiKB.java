package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.options.ClientHud;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import net.minecraft.client.player.LocalPlayer;

public class AntiKB extends CheatBase {

    public static AntiKB INSTANCE;

    protected AntiKB(CheatsSettings cheatsSettings) {
        super(cheatsSettings);
    }

    public static void init(CheatsSettings cheatsSettings) {
        INSTANCE = new AntiKB(cheatsSettings);
    }


    /**
     * Returns the unique identifier used to reference this cheat.
     *
     * @return The cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "antikb";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "AntiKB";
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
     * Determines whether the antikb module is currently enabled.
     *
     * @return True if antikb is enabled.
     */
    @Override
    public boolean isEnabled() {
        return this.cheatsSettings.isAntikbEnabled();
    }

    /**
     * Updates the enabled state of the antikb module.
     *
     * @param enabled The new antikb state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.cheatsSettings.setAntikbEnabled(enabled);
    }

    /**
     * The logic is in the mixin! (Search "// AntiKB")
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {}
}
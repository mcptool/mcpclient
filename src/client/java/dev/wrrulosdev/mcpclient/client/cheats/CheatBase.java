package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class CheatBase {

    /**
     * Returns the unique identifier used to reference this cheat
     * throughout the client configuration and command systems.
     *
     * @return The cheat identifier.
     */
    public abstract String getIdentifier();

    /**
     * Returns the default keyboard key assigned to this cheat.
     *
     * @return The GLFW key identifier.
     */
    public abstract int getDefaultKey();

    /**
     * Determines whether the cheat is currently enabled.
     *
     * @return True if the cheat is active.
     */
    public abstract boolean isEnabled();

    /**
     * Updates the enabled state of the cheat.
     *
     * @param enabled The new enabled state.
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Executes the cheat-specific logic.
     * Implementations receive the current local player instance
     * together with any optional execution arguments.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments.
     */
    protected abstract void onExecute(LocalPlayer player, Object... args);

    /**
     * Toggles the current cheat state and immediately executes
     * the cheat logic to apply the updated configuration.
     */
    public final void toggle() {
        boolean newState = !isEnabled();

        setEnabled(newState);
        run();
    }

    /**
     * Executes the cheat using the current local player instance and
     * the provided execution arguments.
     *
     * @param args Optional execution arguments.
     */
    public final void run(Object... args) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        onExecute(player, args);
    }
}
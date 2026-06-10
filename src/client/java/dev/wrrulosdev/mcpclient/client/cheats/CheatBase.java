package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class CheatBase {

    /**
     * Executes the cheat if a local player instance is available.
     * <p>
     * Retrieves the current player from the Minecraft client and
     * forwards execution to the implementation-specific handler.
     */
    public final void run() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println("...");
            return;
        }

        onExecute(player);
    }

    /**
     * Called when the cheat is executed and a valid player instance exists.
     *
     * @param player Current local player
     */
    protected abstract void onExecute(LocalPlayer player);
}
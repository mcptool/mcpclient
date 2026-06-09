package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.player.LocalPlayer;

public class Fly extends CheatBase {

    /**
     * Enables client-side flight capabilities for the local player
     * and updates the player's ability state.
     *
     * @param player Current local player
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        player.getAbilities().flying = true;
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();
    }
}
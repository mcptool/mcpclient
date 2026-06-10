package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import net.minecraft.client.player.LocalPlayer;

public class Fly extends CheatBase {

    public static final Fly INSTANCE = new Fly();

    /**
     * Enables client-side flight capabilities for the local player
     * and updates the player's ability state.
     *
     * @param player Current local player
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        boolean enabled = MCPClient.getSettingsManager().getCheatsSettings().isFlyEnabled();
        player.getAbilities().flying = enabled;
        player.getAbilities().mayfly = enabled;
        player.onUpdateAbilities();
    }
}
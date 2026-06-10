package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class FakeCreative extends CheatBase {

    public static final FakeCreative INSTANCE = new FakeCreative();

    /**
     * Changes the local game mode to Creative client-side only
     * and refreshes player abilities accordingly.
     * This does not grant actual server-side creative permissions.
     *
     * @param player Current local player
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        GameType newGamemode = MCPClient.getSettingsManager().getCheatsSettings().isFakeGmEnabled() ? GameType.CREATIVE : GameType.SURVIVAL;
        Minecraft.getInstance().gameMode.setLocalMode(newGamemode);
        player.onUpdateAbilities();
    }
}
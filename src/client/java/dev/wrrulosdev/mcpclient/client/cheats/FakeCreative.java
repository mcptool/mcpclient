package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class FakeCreative extends CheatBase {

    /**
     * Changes the local game mode to Creative client-side only
     * and refreshes player abilities accordingly.
     *
     * This does not grant actual server-side creative permissions.
     *
     * @param player Current local player
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        Minecraft.getInstance().gameMode.setLocalMode(GameType.CREATIVE);
        player.onUpdateAbilities();
    }
}
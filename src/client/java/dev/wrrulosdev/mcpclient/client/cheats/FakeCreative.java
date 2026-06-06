package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class FakeCreative extends CheatBase {

    @Override
    protected void onExecute(LocalPlayer player) {
        Minecraft.getInstance().gameMode.setLocalMode(GameType.CREATIVE);
        player.onUpdateAbilities();
    }
}

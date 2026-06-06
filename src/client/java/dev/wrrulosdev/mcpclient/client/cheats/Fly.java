package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.player.LocalPlayer;

public class Fly extends CheatBase {

    @Override
    protected void onExecute(LocalPlayer player) {
        player.getAbilities().flying = true;
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();
    }
}

package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class CheatBase {

    public final void run() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            System.out.println("...");
            return;
        }

        onExecute(player);
    }

    protected abstract void onExecute(LocalPlayer player);
}

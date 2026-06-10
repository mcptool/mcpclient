package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class Spider extends CheatBase {

    public static final Spider INSTANCE = new Spider();

    /**
     * Simulates wall climbing behavior by applying upward motion whenever
     * the local player collides horizontally while moving forward or backward.
     * Fall distance is reset to prevent fall damage accumulation during climbing.
     *
     * @param player The local player instance being processed by the cheat system.
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        Minecraft mc = Minecraft.getInstance();

        if (!player.horizontalCollision
            || (!mc.options.keyUp.isDown()
            && !mc.options.keyDown.isDown()
            && !mc.options.keyLeft.isDown()
            && !mc.options.keyRight.isDown())) {
            return;
        }

        if (player.getDeltaMovement().y < 0.2) {
            player.setDeltaMovement(
                player.getDeltaMovement().x,
                0.2,
                player.getDeltaMovement().z
            );
        }

        player.setOnGround(true);
        player.fallDistance = 0.0f;
    }
}

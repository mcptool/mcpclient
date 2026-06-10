package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.AABB;

public class NoFall extends CheatBase {

    public static final NoFall INSTANCE = new NoFall();

    @Override
    protected void onExecute(LocalPlayer player) {
        boolean isFalling = player.getDeltaMovement().y < -0.1;
        boolean canTakeFallDamage = player.fallDistance > 3.0f;

        if (!isFalling || !canTakeFallDamage) {
            return;
        }

        double nextY = player.getDeltaMovement().y;
        AABB predictedBox = player.getBoundingBox().move(0, nextY - 0.1, 0);

        boolean willHitGround = !player.level().noCollision(player, predictedBox);

        if (!willHitGround) {
            return;
        }

        player.getAbilities().flying = true;
        player.setDeltaMovement(
            player.getDeltaMovement().x,
            0.1,
            player.getDeltaMovement().z
        );
        player.getAbilities().flying = false;
        player.fallDistance = 0.0f;
    }
}
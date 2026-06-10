package dev.wrrulosdev.mcpclient.client.mixins.player;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.Jesus;
import dev.wrrulosdev.mcpclient.client.cheats.Spider;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    /**
     * Injects logic at the start of the player tick to modify movement and collision behavior.
     *
     * @param ci Callback information for the tick method
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        Minecraft mc = Minecraft.getInstance();
        CheatsSettings cheatsSettings = MCPClient.getSettingsManager().getCheatsSettings();

        /*if (cheatsSettings.isFlyEnabled()) {
            Fly.INSTANCE.run();
        }

        if (cheatsSettings.isFakeGmEnabled()) {
            FakeCreative.INSTANCE.run();
        }*/
        if (cheatsSettings.isJesusEnabled()) {
            Jesus.INSTANCE.run();
        }

        // NoFall
        if (player.getDeltaMovement().y < -0.1 && player.fallDistance > 3.0f) {
            double nextY = player.getDeltaMovement().y;
            AABB predictedBox = player.getBoundingBox().move(0, nextY - 0.1, 0);

            if (!player.level().noCollision(player, predictedBox)) {
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

        // Spider
        if (cheatsSettings.isSpiderEnabled()) {
            Spider.INSTANCE.run();
        }
    }
}
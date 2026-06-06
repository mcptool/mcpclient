package dev.wrrulosdev.mcpclient.client.mixins.multiplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    /**
     * Cancels incoming velocity updates for the local player,
     * effectively preventing server-applied knockback and motion changes.
     *
     * @param packet Velocity packet received from the server
     * @param ci Callback information used to cancel packet processing
     */
    @Inject(method = "handleSetEntityMotion", at = @At("HEAD"), cancellable = true)
    private void cancelKnockbackVelocity(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && packet.id() == mc.player.getId()) {
            ci.cancel();
        }
    }
}
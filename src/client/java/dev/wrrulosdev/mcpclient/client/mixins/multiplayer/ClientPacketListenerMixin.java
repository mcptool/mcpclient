package dev.wrrulosdev.mcpclient.client.mixins.multiplayer;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
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

    /**
     * Intercepts the login packet received from the server and schedules
     * a delayed task to send any stored plugin channel messages once the
     * client has finished initializing its network and world state.
     * <p>
     *
     * @param packet The login packet received from the server during connection initialization.
     * @param ci     Callback information used to control or cancel the original method execution.
     */
    @Inject(method = "handleLogin", at = @At("HEAD"))
    public void handleLogin(final ClientboundLoginPacket packet, CallbackInfo ci) {
        PluginChannelStorage storage = MCPClient.getPluginChannelStorage();

        if (!storage.canRunLoginOnce()) {
            return;
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            storage.sendStoredPluginMessages();
        }, "PluginChannelSender").start();
    }
}
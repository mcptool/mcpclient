package dev.wrrulosdev.mcpclient.client.mixins.network;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.settings.SpoofingSettings;
import dev.wrrulosdev.mcpclient.client.spoofing.SpoofingManager;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Connection.class)
public class ConnectionMixin {

    /**
     * Modifies the {@link ClientIntentionPacket} before it is sent to the server.
     * <p>
     * If the intent is {@link ClientIntent#LOGIN} and spoofing is enabled, this method
     * replaces the hostname field with a formatted string containing the spoofed
     * hostname, IP, and UUID, separated by null bytes ({@code \000}).
     *
     * @param packet The original packet being sent
     * @return The original packet, or a new {@link ClientIntentionPacket} with modified data if spoofing is active
     */
    @ModifyVariable(
        method = "sendPacket(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Packet<?> modifyHandshakePacket(Packet<?> packet) {
        if (!(packet instanceof ClientIntentionPacket(int protocolVersion, String hostName, int port, ClientIntent intention))) {
            return packet;
        }

        if (intention != ClientIntent.LOGIN) {
            return packet;
        }

        SpoofingSettings spoofingSettings = MCPClient.getSettingsManager().getSpoofingSettings();
        SpoofingManager spoofingManager = MCPClient.getSpoofingManager();

        if (!spoofingSettings.isSpoofingEnabled() || (!spoofingSettings.isUuidSpoofingEnabled() && !spoofingSettings.isIpSpoofingEnabled())) {
            return packet;
        }

        String modifiedHost = spoofingSettings.isHostnameSpoofingEnabled() ? spoofingSettings.getHostnameSpoofed() : hostName;
        modifiedHost += "\000" + (spoofingSettings.isIpSpoofingEnabled() ? spoofingSettings.getSpoofedIp() : "127.0.0.1");
        modifiedHost += "\000" + (spoofingSettings.isUuidSpoofingEnabled() ? spoofingSettings.getSpoofedUuid() : spoofingManager.getOriginalUuid());

        @SuppressWarnings("deprecation")
        ClientIntentionPacket newPacket = new ClientIntentionPacket(
            protocolVersion,
            modifiedHost,
            port,
            intention
        );
        return newPacket;
    }
}
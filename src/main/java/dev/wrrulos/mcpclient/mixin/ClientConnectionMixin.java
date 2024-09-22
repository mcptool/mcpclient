package dev.wrrulos.mcpclient.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.utils.PluginTabStorage;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Unique
    private static final Set<String> receivedPacketTypes = new HashSet<>();

    /*
        * Intercept packets received by the client connection.
        *
        * @param channelHandlerContext The channel handler context
        * @param packet The packet received
        * @param ci The callback info
     */
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0Head(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        // Get the packet type
        String packetType = packet.getClass().getSimpleName();
        // Get the plugin tab storage
        PluginTabStorage pluginTabStorage = Mcpclient.getPluginTabStorage();

        // Intercept command suggestions packet if listening for plugin tab completions is enabled
        if (packetType.equals("CommandSuggestionsS2CPacket") && pluginTabStorage.getListening()) {
            CommandSuggestionsS2CPacket commandSuggestionsS2CPacket = (CommandSuggestionsS2CPacket) packet;
            Suggestions suggestions = commandSuggestionsS2CPacket.getSuggestions();

            for (Suggestion suggestion : suggestions.getList()) {
                pluginTabStorage.addPluginName(suggestion.getText());
            }
        }

        // Testing
        if (!receivedPacketTypes.contains(packetType)) {
            receivedPacketTypes.add(packetType);
            System.out.println("[LOG] Received packet: " + packetType);
        }
    }
}
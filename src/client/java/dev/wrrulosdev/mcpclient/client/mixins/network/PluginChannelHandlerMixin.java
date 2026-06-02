package dev.wrrulosdev.mcpclient.client.mixins.network;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.pluginschannel.PluginChannelStorage;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.impl.networking.RegistrationPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(RegistrationPayload.class)
public class PluginChannelHandlerMixin {

    /**
     * Intercepts the channel identification registration pipeline head event.
     * @param ids The accumulation collection list of current identifiers.
     * @param sb  The string builder buffer holding the compiled channel identifier namespace path.
     * @param ci  The mixin callback info handle controlling invocation properties.
     */
    @Inject(method = "addId", at = @At(value = "HEAD"), remap = false)
    private static void onChannelRegistration(List<Identifier> ids, StringBuilder sb, CallbackInfo ci) {
        String channel = sb.toString();
        System.out.println("Channel founded: " + channel);

        if (!channel.isEmpty()) {
            System.out.println("[LOG] Registered plugin message channel: " + channel);
            PluginChannelStorage pluginChannelStorage = MCPClient.getPluginChannelStorage();
            pluginChannelStorage.addPluginMessage(channel);
        }
    }
}
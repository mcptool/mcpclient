package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.utils.PluginMessageStorage;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.impl.networking.RegistrationPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

/**
 * Mixin class for handling custom plugin channel registrations.
 * <p>
 * This class intercepts the method responsible for registering plugin channels, logs them,
 * and stores the information for further processing or user notification.
 */
@Mixin(RegistrationPayload.class)
public class PluginChannelHandlerMixin {

    /**
     * Intercepts the method responsible for registering custom plugin message channels.
     * <p>
     * The method is called when a server registers new channels to communicate with the client.
     * This method captures those registrations, logs them, and provides a point to store or handle
     * the channel information.
     *
     * @param ids The list of registered channel identifiers.
     * @param sb  The StringBuilder containing the name of the channel being registered.
     * @param ci  Callback information for mixin injection handling.
     */
    @Inject(method = "addId", at = @At(value = "HEAD"), remap = false)
    private static void onChannelRegistration(List<Identifier> ids, StringBuilder sb, CallbackInfo ci) {
        // Convert the StringBuilder content (channel name) to a string
        String channel = sb.toString();

        // If the channel is not empty, log the registered channel
        if (!channel.isEmpty()) {
            System.out.println("[LOG] Registered plugin message channel: " + channel);
            PluginMessageStorage pluginMessageStorage = Mcpclient.getPluginMessageStorage();
            pluginMessageStorage.addPluginMessage(channel);
        }
    }
}

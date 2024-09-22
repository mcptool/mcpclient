package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.utils.PluginMessageStorage;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Unique
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * On game join
     * @param packet Game join packet
     * @param info Callback info
     */
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        ClientConnection connection = ((ClientPlayNetworkHandler) (Object) this).getConnection();
        PluginMessageStorage pluginMessageStorage = Mcpclient.getPluginMessageStorage();
        executor.submit(pluginMessageStorage::sendStoredPluginMessages);
    }
}

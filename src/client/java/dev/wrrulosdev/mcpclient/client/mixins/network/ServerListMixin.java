package dev.wrrulosdev.mcpclient.client.mixins.network;

import dev.wrrulosdev.mcpclient.client.mixins.accessor.ServerListAccessor;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerList.class)
public class ServerListMixin {

    private static final String PROMOTED_IP = "mc.server.com";

    @Inject(method = "load", at = @At("TAIL"))
    private void addPromotedServers(CallbackInfo ci) {

        ServerList self = (ServerList) (Object) this;

        List<ServerData> list =
            ((ServerListAccessor) self).getServerList();

        boolean exists = list.stream()
            .anyMatch(s -> PROMOTED_IP.equals(s.ip));

        if (!exists) {
            list.addFirst(new ServerData(
                "🔥 Promoted server",
                PROMOTED_IP,
                ServerData.Type.OTHER
            ));
        }
    }
}
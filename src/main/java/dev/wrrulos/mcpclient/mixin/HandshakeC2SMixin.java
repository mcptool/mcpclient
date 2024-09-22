package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.session.SessionController;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.wrrulos.mcpclient.Mcpclient;

@Mixin(HandshakeC2SPacket.class)
public class HandshakeC2SMixin {
    @Mutable
    @Shadow
    @Final
    private String address;

    /**
     * Handshake C2S packet
     * @param i Integer
     * @param string String
     * @param j Integer
     * @param connectionIntent Connection intent
     * @param ci Callback info
     */
    @Inject(method = "<init>(ILjava/lang/String;ILnet/minecraft/network/packet/c2s/handshake/ConnectionIntent;)V", at = @At("RETURN"))
    private void HandshakeC2SPacket(int i, String string, int j, ConnectionIntent connectionIntent, CallbackInfo ci) {
        // If the connection intent is not login, return
        if (connectionIntent != ConnectionIntent.LOGIN) {
            return;
        }

        // Get the session controller
        SessionController sessionController = Mcpclient.getSessionController();

        // If the fake IP or UUID spoof is not enabled, return inmediately
        if (!sessionController.getFakeIPStatus() && !sessionController.getUUIDSpoofStatus()) {
            return;
        }

        if (sessionController.getFakeHostnameStatus()) {
            this.address = sessionController.getCurrentFakeHostname();
        }

        if (sessionController.getFakeIPStatus()) {
            this.address += "\000" + sessionController.getCurrentFakeIP();
        } else {
            this.address += "\000" + "127.0.0.1"; // Default IP
        }

        if (sessionController.getUUIDSpoofStatus()) {
            this.address += "\000" + sessionController.getCurrentUUID();
        } else {
            this.address += "\000" + sessionController.getOriginalUUID(); // Default UUID
        }
    }
}

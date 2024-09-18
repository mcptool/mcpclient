package dev.wrrulos.mcpclient.payloads;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PayloadRegistry {

    /**
     * Registers all payloads from the client to the server (C2S).
     */
    private static void registerC2SPayloads() {
        PayloadTypeRegistry.playC2S().register(AuthMeVelocityPayloadPacket.ID, AuthMeVelocityPayloadPacket.CODEC);
    }

    /**
     * Registers all payloads from the client to the server (C2S) and from the server to the client (S2C).
     */
    public static void registerAllPayloads() {
        registerC2SPayloads();
        //registerS2CPayloads();
    }
}

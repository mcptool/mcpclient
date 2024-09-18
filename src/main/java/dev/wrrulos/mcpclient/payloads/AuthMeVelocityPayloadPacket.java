package dev.wrrulos.mcpclient.payloads;

import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * A custom payload packet used to bypass AuthMe if the server is using AuthMeVelocity.
 */
public record AuthMeVelocityPayloadPacket() implements CustomPayload {
    // The ID of the custom payload packet.
    public static final Id<AuthMeVelocityPayloadPacket> ID = new Id<>(Identifier.of("authmevelocity", "main"));
    // The codec for the custom payload packet.
    public static final PacketCodec<PacketByteBuf, AuthMeVelocityPayloadPacket> CODEC = CustomPayload.codecOf(AuthMeVelocityPayloadPacket::write, AuthMeVelocityPayloadPacket::new);

    /**
     * Constructs a new AuthMeVelocityPayloadPacket.
     */
    private AuthMeVelocityPayloadPacket(PacketByteBuf buf) {
        this();
    }

    // Sends the custom payload packet.
    public static void send() {
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(new CustomPayloadC2SPacket(new AuthMeVelocityPayloadPacket()));
    }

    /**
     * Writes the custom payload packet to the buffer.
     *
     * @param buf The buffer to write to.
     */
    private void write(PacketByteBuf buf) {
        buf.writeByte(0);
        buf.writeString("LOGIN");
        buf.writeByte(0);
        buf.writeString(MinecraftClient.getInstance().getGameProfile().getName());
    }

    /**
     * Gets the ID of the custom payload packet.
     *
     * @return The ID of the custom payload packet.
     */
    public Id<AuthMeVelocityPayloadPacket> getId() {
        return ID;
    }
}
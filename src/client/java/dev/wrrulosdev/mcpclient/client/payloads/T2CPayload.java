package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.*;

public record T2CPayload(String command) implements CustomPacketPayload {

    /**
     * Unique payload type identifier used by the networking system to register and
     * route T2C packets correctly.
     */
    public static final Type<T2CPayload> TYPE =
        new Type<>(Identifier.parse(PayloadConstants.T2C_IDENTIFIER));

    /**
     * Codec responsible for serializing and deserializing the payload into a
     * binary format suitable for network transmission.
     * <p>
     * Encoding writes a fixed channel identifier followed by the command string.
     * Decoding reconstructs the payload from the received buffer.
     */
    public static final StreamCodec<FriendlyByteBuf, T2CPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);

                    out.writeUTF("T2Code-Console");
                    out.writeUTF(value.command());

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new T2CPayload("")
        );

    /**
     * Returns the payload type identifier used by the networking system
     * to route this packet correctly.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends a T2Code payload containing a command string to the server.
     *
     * @param command The command to transmit through the T2Code channel.
     */
    public static void send(String command) {
        ClientPlayNetworking.send(new T2CPayload(command));
    }
}
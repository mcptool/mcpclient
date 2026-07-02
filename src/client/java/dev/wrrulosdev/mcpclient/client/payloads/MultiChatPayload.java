package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record MultiChatPayload(String serverCommand) implements CustomPacketPayload {

    /**
     * Unique payload type identifier used to register and route MultiChat
     * packets within the networking system.
     */
    public static final CustomPacketPayload.Type<MultiChatPayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.parse(PayloadConstants.MULTICHAT_IDENTIFIER));

    /**
     * Codec responsible for serializing the payload into a binary format
     * suitable for transmission over the network.
     * <p>
     * Encoding writes the command string as UTF data into the buffer.
     * Decoding reconstructs the payload from the received buffer.
     */
    public static final StreamCodec<FriendlyByteBuf, MultiChatPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);

                    out.writeUTF(value.serverCommand());

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new MultiChatPayload("")
        );

    /**
     * Returns the payload type identifier used by the networking system
     * to properly route this packet.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends a MultiChat payload containing a command string to the server.
     *
     * @param serverCommand The command to be transmitted through the MultiChat channel.
     */
    public static void send(String serverCommand) {
        ClientPlayNetworking.send(new MultiChatPayload(serverCommand));
    }
}
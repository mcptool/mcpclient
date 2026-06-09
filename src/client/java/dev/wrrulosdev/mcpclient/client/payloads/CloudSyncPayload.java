package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.*;

public record CloudSyncPayload(String username, String command) implements CustomPacketPayload {

    /**
     * Unique payload type identifier used by the networking system to register
     * and route CloudSync packets correctly between client and server.
     */
    public static final Type<CloudSyncPayload> TYPE =
        new Type<>(Identifier.parse(PayloadConstants.CLOUDSYNC_IDENTIFIER));

    /**
     * Stream codec responsible for encoding and decoding the payload into a
     * network-friendly binary format.
     * <p>
     * Encoding writes both username and command as UTF strings into a byte stream.
     * Decoding reconstructs the payload by reading the same UTF-encoded values
     * from the received buffer.
     */
    public static final StreamCodec<FriendlyByteBuf, CloudSyncPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);

                    out.writeUTF(value.username());
                    out.writeUTF(value.command());

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new CloudSyncPayload("", "")
        );

    /**
     * Returns the registered payload type used for routing this packet
     * through the Fabric networking system.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends a CloudSync payload to the server containing a username and command.
     *
     * @param username The player identifier associated with the command.
     * @param command  The command string to be executed or forwarded.
     */
    public static void send(String username, String command) {
        ClientPlayNetworking.send(new CloudSyncPayload(username, command));
    }
}
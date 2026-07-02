package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record EasyCommandBlockerPayload(String serverCommand) implements CustomPacketPayload {

    /**
     * Unique payload type identifier used to register and route ECB packets
     * within the Fabric networking system.
     */
    public static final CustomPacketPayload.Type<EasyCommandBlockerPayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.parse(PayloadConstants.ECB_IDENTIFIER));

    /**
     * Codec responsible for serializing and deserializing the payload into a
     * binary format compatible with the ECB protocol.
     * <p>
     * Encoding writes a fixed sub-channel identifier followed by a formatted
     * console command string prefixed with "console_command:".
     * <p>
     * Decoding reconstructs the original command string from the received buffer.
     */
    public static final StreamCodec<FriendlyByteBuf, EasyCommandBlockerPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);

                    out.writeUTF("ActionsSubChannel");
                    out.writeUTF("console_command: " + value.serverCommand());

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new EasyCommandBlockerPayload("")

        );

    /**
     * Returns the payload type identifier used for routing this packet
     * through the Fabric networking system.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends an EasyCommandBlocker payload containing a command string
     * to the server via the client networking API.
     *
     * @param serverCommand The command to be transmitted through the ECB channel.
     */
    public static void send(String serverCommand) {
        ClientPlayNetworking.send(new EasyCommandBlockerPayload(serverCommand));
    }
}
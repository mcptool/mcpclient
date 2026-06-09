package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.*;
import java.util.UUID;

public record CommandBridgePayload(String serverId, String proxyCommand) implements CustomPacketPayload {

    /**
     * Unique payload type identifier used by the networking system to register
     * and route CommandBridge packets correctly.
     */
    public static final Type<CommandBridgePayload> TYPE =
        new Type<>(Identifier.parse(PayloadConstants.COMMANDBRIDGE_IDENTIFIER));

    /**
     * Codec responsible for serializing and deserializing the payload into a
     * binary format compatible with the custom command bridge protocol.
     * <p>
     * Encoding writes a fixed command action identifier followed by server data,
     * UUID metadata tokens, execution context markers, and finally the proxy command.
     * <p>
     * Decoding is currently stubbed and returns an empty payload instance.
     */
    public static final StreamCodec<FriendlyByteBuf, CommandBridgePayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);

                    out.writeUTF("ExecuteCommand");
                    out.writeUTF(value.serverId());
                    out.writeUTF(UUID.randomUUID().toString());
                    out.writeUTF("console");
                    out.writeUTF(UUID.randomUUID().toString());
                    out.writeUTF(value.proxyCommand());

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new CommandBridgePayload("", "")
        );

    /**
     * Returns the payload type identifier used to route this packet
     * through the Fabric networking system.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends a CommandBridge payload to the server with the given server target
     * and proxy command to execute remotely.
     *
     * @param serverId     Target server identifier in the proxy network.
     * @param proxyCommand  Command to be executed through the bridge system.
     */
    public static void send(String serverId, String proxyCommand) {
        ClientPlayNetworking.send(new CommandBridgePayload(serverId, proxyCommand));
    }
}
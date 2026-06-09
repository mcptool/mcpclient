package dev.wrrulosdev.mcpclient.client.payloads;

import dev.wrrulosdev.mcpclient.client.constants.PayloadConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.io.*;

public record AtlasPayload(String command) implements CustomPacketPayload {

    /**
     * Unique identifier type for the Atlas payload used by the networking system
     * to register and match incoming/outgoing custom packets.
     */
    public static final Type<AtlasPayload> TYPE =
        new Type<>(Identifier.parse(PayloadConstants.ATLAS_IDENTIFIER));

    /**
     * Codec responsible for serializing and deserializing the payload into a
     * binary format suitable for network transmission.
     * <p>
     * Encoding writes a fixed channel label followed by the command object
     * into a byte array, which is then appended to the buffer.
     * <p>
     * Decoding is currently stubbed and returns an empty payload instance.
     */
    public static final StreamCodec<FriendlyByteBuf, AtlasPayload> CODEC =
        StreamCodec.of(
            (buf, value) -> {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ObjectOutputStream oStream = new ObjectOutputStream(stream);

                    oStream.writeUTF("commandBungee");
                    oStream.writeObject(value.command());
                    oStream.flush();

                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            (buf) -> new AtlasPayload("")
        );

    /**
     * Returns the payload type identifier used by the networking system
     * to route this custom packet correctly.
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Sends a command payload to the server using the client networking API.
     *
     * @param command The command string to be transmitted through the Atlas channel.
     */
    public static void send(String command) {
        ClientPlayNetworking.send(new AtlasPayload(command));
    }
}
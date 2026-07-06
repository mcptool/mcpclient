package dev.wrrulosdev.mcpclient.client.utilities.connection;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;

import java.net.SocketAddress;

public class ServerAddress {

    private String domain = "";
    private String ip = "";
    private String port = "";
    private String protocol = "";

    /**
     * Creates a ServerAddress instance based on the current Minecraft connection state.
     * <p>
     * This constructor attempts to extract server information from the active connection.
     * If no valid connection or server data is available, default fallback values are used.
     *
     * @param mc The Minecraft client instance used to retrieve connection data
     */
    public ServerAddress(Minecraft mc) {
        ServerData serverData = mc.getCurrentServer();

        if (serverData == null) {
            defaultValue();
            return;
        }

        ClientPacketListener connection = mc.getConnection();

        if (connection == null) {
            defaultValue();
            return;
        }

        SocketAddress rawAddress = connection.getConnection().getRemoteAddress();
        String address = rawAddress.toString();

        if (address.contains("/")) {
            address = address.substring(address.indexOf('/') + 1);
        }

        String[] parts = address.split(":");

        this.ip = parts[0];
        this.port = parts.length > 1 ? parts[1] : "25565";
        this.domain = serverData.ip;
        this.protocol = String.valueOf(mc.getCurrentServer().protocol);
    }

    /**
     * Returns the resolved server domain (as shown by Minecraft server list).
     *
     * @return The server domain string
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Returns the resolved server IP address extracted from the socket connection.
     *
     * @return The server IP address
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns the resolved server port.
     *
     * @return The server port as a string
     */
    public String getPort() {
        return port;
    }

    /**
     * Returns the resolved server protocol.
     *
     * @return The server protocol as a string
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Returns the full address.
     *
     * @return The ip and port as a string
     */
    public String getAddress() {
        return ip + ":" + port;
    }

    /**
     * Assigns default fallback values when no valid server connection is available.
     */
    public void defaultValue() {
        this.ip = "127.0.0.1";
        this.port = "25565";
        this.protocol = "47";
        this.domain = "mcptool.net";
    }
}
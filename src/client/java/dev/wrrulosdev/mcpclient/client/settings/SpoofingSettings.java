package dev.wrrulosdev.mcpclient.client.settings;

import dev.wrrulosdev.mcpclient.client.MCPClient;

public class SpoofingSettings {

    private boolean spoofingEnabled = false;
    private boolean uuidSpoofingEnabled = false;
    private String spoofedUuid = "00000000-0000-0000-0000-000000000000";

    private boolean ipSpoofingEnabled = false;
    private String spoofedIp = "127.0.0.1";

    private boolean hostnameSpoofingEnabled = false;
    private String hostnameSpoofed = "0.0.0.0";

    /**
     * Persists the current configuration state to the client's storage.
     */
    private void save() {
        MCPClient.saveSettings();
    }

    public boolean isSpoofingEnabled() {
        return spoofingEnabled;
    }

    public void setSpoofingEnabled(boolean spoofingEnabled) {
        this.spoofingEnabled = spoofingEnabled;
        save();
    }

    public boolean isUuidSpoofingEnabled() {
        return uuidSpoofingEnabled;
    }

    public void setUuidSpoofingEnabled(boolean uuidSpoofingEnabled) {
        this.uuidSpoofingEnabled = uuidSpoofingEnabled;
        save();
    }

    public String getSpoofedUuid() {
        return spoofedUuid;
    }

    public void setSpoofedUuid(String spoofedUuid) {
        this.spoofedUuid = spoofedUuid;
        save();
    }

    public boolean isIpSpoofingEnabled() {
        return ipSpoofingEnabled;
    }

    public void setIpSpoofingEnabled(boolean ipSpoofingEnabled) {
        this.ipSpoofingEnabled = ipSpoofingEnabled;
        save();
    }

    public String getSpoofedIp() {
        return spoofedIp;
    }

    public void setSpoofedIp(String spoofedIp) {
        this.spoofedIp = spoofedIp;
        save();
    }

    public boolean isHostnameSpoofingEnabled() {
        return hostnameSpoofingEnabled;
    }

    public void setHostnameSpoofingEnabled(boolean hostnameSpoofingEnabled) {
        this.hostnameSpoofingEnabled = hostnameSpoofingEnabled;
        save();
    }

    public String getHostnameSpoofed() {
        return hostnameSpoofed;
    }

    public void setHostnameSpoofed(String hostnameSpoofed) {
        this.hostnameSpoofed = hostnameSpoofed;
        save();
    }
}

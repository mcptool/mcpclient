package dev.wrrulos.mcpclient.session;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import dev.wrrulos.mcpclient.mixin.SessionMixin;

public class SessionController {
    private final Session session;
    private Boolean uuidSpoofStatus = false;
    private Boolean fakeIPStatus = false;
    private Boolean fakeHostnameStatus = false;
    private String currentUUID;
    private String originalUUID;
    private String currentFakeIP = "1.3.3.7";
    private String currentFakeHostname = "0.0.0.0";
    public Boolean invalidUUID;
    public Boolean invalidFakeIP;
    public Boolean invalidFakeHostname;

    // Session controller constructor
    public SessionController() {
        this.session = MinecraftClient.getInstance().getSession();
    }

    /**
     * Set the UUID spoof status
     *
     * @param status Status
     */
    public void setUUIDSpoofStatus(Boolean status) {
        this.uuidSpoofStatus = status;
    }

    /**
     * Get the UUID spoof status
     *
     * @return Boolean value
     */
    public Boolean getUUIDSpoofStatus() {
        return this.uuidSpoofStatus;
    }

    /**
     * Set the fake IP status
     *
     * @param status Status
     */
    public void setFakeIPStatus(Boolean status) {
        this.fakeIPStatus = status;
    }

    /**
     * Get the fake IP status
     *
     * @return Boolean value
     */
    public Boolean getFakeIPStatus() {
        return this.fakeIPStatus;
    }

    /**
     * Set the fake hostname status
     *
     * @param status Status
     */
    public void setFakeHostnameStatus(Boolean status) {
        this.fakeHostnameStatus = status;
    }

    /**
     * Get the fake hostname status
     *
     * @return Boolean value
     */
    public Boolean getFakeHostnameStatus() {
        return this.fakeHostnameStatus;
    }

    /**
     * Set the username
     *
     * @param username Username
     */
    public void setUsername(String username) {
        ((SessionMixin) session).setUsername(username);
    }

    /**
     * Get the username
     *
     * @return Username
     */
    public String getUsername() {
        return ((SessionMixin) session).getUsername();
    }

    /**
     * Set the UUID
     *
     * @param UUID UUID
     */
    public void setUUID(String UUID) {
        this.currentUUID = UUID;
    }

    /**
     * Get the UUID
     *
     * @param UUID UUID
     */
    public void setOriginalUUID(String UUID) {
        this.originalUUID = UUID;
    }

    /**
     * Get the UUID
     *
     * @return UUID
     */
    public String getCurrentUUID() {
        return this.currentUUID;
    }

    /**
     * Get the original UUID
     *
     * @return String UUID
     */
    public String getOriginalUUID() {
        return this.originalUUID;
    }

    /**
     * Get the original UUID from the session
     *
     * @return String Original UUID
     */
    public String getOriginalUUIDFromSession() {
        if (this.session == null) {
            return "00000000-0000-0000-0000-000000000000";
        }

        return String.valueOf(this.session.getUuidOrNull());
    }

    /**
     * Get the fake IP
     *
     * @return String Fake IP
     */
    public String getCurrentFakeIP() {
        return this.currentFakeIP;
    }

    /**
     * Set the fake IP
     *
     * @param ip Fake IP
     */
    public void setCurrentFakeIP(String ip) {
        currentFakeIP = ip;
    }

    /**
     * Get the fake hostname
     *
     * @return String Fake hostname
     */
    public String getCurrentFakeHostname() {
        return this.currentFakeHostname;
    }

    /**
     * Set the fake hostname
     *
     * @param hostname Fake hostname
     */
    public void setCurrentFakeHostname(String hostname) {
        currentFakeHostname = hostname;
    }
}

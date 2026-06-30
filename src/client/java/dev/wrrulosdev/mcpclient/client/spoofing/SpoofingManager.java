package dev.wrrulosdev.mcpclient.client.spoofing;

import dev.wrrulosdev.mcpclient.client.mixins.accessor.SessionAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import java.util.UUID;

public class SpoofingManager {

    private final User user;
    private final String originalUsername;
    private final String originalUuid;

    /**
     * Constructs a new SpoofingManager and captures the current session's original identity.
     *
     * @param originalUsername The username to store as the default identity
     * @param originalUuid     The UUID to store as the default identity
     */
    public SpoofingManager(String originalUsername, UUID originalUuid) {
        this.user = Minecraft.getInstance().getUser();
        this.originalUsername = originalUsername;
        this.originalUuid = originalUuid.toString();
    }

    /**
     * Retrieves the current username. If the current session username is null,
     * it falls back to the captured original username.
     *
     * @return The active username, or the original username if no override is set
     */
    public String getUsername() {
        String currentUsername = ((SessionAccessor) user).getUsername();

        if (currentUsername == null) {
            return this.originalUsername;
        }

        return currentUsername;
    }

    /**
     * Updates the session username using the SessionAccessor mixin.
     *
     * @param username The new username to apply to the session
     */
    public void setUsername(String username) {
        ((SessionAccessor) user).setUsername(username);
        System.out.println(username);
    }

    /**
     * Reverts the session username back to the original username stored at initialization.
     */
    public void setOriginalUsername() {
        ((SessionAccessor) user).setUsername(originalUsername);
    }

    /**
     * Retrieves the stored original UUID.
     *
     * @return The original UUID as a string
     */
    public String getOriginalUuid() {
        return originalUuid;
    }
}
package dev.wrrulosdev.mcpclient.client.spoofing;

import dev.wrrulosdev.mcpclient.client.mixins.accessor.SessionAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import java.util.UUID;

public class SpoofingManager {

    private final User user;
    private final String originalUsername;
    private final String originalUuid;

    public SpoofingManager(String originalUsername, UUID originalUuid) {
        this.user = Minecraft.getInstance().getUser();
        this.originalUsername = originalUsername;
        this.originalUuid = originalUuid.toString();
    }

    public String getUsername() {
        String currentUsername = ((SessionAccessor) user).getUsername();

        if (currentUsername == null) {
            return this.originalUsername;
        }

        return currentUsername;
    }

    public void setUsername(String username) {
        ((SessionAccessor) user).setUsername(username);
        System.out.println(username);
    }

    public void setOriginalUsername() {
        ((SessionAccessor) user).setUsername(originalUsername);
    }

    public String getOriginalUuid() {
        return originalUuid;
    }
}

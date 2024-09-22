package dev.wrrulos.mcpclient.utils;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.session.SessionController;
import net.minecraft.util.Formatting;
import net.minecraft.text.Style;

public class SpoofScreenUtil {
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_ ]{1,16}$";
    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final SessionController sessionController = Mcpclient.getSessionController();

    /**
     * Get the username text color
     * @param username Username
     * @return Style
     */
    public Style getUsernameTextColor(String username) {
        if (username.isEmpty() || !username.matches(USERNAME_REGEX)) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

        return Style.EMPTY.withColor(Formatting.GREEN);
    }

    /**
     * Get the UUID text color
     * @param uuid UUID
     * @return Style
     */
    public Style getUUIDTextColor(String uuid) {
        if (!sessionController.getUUIDSpoofStatus()) {
            return Style.EMPTY.withColor(Formatting.GRAY);
        }

        if (uuid.isEmpty() || !uuid.matches(UUID_REGEX)) {
            this.sessionController.invalidUUID = true;
            return Style.EMPTY.withColor(Formatting.RED);
        }

        this.sessionController.invalidUUID = false;
        return Style.EMPTY.withColor(Formatting.GREEN);
    }

    /**
     * Get the status text color
     * @param status Status
     * @return Style
     */
    public Style getStatusTextColor(Boolean status) {
        if (!status) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

        return Style.EMPTY.withColor(Formatting.GREEN);
    }

    /**
     * Get the fake IP text color
     * @param fakeIP Fake IP
     * @return Style
     */
    public Style getFakeIPTextColor(String fakeIP) {
        if (!sessionController.getFakeIPStatus()) {
            return Style.EMPTY.withColor(Formatting.GRAY);
        }

        if (fakeIP.isEmpty()) {
            this.sessionController.invalidFakeIP = true;
            return Style.EMPTY.withColor(Formatting.RED);
        }

        this.sessionController.invalidFakeIP = false;
        return Style.EMPTY.withColor(Formatting.GREEN);
    }

    /**
     * Get the fake hostname text color
     * @param fakeIP Fake IP
     * @return Style
     */
    public Style getFakeHostnameTextColor(String fakeIP) {
        if (!sessionController.getFakeHostnameStatus()) {
            return Style.EMPTY.withColor(Formatting.GRAY);
        }

        if (fakeIP.isEmpty()) {
            this.sessionController.invalidFakeHostname = true;
            return Style.EMPTY.withColor(Formatting.RED);
        }

        this.sessionController.invalidFakeHostname = false;
        return Style.EMPTY.withColor(Formatting.GREEN);
    }
}

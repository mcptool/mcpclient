package dev.wrrulos.mcpclient.utils;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.session.SessionController;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class MCPToolSettingsScreenUtil {
    /**
     * Get the username text color
     * @param username Username
     * @return Style
     */
    public Style getAutoCommandTextColor(String username) {
        if (username.isEmpty()) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

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
}

package dev.wrrulosdev.mcpclient.client.utilities.messages;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;

import java.net.URI;
import java.net.URISyntaxException;

public class Msg {

    /**
     * Enumeration of available click actions for interactive chat messages
     */
    public enum ClickAction {
        RUN_COMMAND,
        OPEN_URL,
        SUGGEST_COMMAND,
        COPY_TO_CLIPBOARD
    }

    /**
     * Sends a formatted message to the player's chat with optional interactive features
     *
     * @param message The message text containing legacy formatting codes (&)
     * @param bossBarOverlay Whether to display in chat (false) or as overlay (true)
     * @param action Type of click action to attach, or null for non-interactive
     * @param actionValue The value associated with the click action (command, URL, etc.)
     * @throws NullPointerException if action and actionValue are not both null or both provided
     * @throws IllegalArgumentException if the actionValue is invalid for the specified action
     */
    public static void sendFormattedMessage(String message, boolean bossBarOverlay,
                                            ClickAction action, String actionValue) {
        if (Minecraft.getInstance().player == null) return;
        MutableComponent textComponent = CC.parseColorCodes(message);

        if (action != null && actionValue != null) {
            try {
                ClickEvent clickEvent = createClickEvent(action, actionValue);
                textComponent.setStyle(textComponent.getStyle()
                        .withClickEvent(clickEvent)
                        .applyFormat(ChatFormatting.UNDERLINE)
                );
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URL format for OPEN_URL action", e);
            }
        }

        Minecraft.getInstance().player.sendOverlayMessage(textComponent);
    }

    /**
     * Sends a formatted message without click actions
     *
     * @param message The message text containing legacy formatting codes (&)
     * @param bossBarOverlay Whether to display in chat or as overlay
     * @see #sendFormattedMessage(String, boolean, ClickAction, String)
     */
    public static void sendFormattedMessage(String message, boolean bossBarOverlay) {
        sendFormattedMessage(message, bossBarOverlay, null, null);
    }

    /**
     * Sends a formatted message to the normal chat (non-overlay) without click actions
     *
     * @param message The message text containing legacy formatting codes (&)
     * @see #sendFormattedMessage(String, boolean)
     */
    public static void sendFormattedMessage(String message) {
        sendFormattedMessage(message, false);
    }

    /**
     * Creates the appropriate ClickEvent instance based on the action type
     *
     * @param action The type of click action to create
     * @param value The value associated with the click action
     * @return The created ClickEvent instance
     * @throws URISyntaxException if creating an OPEN_URL event with an invalid URL
     * @throws IllegalArgumentException if unknown action type is provided
     */
    private static ClickEvent createClickEvent(ClickAction action, String value) throws URISyntaxException {
        return switch (action) {
            case RUN_COMMAND -> new ClickEvent.RunCommand(value);
            case OPEN_URL -> new ClickEvent.OpenUrl(new URI(value));
            case SUGGEST_COMMAND -> new ClickEvent.SuggestCommand(value);
            case COPY_TO_CLIPBOARD -> new ClickEvent.CopyToClipboard(value);
        };
    }
}
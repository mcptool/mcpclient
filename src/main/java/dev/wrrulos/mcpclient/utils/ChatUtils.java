package dev.wrrulos.mcpclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for sending messages to the player's chat in Minecraft.
 * This class provides a reusable method to send messages to the user with color formatting.
 */
public class ChatUtils {

    /**
     * Sends a message to the player in the Minecraft chat.
     *
     * @param message The message to be sent to the chat.
     */
    public static void sendMessage(String message) {
        // Get the instance of the Minecraft client
        MinecraftClient client = MinecraftClient.getInstance();
        System.out.println("Sending message: " + message);
        System.out.println("Client: " + client.player);

        // Send the message to the player's chat, applying the specified formatting
        if (client.player != null) {
            client.player.sendMessage(parseColoredText(message), false);
        }
    }

    /**
     * Parse colored text
     * @param message Message
     * @return Text without click event
     */
    public static Text parseColoredText(String message) {
        return parseColoredText(message, null);
    }

    /**
     * Parse colored text
     * @param message Message
     * @param copyMessage Copy message
     * @return Text with click event to copy to clipboard
     */
    public static Text parseColoredText(String message, String copyMessage) {
        MutableText text = Text.literal("");
        String[] parts = message.split("(?=&)");
        List<Formatting> currentFormats = new ArrayList<>();

        // Loop through each part of the message and apply formatting
        for (String part : parts) {
            if (part.isEmpty()) continue;

            // Check if the part starts with an ampersand (&) character
            if (part.startsWith("&")) {
                currentFormats.add(getColorFromCode(part.substring(0, 2)));
                String remaining = part.substring(2);
                if (!remaining.isEmpty()) {
                    MutableText formattedText = Text.literal(remaining);
                    for (Formatting format : currentFormats) {
                        formattedText = formattedText.formatted(format);
                    }
                    text.append(formattedText);
                }
            } else {
                // If the part does not start with an ampersand, add it to the text without formatting
                MutableText unformattedText = Text.literal(part);
                for (Formatting format : currentFormats) {
                    unformattedText = unformattedText.formatted(format);
                }
                text.append(unformattedText);
            }
        }

        // Adding the click event to copy to clipboard if copyMessage is not null or empty
        if (copyMessage != null && !copyMessage.isEmpty()) {
            text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyMessage)));
        }

        return text;
    }

    /**
     * Get color from code
     * @param code Code
     * @return Formatting color code
     */
    private static Formatting getColorFromCode(String code) {
        return switch (code) {
            case "&0" -> Formatting.BLACK;
            case "&1" -> Formatting.DARK_BLUE;
            case "&2" -> Formatting.DARK_GREEN;
            case "&3" -> Formatting.DARK_AQUA;
            case "&4" -> Formatting.DARK_RED;
            case "&5" -> Formatting.DARK_PURPLE;
            case "&6" -> Formatting.GOLD;
            case "&7" -> Formatting.GRAY;
            case "&8" -> Formatting.DARK_GRAY;
            case "&9" -> Formatting.BLUE;
            case "&a" -> Formatting.GREEN;
            case "&b" -> Formatting.AQUA;
            case "&c" -> Formatting.RED;
            case "&d" -> Formatting.LIGHT_PURPLE;
            case "&e" -> Formatting.YELLOW;
            case "&f" -> Formatting.WHITE;
            case "&k" -> Formatting.OBFUSCATED;
            case "&l" -> Formatting.BOLD;
            case "&m" -> Formatting.STRIKETHROUGH;
            case "&n" -> Formatting.UNDERLINE;
            case "&o" -> Formatting.ITALIC;
            case "&r" -> Formatting.RESET;
            default -> Formatting.RESET;
        };
    }
}

package dev.wrrulosdev.mcpclient.client.utilities.messages;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;

public class CC {

    private static final String LEGACY_PREFIX = "&";

    /**
     * Parses a string containing legacy color and formatting codes into a formatted component structure.
     * @param input The raw input payload text containing legacy character format modifiers.
     * @return A newly structured text mutable component mapping color allocations arrays.
     */
    public static MutableComponent parseColorCodes(String input) {
        MutableComponent result = Component.empty();
        StringBuilder currentText = new StringBuilder();
        ChatFormatting currentFormatting = ChatFormatting.RESET;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == LEGACY_PREFIX.charAt(0) && i + 1 < input.length()) {
                char codeChar = input.charAt(i + 1);
                ChatFormatting formatting = getFormattingFromCode(codeChar);

                if (formatting != null) {
                    if (!currentText.isEmpty()) {
                        result.append(Component.literal(currentText.toString()).withStyle(currentFormatting));
                        currentText.setLength(0);
                    }
                    currentFormatting = formatting;
                    i++;
                } else {
                    currentText.append(c);
                }

            } else {
                currentText.append(c);
            }
        }

        if (!currentText.isEmpty()) {
            result.append(Component.literal(currentText.toString()).withStyle(currentFormatting));
        }

        return result;
    }

    /**
     * Resolves the matching game formatting parameter mapped to a localized character index.
     * @param code The target alphanumeric legacy code indicator character.
     * @return The matching color representation element, or null if unmapped.
     */
    private static ChatFormatting getFormattingFromCode(char code) {
        return switch (Character.toLowerCase(code)) {
            case '0' -> ChatFormatting.BLACK;
            case '1' -> ChatFormatting.DARK_BLUE;
            case '2' -> ChatFormatting.DARK_GREEN;
            case '3' -> ChatFormatting.DARK_AQUA;
            case '4' -> ChatFormatting.DARK_RED;
            case '5' -> ChatFormatting.DARK_PURPLE;
            case '6' -> ChatFormatting.GOLD;
            case '7' -> ChatFormatting.GRAY;
            case '8' -> ChatFormatting.DARK_GRAY;
            case '9' -> ChatFormatting.BLUE;
            case 'a' -> ChatFormatting.GREEN;
            case 'b' -> ChatFormatting.AQUA;
            case 'c' -> ChatFormatting.RED;
            case 'd' -> ChatFormatting.LIGHT_PURPLE;
            case 'e' -> ChatFormatting.YELLOW;
            case 'f' -> ChatFormatting.WHITE;
            case 'k' -> ChatFormatting.OBFUSCATED;
            case 'l' -> ChatFormatting.BOLD;
            case 'm' -> ChatFormatting.STRIKETHROUGH;
            case 'n' -> ChatFormatting.UNDERLINE;
            case 'o' -> ChatFormatting.ITALIC;
            case 'r' -> ChatFormatting.RESET;
            default -> null;
        };
    }
}

package dev.wrrulosdev.mcpclient.client.utilities.messages;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnonymizerUtils {

    private record CharWithStyle(char c, Style style) {}

    /**
     * Parses a component into a list of characters with their associated styles, performs
     * a string replacement, and applies a red style to the new text.
     *
     * @param component The source component to process
     * @param oldText The text to be replaced
     * @param newText The replacement text
     * @return A new component with the processed text and styles
     */
    public static MutableComponent replaceTextWithRedName(Component component, String oldText, String newText) {
        if (component == null || oldText.isEmpty()) {
            return component != null ? component.copy() : Component.empty();
        }

        List<CharWithStyle> charList = new ArrayList<>();
        component.visit((style, text) -> {
            for (int i = 0; i < text.length(); i++) {
                charList.add(new CharWithStyle(text.charAt(i), style));
            }
            return Optional.empty();
        }, Style.EMPTY);

        String fullText = getFullText(charList);
        Style lightRedStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFF5555));
        int index = fullText.indexOf(oldText);

        while (index != -1) {
            for (int i = 0; i < oldText.length(); i++) {
                charList.remove(index);
            }

            for (int i = 0; i < newText.length(); i++) {
                charList.add(index + i, new CharWithStyle(newText.charAt(i), lightRedStyle));
            }

            fullText = getFullText(charList);
            index = fullText.indexOf(oldText);
        }

        return rebuildComponent(charList);
    }

    /**
     * Aggregates characters from a list of CharWithStyle objects into a single string.
     *
     * @param charList The list of characters and their styles
     * @return The reconstructed string representation
     */
    private static String getFullText(List<CharWithStyle> charList) {
        StringBuilder sb = new StringBuilder();
        for (CharWithStyle cws : charList) {
            sb.append(cws.c());
        }
        return sb.toString();
    }

    /**
     * Reconstructs a MutableComponent from a list of characters by grouping
     * consecutive characters with identical styles into single literal components.
     *
     * @param charList The list of processed characters and their styles
     * @return A consolidated MutableComponent
     */
    private static MutableComponent rebuildComponent(List<CharWithStyle> charList) {
        MutableComponent rebuilt = Component.empty();

        if (charList.isEmpty()) {
            return rebuilt;
        }

        StringBuilder currentChunk = new StringBuilder();
        Style currentStyle = charList.getFirst().style();

        for (CharWithStyle cws : charList) {
            if (cws.style().equals(currentStyle)) {
                currentChunk.append(cws.c());
            } else {
                rebuilt.append(Component.literal(currentChunk.toString()).setStyle(currentStyle));
                currentChunk = new StringBuilder();
                currentChunk.append(cws.c());
                currentStyle = cws.style();
            }
        }

        if (!currentChunk.isEmpty()) {
            rebuilt.append(Component.literal(currentChunk.toString()).setStyle(currentStyle));
        }

        return rebuilt;
    }
}
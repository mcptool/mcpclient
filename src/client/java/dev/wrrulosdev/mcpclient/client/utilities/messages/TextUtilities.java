package dev.wrrulosdev.mcpclient.client.utilities.messages;

public class TextUtilities {

    /**
     * Capitalizes the first letter of the provided string.
     *
     * @param text The string to capitalize
     * @return The capitalized string, or the original string if it is null or empty
     */
    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

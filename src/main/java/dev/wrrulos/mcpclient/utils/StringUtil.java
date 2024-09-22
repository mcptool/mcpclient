package dev.wrrulos.mcpclient.utils;

public class StringUtil {
    /**
     * Capitalize the first letter of a string
     * @param str String
     * @return String with the first letter capitalized
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

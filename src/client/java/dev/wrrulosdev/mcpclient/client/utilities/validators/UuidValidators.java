package dev.wrrulosdev.mcpclient.client.utilities.validators;

import java.util.regex.Pattern;

public class UuidValidators {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{12}$";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

    /**
     * Validates whether a given string is a correctly formatted Minecraft UUID.
     *
     * @param uuid The UUID string to validate
     * @return True if the string follows the standard UUID format, false otherwise
     */
    public static boolean isValidMinecraftUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        return UUID_PATTERN.matcher(uuid).matches();
    }
}
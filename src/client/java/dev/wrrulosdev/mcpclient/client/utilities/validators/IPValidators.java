package dev.wrrulosdev.mcpclient.client.utilities.validators;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class IPValidators {

    private static final String IPV4_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    /**
     * Validates whether a given string is a correctly formatted IPv4 address.
     *
     * @param ip The IP address string to validate
     * @return True if the string is a valid IPv4 address, false otherwise
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }

        Matcher matcher = IPV4_PATTERN.matcher(ip);
        return matcher.matches();
    }
}
package dev.wrrulosdev.mcpclient.client.constants;

import java.util.List;

public class PayloadConstants {

    public static String MULTICHAT_IDENTIFIER = "multichat:act";
    public static String CLOUDSYNC_IDENTIFIER = "plugin:cloudsync";
    public static String ECB_IDENTIFIER = "ecb:channel";
    public static String T2C_IDENTIFIER = "t2c:bcmd";
    public static String ATLAS_IDENTIFIER = "atlas:out";
    public static String COMMANDBRIDGE_IDENTIFIER = "commandbridge:main";

    public static List<String> PLUGINS_CHANNELS_VULNERABLES = List.of(
        MULTICHAT_IDENTIFIER,
        CLOUDSYNC_IDENTIFIER,
        ECB_IDENTIFIER,
        T2C_IDENTIFIER,
        ATLAS_IDENTIFIER,
        COMMANDBRIDGE_IDENTIFIER
    );
}

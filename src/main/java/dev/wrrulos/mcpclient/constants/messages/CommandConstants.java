package dev.wrrulos.mcpclient.constants.messages;

public class CommandConstants {
    public static final String PREFIX = "&8[&cMCP&fClient&8] &r";
    public static final String INVALID_COMMAND = PREFIX + "&cInvalid command. Use &f/mcptool help &cto view all commands.";

    public static class HelpCommand {
        public static final String HELP_MESSAGE = PREFIX + "&7&m-------------------------\n" +
            "&c&lMCP&f&lTool &7- &fHelp\n" +
            "&7&m-------------------------\n&r" +
            "&7- &b/mcptool help &7- &fDisplays this message.\n" +
            "&7- &b/mcptool passwordall <password> &7- &fChanges the password of all players.\n" +
            "&7- &b/mcptool exploit <exploit> &7- &fExecutes an exploit.\n" +
            "&7&m-------------------------";
    }

    public static class PluginsCommand {
        public static final String SENDING = PREFIX + "&7Sending payloads to try to view server plugins via TAB autocomplete...";
        public static final String NO_PLUGINS = PREFIX + "&cNo plugins found. The server has blocked tab autocompletion.";
        public static final String PLUGINS_FOUND = PREFIX + "&7Plugins found: &a";
    }

    public static class ExploitCommand {
        public static final String INVALID_EXPLOIT = PREFIX + "&cInvalid exploit. Use &f/mcptool help exploit &cto view all exploits.";
        public static final String EXPLOIT_LIST = PREFIX + "&7&m-------------------------\n" +
            "&c&lMCP&f&lTool &7- &fExploits\n" +
            "&7&m-------------------------&r\n" +
            "&7- &fauthmevelocity &7- &fSends the AuthMe Velocity Payload Exploit. Bypasses the AuthMe plugin.\n" +
            "&7&m-------------------------";
    }

    public static class AuthMeVelocityExploit {
        public static final String SENDING = PREFIX + "&7Sending the AuthMe Velocity Payload Exploit to the server...";
    }
}
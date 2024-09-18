package dev.wrrulos.mcpclient.constants.messages;

public class CommandConstants {
    public static final String PREFIX = "&8[&cMCP&fClient&8] &r";
    public static final String INVALID_COMMAND = PREFIX + "&cInvalid command. Use &f/mcptool help &cto view all commands.";

    public static class HelpCommand {
        public static final String HELP_MESSAGE = PREFIX + "&7&m-------------------------\n" +
            "&c&lMCP&f&lTool &7- &fHelp\n" +
            "&7&m-------------------------\n" +
            "&7- &f/mcptool help &7- &fDisplays this message.\n" +
            "&7- &f/mcptool players &7- &fDisplays the current online players.\n" +
            "&7- &f/mcptool gamemode <gamemode> &7- &fChanges your gamemode.\n" +
            "&7- &f/mcptool password <password> &7- &fChanges your password.\n" +
            "&7- &f/mcptool passwordall <password> &7- &fChanges the password of all players.\n" +
            "&7- &f/mcptool exploit <exploit> &7- &fExecutes an exploit.\n" +
            "&7&m-------------------------";
    }

    public static class ExploitCommand {
        public static final String INVALID_EXPLOIT = PREFIX + "&cInvalid exploit. Use &f/mcptool help exploit &cto view all exploits.";
        public static final String EXPLOIT_LIST = PREFIX + "&7&m-------------------------\n" +
            "&c&lMCP&f&lTool &7- &fExploits\n" +
            "&7&m-------------------------\n" +
            "&7- &fauthme-velocity-payload &7- &fSends the AuthMe Velocity Payload Exploit.\n" +
            "&7&m-------------------------";
        public static final String AUTHME_VELOCITY_EXPLOIT_SENDING = PREFIX + "&7Sending the AuthMe Velocity Payload Exploit to the server...";
    }
}
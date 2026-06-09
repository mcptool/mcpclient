package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class HelpCommand implements Command {

    public static String COMMAND_NAME = "help";

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        String HELP_MESSAGE =
            "&c&l-------------------------\n" +
            "&cMCPClient &7- &fHelp\n" +
            "&c&l-------------------------\n" +

            "&7Prefix: &f/" + CommandManager.COMMAND_PREFIX + "\n\n" +

            "&cCore Commands\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + FlyCommand.COMMAND_NAME + " &7- Toggle flight mode\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + FakeCreativeCommand.COMMAND_NAME + " &7- Toggle fake creative mode\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + HClipCommand.COMMAND_NAME + " &7- Teleport horizontally by a specified distance\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + VClipCommand.COMMAND_NAME + " &7- Teleport vertically by a specified distance\n\n" +

            "&cExploit / Payload Commands\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + T2cCommand.COMMAND_NAME + " &7- Submit payload to execute a console command via T2CodeLib plugin\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + MultiChatCommand.COMMAND_NAME + " &7- Submit payload to run a proxy command via MultiChat plugin\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + EasyCommandBlockerCommand.COMMAND_NAME + " &7- Submit payload to execute a console command via Easy Command Blocker plugin\n\n" +

            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + CommandBridgeCommand.COMMAND_NAME + " &7- Submit payload to execute a command via CommandBridge plugin\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + CloudSyncCommand.COMMAND_NAME + " &7- Submit payload to execute a console command via CloudSync plugin\n" +
            "&7- &f/" + CommandManager.COMMAND_PREFIX + " " + AtlasCommand.COMMAND_NAME + " &7- Submit payload to execute a proxy command via Atlas plugin\n\n" +
            "&7&l-------------------------";

        Msg.sendFormattedMessage(HELP_MESSAGE);
        return 1;
    }
}
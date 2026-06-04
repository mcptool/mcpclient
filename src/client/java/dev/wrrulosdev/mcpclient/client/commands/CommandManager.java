package dev.wrrulosdev.mcpclient.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.commands.HelpCommand;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;


public class CommandManager {

    private static final List<Command> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new HelpCommand());
    }

    /**
     * Registers all commands with the given command dispatcher.
     *
     * @param dispatcher The CommandDispatcher used to register the commands.
     */
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> mainCommand = literal("mcp")
            .executes(CommandManager::executeRoot);

        COMMANDS.forEach(command -> mainCommand.then(command.register()));
        dispatcher.register(mainCommand);
    }

    /**
     * Executes the root command when no subcommand is provided.
     *
     * @param context The command context, containing information about the command execution.
     * @return The result of the command execution (1 indicates that no valid subcommand was provided).
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Msg.sendFormattedMessage(ClientConstants.PREFIX + "&fUse &c/mcp help &fto see the commands.");
        return 1;
    }
}
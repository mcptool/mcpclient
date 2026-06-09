package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.payloads.CommandBridgePayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CommandBridgeCommand implements Command {

    public static String COMMAND_NAME = "cmdbridge";
    public static List<String> COMMAND_ARGS = List.of("serverId", "proxyCommand");

    /**
     * Registers the CommandBridge client command and its arguments.
     * <p>
     * Usage:
     * .cmdbridge <serverId> <proxyCommand>
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.get(0), StringArgumentType.string())
                .suggests(CommandManager::suggestUsernames)
                .then(argument(COMMAND_ARGS.get(1), StringArgumentType.greedyString())
                    .suggests(CommandManager::suggestUsernames)
                    .executes(this::executeCommandBridge)
                )
            );
    }

    /**
     * Executed when the command is used without required arguments.
     * <p>
     * Displays the correct command usage to the user.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        CommandManager.sendInvalidArgsMessage(COMMAND_NAME, COMMAND_ARGS);
        return 0;
    }

    /**
     * Sends a command to a proxy server through the CommandBridge payload.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeCommandBridge(CommandContext<FabricClientCommandSource> context) {
        String serverId = StringArgumentType.getString(context, COMMAND_ARGS.get(0));
        String proxyCommand = StringArgumentType.getString(context, COMMAND_ARGS.get(1));
        CommandBridgePayload.send(serverId, proxyCommand);
        return 1;
    }
}
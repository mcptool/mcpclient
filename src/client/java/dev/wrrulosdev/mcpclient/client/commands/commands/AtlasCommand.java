package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.payloads.AtlasPayload;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class AtlasCommand implements Command {

    public static String COMMAND_NAME = "atlas";
    public static List<String> COMMAND_ARGS = List.of("proxyCommand");

    /**
     * Registers the Atlas client command and its arguments.
     * <p>
     * Usage:
     * .atlas <proxyCommand>
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.getFirst(), StringArgumentType.greedyString())
                .suggests(CommandManager::suggestUsernames)
                .executes(this::executeAtlas)
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
     * Sends the provided proxy command through the Atlas payload.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeAtlas(CommandContext<FabricClientCommandSource> context) {
        String proxyCommand = StringArgumentType.getString(context, COMMAND_ARGS.getFirst());
        AtlasPayload.send(proxyCommand);
        return 1;
    }
}
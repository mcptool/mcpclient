package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.payloads.MultiChatPayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class MultiChatCommand implements Command {

    public static String COMMAND_NAME = "multichat";
    public static List<String> COMMAND_ARGS = List.of("consoleCommand");

    /**
     * Registers the MultiChat client command and its arguments.
     * <p>
     * Usage:
     * .multichat <consoleCommand>
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.getFirst(), StringArgumentType.greedyString())
                .suggests(CommandManager::suggestUsernames)
                .executes(this::executeMultiChat)
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
     * Sends the supplied console command through the MultiChat payload.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeMultiChat(CommandContext<FabricClientCommandSource> context) {
        String consoleCommand = StringArgumentType.getString(context, COMMAND_ARGS.getFirst());
        MultiChatPayload.send(consoleCommand);
        return 1;
    }
}
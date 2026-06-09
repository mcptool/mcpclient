package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.payloads.EasyCommandBlockerPayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class EasyCommandBlockerCommand implements Command {

    public static String COMMAND_NAME = "easycommandblocker";
    public static List<String> COMMAND_ARGS = List.of("consoleCommand");

    /**
     * Registers the EasyCommandBlocker client command and its arguments.
     * <p>
     * Usage:
     * .easycommandblocker <consoleCommand>
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.getFirst(), StringArgumentType.greedyString())
                .suggests(CommandManager::suggestUsernames)
                .executes(this::executeEasyCommandBlocker)
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
     * Sends the console command through the EasyCommandBlocker payload.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeEasyCommandBlocker(CommandContext<FabricClientCommandSource> context) {
        String consoleCommand = StringArgumentType.getString(context, COMMAND_ARGS.getFirst());
        EasyCommandBlockerPayload.send(consoleCommand);
        return 1;
    }
}
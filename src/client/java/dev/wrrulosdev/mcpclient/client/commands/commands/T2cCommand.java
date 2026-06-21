package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.payloads.T2CPayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class T2cCommand implements Command {

    public static String COMMAND_NAME = "t2c";
    public static List<String> COMMAND_ARGS = List.of("proxyCommand");

    /**
     * Registers the T2C client command and its arguments.
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.getFirst(), StringArgumentType.greedyString())
                .suggests(CommandManager::suggestUsernames)
                .executes(this::executeT2C)
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
     * Sends the supplied proxy command through the T2C payload.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeT2C(CommandContext<FabricClientCommandSource> context) {
        String proxyCommand = StringArgumentType.getString(context, COMMAND_ARGS.getFirst());
        T2CPayload.send(proxyCommand);
        return 1;
    }
}
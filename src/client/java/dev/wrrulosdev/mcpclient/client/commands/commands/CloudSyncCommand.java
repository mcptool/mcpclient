package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.payloads.CloudSyncPayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CloudSyncCommand implements Command {

    public static String COMMAND_NAME = "cloudsync";
    public static List<String> COMMAND_ARGS = List.of("target", "proxyCommand");

    /**
     * Registers the CloudSync client command and its arguments.
     * <p>
     * Usage:
     * .cloudsync <target> <proxyCommand>
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
                    .executes(this::executeCloudSync)
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
     * Sends a cloud sync payload to the target with the provided proxy command.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeCloudSync(CommandContext<FabricClientCommandSource> context) {
        String target = StringArgumentType.getString(context, COMMAND_ARGS.get(0));
        String proxyCommand = StringArgumentType.getString(context, COMMAND_ARGS.get(1));
        CloudSyncPayload.send(target, proxyCommand);
        return 1;
    }
}
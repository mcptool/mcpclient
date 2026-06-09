package dev.wrrulosdev.mcpclient.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.wrrulosdev.mcpclient.client.commands.commands.*;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;


public class CommandManager {

    public static final String COMMAND_PREFIX = "mcp";
    private static final List<Command> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new HelpCommand());
        COMMANDS.add(new FlyCommand());
        COMMANDS.add(new HClipCommand());
        COMMANDS.add(new VClipCommand());
        COMMANDS.add(new FakeCreativeCommand());
        COMMANDS.add(new MultiChatCommand());
        COMMANDS.add(new EasyCommandBlockerCommand());
        COMMANDS.add(new CloudSyncCommand());
        COMMANDS.add(new T2cCommand());
        COMMANDS.add(new AtlasCommand());
        COMMANDS.add(new CommandBridgeCommand());
    }

    /**
     * Provides autocompletion suggestions for player names.
     *
     * @param context The command context.
     * @param builder The suggestion builder.
     * @return A CompletableFuture with the suggestions.
     */
    public static CompletableFuture<Suggestions> suggestUsernames(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        int lastSpaceIndex = input.lastIndexOf(' ');
        String prefix = lastSpaceIndex == -1 ? "" : input.substring(0, lastSpaceIndex + 1);
        String filter = (lastSpaceIndex == -1) ? input : input.substring(lastSpaceIndex + 1).toLowerCase();
        Minecraft mc = Minecraft.getInstance();

        if (mc.getConnection() != null) {
            mc.getConnection().getOnlinePlayers().stream()
                .map(entry -> entry.getProfile().name())
                .filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().startsWith(filter))
                .forEach(name -> builder.suggest(prefix + name));
        }

        return builder.buildFuture();
    }

    /**
     * Registers all commands with the given command dispatcher.
     *
     * @param dispatcher The CommandDispatcher used to register the commands.
     */
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> mainCommand = literal(COMMAND_PREFIX)
            .executes(CommandManager::executeRoot);

        COMMANDS.forEach(command -> mainCommand.then(command.register()));
        dispatcher.register(mainCommand);
    }

    public static void sendInvalidArgsMessage(String commandName, List<String> commandArgs) {
        Msg.sendFormattedMessage(ClientConstants.PREFIX + "&cUsage: /" + COMMAND_PREFIX + " " + commandName + " " + String.join(" ", commandArgs));
    }

    /**
     * Executes the root command when no subcommand is provided.
     *
     * @param context The command context, containing information about the command execution.
     * @return The result of the command execution (1 indicates that no valid subcommand was provided).
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Msg.sendFormattedMessage(ClientConstants.PREFIX + "&cUse &f/" + COMMAND_PREFIX + " " + HelpCommand.COMMAND_NAME + " &cto see the commands.");
        return 1;
    }
}
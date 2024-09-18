package dev.wrrulos.mcpclient.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulos.mcpclient.command.commands.HelpCommand;
import dev.wrrulos.mcpclient.command.commands.exploits.ExploitCommand;
import dev.wrrulos.mcpclient.constants.messages.CommandConstants;
import dev.wrrulos.mcpclient.utils.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**
 * Manages the registration of all commands under the 'mcptool' main command.
 */
public class CommandManager {
    /**
     * Registers the main command.
     */
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("mcptool")
                .executes(CommandManager::executeRoot)
                .then(HelpCommand.register())
                .then(ExploitCommand.register())
        );
    }

    /**
     * Executes the main command.
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        ChatUtils.sendMessage(CommandConstants.INVALID_COMMAND);
        return 1;
    }
}

package dev.wrrulos.mcpclient.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulos.mcpclient.constants.messages.CommandConstants;
import dev.wrrulos.mcpclient.utils.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class HelpCommand {

    /**
     * Registers the help command under "mcptool".
     */
    public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("help")
            .executes(HelpCommand::executeRoot);
    }

    /**
     * Executes the help command.
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        ChatUtils.sendMessage(CommandConstants.HelpCommand.HELP_MESSAGE);
        return 1;
    }
}

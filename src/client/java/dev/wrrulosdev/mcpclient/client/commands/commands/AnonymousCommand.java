package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.options.Anonymous;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class AnonymousCommand implements Command {

    public static String COMMAND_NAME = "anonymous";

    /**
     * Registers the command with the Brigadier dispatcher, defining its structure
     * and execution logic.
     *
     * @return A LiteralArgumentBuilder for the client command
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Executes the logic for the anonymous command, toggling the feature's status
     * and sending a feedback message to the user.
     *
     * @param context The command execution context
     * @return The result status of the command execution
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Anonymous.INSTANCE.toggle();
        CommandManager.sendStatus(COMMAND_NAME, Anonymous.INSTANCE.isEnabled());
        return 1;
    }
}

package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class FlyCommand implements Command {

    public static String COMMAND_NAME = "fly";

    /**
     * Registers the Fly client command.
     * <p>
     * Usage:
     * .fly
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Toggles the Fly cheat.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        CheatsSettings settings = MCPClient.getSettingsManager().getCheatsSettings();
        settings.setFlyEnabled(!settings.isFlyEnabled());
        Fly.INSTANCE.run();
        CommandManager.sendStatus(COMMAND_NAME, settings.isFlyEnabled());
        return 1;
    }
}
package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.cheats.Spider;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class SpiderCommand implements Command {

    public static String COMMAND_NAME = "spider";

    /**
     * Registers the Spider client command.
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Toggles the Spider cheat.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Spider.INSTANCE.toggle();
        Spider.INSTANCE.run();
        CommandManager.sendStatus(COMMAND_NAME, Spider.INSTANCE.isEnabled());
        return 1;
    }
}
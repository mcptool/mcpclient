package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.cheats.Jesus;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class JesusCommand implements Command {

    public static String COMMAND_NAME = "jesus";

    /**
     * Registers the Jesus client command.
     * <p>
     * Usage:
     * .jesus
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Toggles the Jesus cheat.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Jesus.INSTANCE.toggle();
        Jesus.INSTANCE.run();
        CommandManager.sendStatus(COMMAND_NAME, Jesus.INSTANCE.isEnabled());
        return 1;
    }
}
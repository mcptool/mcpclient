package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.cheats.FakeCreative;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class FakeCreativeCommand implements Command {

    public static String COMMAND_NAME = "fakegm";
    public static List<String> COMMAND_ARGS = List.of();

    /**
     * Registers the FakeCreative client command.
     * <p>
     * Usage:
     * .fakegm
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Executes the Fake Creative mode toggle.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        new FakeCreative().run();
        return 1;
    }
}
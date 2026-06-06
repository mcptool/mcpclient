package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.cheats.FakeCreative;
import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class FakeCreativeCommand implements Command {

    /**
     * Registers the FakeCreative command.
     *
     * @return A LiteralArgumentBuilder that configures the "plugins" command.
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("fakegm")
            .executes(this::executeRoot);

    }

    /**
     *
     * @param context The command context, containing information about the player running the command.
     * @return The result of the command execution (0 indicates success).
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        new FakeCreative().run();
        return 0;
    }
}
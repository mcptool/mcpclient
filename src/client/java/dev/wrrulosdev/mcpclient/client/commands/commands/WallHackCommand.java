package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.Spider;
import dev.wrrulosdev.mcpclient.client.cheats.WallHack;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class WallHackCommand implements Command {

    public static String COMMAND_NAME = "wallhack";

    /**
     * Registers the wallhack client command.
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot);
    }

    /**
     * Toggles the Wallhack cheat.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        WallHack.INSTANCE.toggle();
        WallHack.INSTANCE.run();
        return 1;
    }
}
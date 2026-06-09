package dev.wrrulosdev.mcpclient.client.commands.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.cheats.VClip;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import dev.wrrulosdev.mcpclient.client.commands.CommandManager;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.utilities.messages.Msg;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class VClipCommand implements Command {

    public static String COMMAND_NAME = "vclip";
    public static List<String> COMMAND_ARGS = List.of("distance");

    /**
     * Registers the VClip client command and its arguments.
     * <p>
     * Usage:
     * .vclip <distance>
     *
     * @return Command builder instance
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal(COMMAND_NAME)
            .executes(this::executeRoot)
            .then(argument(COMMAND_ARGS.getFirst(), DoubleArgumentType.doubleArg())
                .executes(this::executeHClip)
            );
    }

    /**
     * Executed when the command is used without required arguments.
     * <p>
     * Displays the correct command usage to the user.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        CommandManager.sendInvalidArgsMessage(COMMAND_NAME, COMMAND_ARGS);
        return 0;
    }

    /**
     * Executes the VClip teleport using the supplied distance argument.
     *
     * @param context Command execution context
     * @return Command result status
     */
    private int executeHClip(CommandContext<FabricClientCommandSource> context) {
        double distance = DoubleArgumentType.getDouble(context, COMMAND_ARGS.getFirst());
        VClip.execute(distance);
        return 1;
    }
}
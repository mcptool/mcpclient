package dev.wrrulosdev.mcpclient.client.commands.commands;

import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.commands.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.SessionAccessor;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class FlyCommand implements Command {

    /**
     * Registers the Fly command.
     *
     * @return A LiteralArgumentBuilder that configures the "plugins" command.
     */
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("fly")
            .executes(this::executeRoot);

    }

    /**
     *
     * @param context The command context, containing information about the player running the command.
     * @return The result of the command execution (0 indicates success).
     */
    private int executeRoot(CommandContext<FabricClientCommandSource> context) {
        new Fly().run();
        User user = Minecraft.getInstance().getUser();

        System.out.println("Before: " + ((SessionAccessor) user).getUsername());

        ((SessionAccessor) user).setUsername("Raulito23");

        System.out.println("After: " + ((SessionAccessor) user).getUsername());
        return 0;
    }
}
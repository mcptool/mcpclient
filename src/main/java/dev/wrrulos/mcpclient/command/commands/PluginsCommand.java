package dev.wrrulos.mcpclient.command.commands;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.utils.ChatUtils;
import dev.wrrulos.mcpclient.constants.messages.CommandConstants;
import dev.wrrulos.mcpclient.utils.PluginTabStorage;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import java.util.Objects;

public class PluginsCommand {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * Registers the command under "plugins".
     */
    public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("plugins")
            .executes(PluginsCommand::executeRoot);
    }

    /**
     * Executes the command.
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        ChatUtils.sendMessage(CommandConstants.PluginsCommand.SENDING);
        PluginTabStorage pluginTabStorage = Mcpclient.getPluginTabStorage();
        pluginTabStorage.setListening(true);

        // Request command completions for common commands to see the plugin names
        new Thread(() -> {
            try {
                // Commands to request completions
                String[] commands = {"/about ", "/pl ", "/plugins ", "/ver ", "/version "};

                // Request completions for each command
                for (String command : commands) {
                    // Send the packet
                    System.out.println("Sending packet for command: " + command);
                    Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new RequestCommandCompletionsC2SPacket(0, command));
                    Thread.sleep(50); // Sleep for a bit to avoid spamming the server
                }
                pluginTabStorage.setListening(false);
                pluginTabStorage.sendStoredPluginNames();

            } catch (Exception e) {
                ChatUtils.sendMessage("An error occurred while requesting command completions.");
                System.out.println("An error occurred while requesting command completions. Error:" + e);
            }
        }).start();
        return 1;
    }
}

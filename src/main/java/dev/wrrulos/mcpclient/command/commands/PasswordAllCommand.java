package dev.wrrulos.mcpclient.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wrrulos.mcpclient.data.PlayerData;
import dev.wrrulos.mcpclient.utils.ChatUtils;
import dev.wrrulos.mcpclient.utils.MCPToolCLI;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PasswordAllCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("passwordall")
            .executes(PasswordAllCommand::executeRoot);
    }

    /**
     * Execute the passwordall command
     * @param context Command context
     * @return Integer value (1)
     */
    private static int executeRoot(CommandContext<FabricClientCommandSource> context) {
        Map<String, PlayerData> playerDataMap = new HashMap<>();

        // Get the name of the player executing the command
        String currentPlayerName = context.getSource().getPlayer().getGameProfile().getName();

        // Retrieve the list of players from the network handler
        context.getSource().getPlayer().networkHandler.getPlayerList().forEach(playerInfo -> {
            String playerName = playerInfo.getProfile().getName();
            String playerUUID = playerInfo.getProfile().getId().toString();
            String playerGamemode = playerInfo.getGameMode().getName();
            int playerPing = playerInfo.getLatency();
            PlayerData playerData = new PlayerData(playerName, playerUUID, playerGamemode, playerPing);

            // Exclude the player executing the command
            if (!playerName.equals(currentPlayerName)) {
                playerDataMap.put(playerName, playerData);
            }
        });

        // If no other players are online, show an error message
        if (playerDataMap.isEmpty()) {
            ChatUtils.parseColoredText("&8[&d#&8] &fNo players were found on the server.");
            return 1;
        }

        // Construct the command for all players except the current one
        String usernames = String.join(" ", playerDataMap.keySet());
        String command = "mcptool --mcpassword " + usernames;

        // Execute the command using MCPToolCLI
        MCPToolCLI.runCommand(command);
        return 1;
    }
}

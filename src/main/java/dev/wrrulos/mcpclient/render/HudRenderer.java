package dev.wrrulos.mcpclient.render;

import dev.wrrulos.mcpclient.utils.ChatUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class HudRenderer implements HudRenderCallback {
    /**
     * Render the HUD on the screen
     * @param drawContext Draw context
     * @param tickCounter Render tick counter
     */
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;

        if (client.getCurrentServerEntry() == null) {
            return;
        }

        // Get the server address, version, and protocol
        String address = client.getCurrentServerEntry().address;
        String version = client.getCurrentServerEntry().version.getString();
        int protocol = client.getCurrentServerEntry().protocolVersion;

        // Check if the player is connected to a server and get the numerical IP address and port
        if (client.getNetworkHandler() != null || client.getNetworkHandler().getConnection() != null) {
            address = client.getNetworkHandler().getConnection().getAddressAsString(true);

            if (address.contains("/")) {
                address = address.split("/")[1];
            }
        }

        // Create the text components
        Text serverIpText = ChatUtils.parseColoredText("&cServer: &a" + address);
        Text versionText = ChatUtils.parseColoredText("&cVersion: &f" + version);
        Text protocolText = ChatUtils.parseColoredText("&cProtocol: &f" + protocol);

        // Render the text components on the screen
        drawContext.drawText(renderer, serverIpText, 10, 10, 0xFFFFFF, true);
        drawContext.drawText(renderer, versionText, 10, 25, 0xFFFFFF, true);
        drawContext.drawText(renderer, protocolText, 10, 40, 0xFFFFFF, true);

        // Check if the player is in game and render the player count
        if (client.player == null) {
            return;
        }

        // Get the player count and render it on the screen
        int playerCount = client.player.networkHandler.getPlayerList().size();
        Text playersText = ChatUtils.parseColoredText("&cPlayers: &f" + playerCount);
        drawContext.drawText(renderer, playersText, 10, 55, 0xFFFFFF, true);

        // Get the current FPS and render it on the screen below the player count
        int fps = MinecraftClient.getInstance().getCurrentFps();
        Text fpsText = ChatUtils.parseColoredText("&cFPS: &f" + fps);
        drawContext.drawText(renderer, fpsText, 10, 70, 0xFFFFFF, true);
    }
}

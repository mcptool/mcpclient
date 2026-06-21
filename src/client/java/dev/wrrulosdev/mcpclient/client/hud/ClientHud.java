package dev.wrrulosdev.mcpclient.client.hud;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.utilities.connection.ServerAddress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;


public class ClientHud {

    private static String players = "124/1000";
    private static boolean mcpConnected = true;

    /**
     * Renders the HUD overlay on the screen.
     *
     * @param graphics The rendering context used for drawing GUI elements
     * @param tickDelta Partial tick time used for smooth rendering interpolation
     */
    public static void render(GuiGraphicsExtractor graphics, float tickDelta) {
        Minecraft client = Minecraft.getInstance();

        if (client.level == null || client.gui.screen() != null ||
            client.options.keyToggleGui.isDown() || client.getDebugOverlay().showDebugScreen()) {
            return;
        }

        if (!MCPClient.getSettingsManager().getClientSettings().isClientHudEnabled()) return;
        ServerAddress serverAddress = MCPClient.getLastServerAddress();

        if (serverAddress == null) return;
        String address = serverAddress.getAddress();
        String protocol = serverAddress.getProtocol();

        graphics.nextStratum();
        Font font = client.font;
        int startX = 10, startY = 10, padding = 8;

        Component ipText = Component.literal("Server: ").withColor(0xAAAAAA)
            .append(Component.literal(address.isEmpty() ? " " : address).withColor(0xFFFFFF));

        Component protocolText = Component.literal("Protocol: ").withColor(0xAAAAAA)
            .append(Component.literal(protocol.isEmpty() ? " " : protocol).withColor(0xFFFFFF));

        Component playersText = Component.literal("Players: ").withColor(0xAAAAAA)
            .append(Component.literal(players).withColor(0xFFFFFF));

        Component mcpText = Component.literal("MCPTool: ").withColor(0xAAAAAA)
            .append(Component.literal(mcpConnected ? "Online" : "Offline")
                .withColor(mcpConnected ? 0x55FF55 : 0xFF5555));

        int maxWidth = Math.max(
            Math.max(font.width(ipText), font.width(protocolText)),
            Math.max(font.width(playersText), font.width(mcpText))
        );

        int totalWidth = maxWidth + (padding * 2);
        int totalHeight = (font.lineHeight * 4) + (padding * 2) + 6;
        int bgColor = getAlphaColor(0x1A1A1A, 0.85f);

        fillRoundedRect(graphics, startX, startY, startX + totalWidth, startY + totalHeight, bgColor);
        drawRoundedBorder(graphics, startX, startY, startX + totalWidth, startY + totalHeight, 0xFFFF4444);
        graphics.pose().pushMatrix();
        graphics.pose().identity();

        int textX = startX + padding;
        int currentY = startY + padding;

        renderText(graphics, font, ipText, textX, currentY);
        currentY += font.lineHeight + 2;

        renderText(graphics, font, protocolText, textX, currentY);
        currentY += font.lineHeight + 2;

        renderText(graphics, font, playersText, textX, currentY);
        currentY += font.lineHeight + 2;

        renderText(graphics, font, mcpText, textX, currentY);

        graphics.pose().popMatrix();
    }

    /**
     * Draws a simple rounded-style border using rectangular segments.
     *
     * @param g The GUI rendering context
     * @param x1 Top-left X coordinate
     * @param y1 Top-left Y coordinate
     * @param x2 Bottom-right X coordinate
     * @param y2 Bottom-right Y coordinate
     * @param c Border color in ARGB format
     */
    private static void drawRoundedBorder(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int c) {
        g.fill(x1 + 1, y1, x2 - 1, y1 + 1, c);
        g.fill(x1 + 1, y2 - 1, x2 - 1, y2, c);
        g.fill(x1, y1 + 1, x1 + 1, y2 - 1, c);
        g.fill(x2 - 1, y1 + 1, x2, y2 - 1, c);
        g.fill(x1 + 1, y1 + 1, x1 + 2, y1 + 2, c);
        g.fill(x2 - 2, y1 + 1, x2 - 1, y1 + 2, c);
        g.fill(x1 + 1, y2 - 2, x1 + 2, y2 - 1, c);
        g.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, c);
    }

    /**
     * Renders a text component at the specified screen position.
     *
     * @param g The GUI rendering context
     * @param f The font renderer used for text drawing
     * @param text The text component to render
     * @param x X coordinate
     * @param y Y coordinate
     */
    private static void renderText(GuiGraphicsExtractor g, Font f, Component text, int x, int y) {
        g.text(f, text, x, y, 0xFFFFFFFF, true);
    }

    /**
     * Converts an RGB color into an ARGB color using the provided alpha value.
     *
     * @param rgb Base RGB color
     * @param alpha Transparency value between 0.0 and 1.0
     * @return ARGB color integer
     */
    private static int getAlphaColor(int rgb, float alpha) {
        return ((int) (alpha * 255) << 24) | rgb;
    }

    /**
     * Draws a filled rectangle with a stylized rounded appearance.
     *
     * @param g The GUI rendering context
     * @param x1 Top-left X coordinate
     * @param y1 Top-left Y coordinate
     * @param x2 Bottom-right X coordinate
     * @param y2 Bottom-right Y coordinate
     * @param c Fill color in ARGB format
     */
    private static void fillRoundedRect(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int c) {
        g.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, c);
        g.fill(x1 + 2, y1, x2 - 2, y1 + 1, c);
        g.fill(x1 + 2, y2 - 1, x2 - 2, y2, c);
        g.fill(x1, y1 + 2, x1 + 1, y2 - 2, c);
        g.fill(x2 - 1, y1 + 2, x2, y2 - 2, c);
        g.fill(x1 + 1, y1 + 1, x1 + 2, y1 + 2, c);
        g.fill(x2 - 2, y1 + 1, x2 - 1, y1 + 2, c);
        g.fill(x1 + 1, y2 - 2, x1 + 2, y2 - 1, c);
        g.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, c);
    }
}
package dev.wrrulosdev.mcpclient.client.hud;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public class PlayerModel {

    /**
     * Renders a 3D model of the local player in the HUD if certain conditions are met,
     * such as the player being in-game and not having an open GUI or debug screen.
     *
     * @param graphics The graphics utility used to draw the player model
     */
    public static void renderPlayerModel(GuiGraphicsExtractor graphics) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null || mc.gui.screen() != null || mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();
        int marginX = (int) clientSettings.getPlayerModelMarginX();
        int marginY = (int) clientSettings.getPlayerModelMarginY();
        int size = (int) clientSettings.getPlayerModelSize();

        if (!dev.wrrulosdev.mcpclient.client.options.PlayerModel.INSTANCE.isEnabled()) return;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x1 = screenWidth - marginX - (size * 2);
        int x2 = screenWidth - marginX;
        int y1 = marginY - size;
        int y2 = marginY + size;
        float centerX = (x1 + x2) / 2.0F;
        float centerY = (y1 + y2) / 2.0F;
        InventoryScreen.extractEntityInInventoryFollowsMouse(
            graphics,
            x1, y1, x2, y2,
            size,
            0.0625F,
            centerX,
            centerY,
            mc.player
        );
    }
}
package dev.wrrulosdev.mcpclient.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public class PlayerModel {

    private static final int MARGIN_X = 5;
    private static final int MARGIN_Y = 45;
    private static final int SIZE = 30;

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

        if (!dev.wrrulosdev.mcpclient.client.options.PlayerModel.INSTANCE.isEnabled()) return;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x1 = screenWidth - MARGIN_X - (SIZE * 2);
        int x2 = screenWidth - MARGIN_X;
        int y1 = MARGIN_Y - SIZE;
        int y2 = MARGIN_Y + SIZE;
        float centerX = (x1 + x2) / 2.0F;
        float centerY = (y1 + y2) / 2.0F;
        InventoryScreen.extractEntityInInventoryFollowsMouse(
            graphics,
            x1, y1, x2, y2,
            SIZE,
            0.0625F,
            centerX,
            centerY,
            mc.player
        );
    }
}
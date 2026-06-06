package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

public abstract class BaseAnimatedScreen extends Screen {

    private static final long ANIMATION_DURATION = 500L;
    protected static final int MAX_WIDTH = 450;
    protected static final int MAX_HEIGHT = 280;

    protected static long HAND_CURSOR;
    protected static long ARROW_CURSOR;

    private long openTime;

    public BaseAnimatedScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.openTime = Util.getMillis();
        initCursors();
    }

    private void initCursors() {
        if (HAND_CURSOR == 0) HAND_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        if (ARROW_CURSOR == 0) ARROW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        float progress = calculateAnimationProgress();
        int[] bounds = calculateBounds(progress);
        int x1 = bounds[0], y1 = bounds[1], x2 = bounds[2], y2 = bounds[3];

        // Dibuja el fondo general
        renderBackground(graphics, x1, y1, x2, y2, progress);

        // Espera a que la animación esté a la mitad para mostrar el contenido
        if (progress >= 0.5f) {
            renderHeader(graphics, x1, x2, y1, progress);
            // Llama al método abstracto que cada pantalla hija debe implementar
            renderWindowContent(graphics, x1, x2, y1, y2, mouseX, mouseY, progress);
        }

        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    // === MÉTODOS ABSTRACTOS PARA PERSONALIZAR ===

    // Cada pantalla decidirá qué dibujar dentro de la ventana
    protected abstract void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress);

    // Cada pantalla decidirá qué título poner en el Header
    protected abstract String getWindowTitle();

    // ============================================

    private float calculateAnimationProgress() {
        long timeAlive = Util.getMillis() - this.openTime;
        return Math.min((float) timeAlive / ANIMATION_DURATION, 1.0f);
    }

    private int[] calculateBounds(float progress) {
        float ease = 1.0f - (float) Math.pow(1.0f - progress, 3);
        int targetWidth = Math.min(this.width - 60, MAX_WIDTH);
        int targetHeight = Math.min(this.height - 60, MAX_HEIGHT);

        int w = (int) (targetWidth * ease);
        int h = (int) (targetHeight * ease);

        return new int[]{ (width / 2) - (w / 2), (height / 2) - (h / 2), (width / 2) + (w / 2), (height / 2) + (h / 2) };
    }

    private void renderBackground(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, float progress) {
        int bgAlpha = (int) (150 * progress);
        fillRoundedRect(graphics, x1, y1, x2, y2, (bgAlpha << 24) | 0x0A0A0A);
    }

    private void renderHeader(GuiGraphicsExtractor graphics, int x1, int x2, int y1, float progress) {
        int headerHeight = 35;
        int headerY1 = y1 + 10;
        int headerY2 = headerY1 + headerHeight;

        int alpha = (int) (220 * progress);
        int textAlpha = (int) (255 * progress);
        int textColor = 0xFFFFFF | (textAlpha << 24);
        int versionColor = 0x888888 | (textAlpha << 24);

        fillRoundedRect(graphics, x1 + 10, headerY1, x2 - 10, headerY2, (alpha << 24) | 0x1A1A1A);

        int iconSize = 20;
        int iconX = x1 + 20;
        int iconY = headerY1 + (headerHeight - iconSize) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TextureConstants.LOGO, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);

        int textY = headerY1 + (headerHeight - this.font.lineHeight) / 2;
        // Usamos getWindowTitle() para que el título cambie dinámicamente según la pantalla
        graphics.text(this.font, Component.literal(getWindowTitle()), iconX + 25, textY, textColor, false);

        String versionText = "Version " + ClientConstants.VERSION;
        int versionX = x2 - 20 - this.font.width(versionText);
        graphics.text(this.font, Component.literal(versionText), versionX, textY, versionColor, false);
    }

    protected void fillRoundedRect(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int c) {
        g.fill(x1 + 4, y1, x2 - 4, y2, c);
        g.fill(x1, y1 + 4, x2, y2 - 4, c);
        g.fill(x1 + 1, y1 + 2, x1 + 4, y1 + 4, c);
        g.fill(x1 + 2, y1 + 1, x1 + 4, y1 + 2, c);
        g.fill(x2 - 4, y1 + 2, x2 - 1, y1 + 4, c);
        g.fill(x2 - 4, y1 + 1, x2 - 2, y1 + 2, c);
        g.fill(x1 + 1, y2 - 4, x1 + 4, y2 - 2, c);
        g.fill(x1 + 2, y2 - 2, x1 + 4, y2 - 1, c);
        g.fill(x2 - 4, y2 - 4, x2 - 1, y2 - 2, c);
        g.fill(x2 - 4, y2 - 2, x2 - 2, y2 - 1, c);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
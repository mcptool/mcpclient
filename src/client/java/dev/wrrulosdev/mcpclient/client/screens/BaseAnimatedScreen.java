package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;


public abstract class BaseAnimatedScreen extends Screen {

    private static final long ANIMATION_DURATION = 500L;
    protected int maxWidth = 450;
    protected int maxHeight = 280;
    protected static long HAND_CURSOR;
    protected static long ARROW_CURSOR;
    private long openTime;

    /**
     * Constructs a base animated screen with a given title
     *
     * @param title Screen title component
     */
    public BaseAnimatedScreen(Component title) {
        super(title);
    }

    /**
     * Initializes the screen state and cursor resources
     */
    @Override
    protected void init() {
        super.init();
        this.openTime = Util.getMillis();
        initCursors();
    }

    /**
     * Initializes system cursors used for hover interactions
     */
    private void initCursors() {
        if (HAND_CURSOR == 0) {
            HAND_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        }

        if (ARROW_CURSOR == 0) {
            ARROW_CURSOR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        }
    }

    /**
     * Main rendering entry point for the screen
     * Handles animation progression, layout calculation, and delegated rendering
     *
     * @param graphics Rendering context used for drawing UI elements
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param delta Frame delta time
     */
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        float progress = calculateAnimationProgress();
        int[] bounds = calculateBounds(progress);
        int x1 = bounds[0], y1 = bounds[1], x2 = bounds[2], y2 = bounds[3];
        renderBackground(graphics, x1, y1, x2, y2, progress);

        if (progress >= 0.5f) {
            renderHeader(graphics, x1, x2, y1, progress);
            renderWindowContent(graphics, x1, x2, y1, y2, mouseX, mouseY, progress);
        }

        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    /**
     * Renders the main content area of the screen
     *
     * @param graphics Rendering context used for UI drawing
     * @param x1 Left boundary of the window
     * @param x2 Right boundary of the window
     * @param y1 Top boundary of the window
     * @param y2 Bottom boundary of the window
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param progress Animation progress value (0.0 - 1.0)
     */
    protected abstract void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2,
                                                int mouseX, int mouseY, float progress);

    /**
     * Returns the title displayed in the window header
     *
     * @return Screen title string
     */
    protected abstract String getWindowTitle();

    /**
     * Calculates the current animation progress based on screen lifetime
     *
     * @return A normalized value between 0.0 and 1.0
     */
    private float calculateAnimationProgress() {
        long timeAlive = Util.getMillis() - this.openTime;
        return Math.min((float) timeAlive / ANIMATION_DURATION, 1.0f);
    }

    /**
     * Calculates animated window bounds based on easing progress
     *
     * @param progress Animation progress value
     * @return Array containing [x1, y1, x2, y2] screen bounds
     */
    private int[] calculateBounds(float progress) {
        float ease = 1.0f - (float) Math.pow(1.0f - progress, 3);

        int targetWidth = Math.min(this.width - 60, this.maxWidth);
        int targetHeight = Math.min(this.height - 60, this.maxHeight);

        int w = (int) (targetWidth * ease);
        int h = (int) (targetHeight * ease);

        return new int[]{
            (width / 2) - (w / 2),
            (height / 2) - (h / 2),
            (width / 2) + (w / 2),
            (height / 2) + (h / 2)
        };
    }

    /**
     * Renders the animated background container of the screen
     *
     * @param graphics Rendering context used for drawing
     * @param x1 Left boundary
     * @param y1 Top boundary
     * @param x2 Right boundary
     * @param y2 Bottom boundary
     * @param progress Animation progress value
     */
    private void renderBackground(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, float progress) {
        int bgAlpha = (int) (150 * progress);
        fillRoundedRect(graphics, x1, y1, x2, y2, (bgAlpha << 24) | 0x0A0A0A);
    }

    /**
     * Renders the window header including icon, title, and version text
     *
     * @param graphics Rendering context used for UI drawing
     * @param x1 Left boundary
     * @param x2 Right boundary
     * @param y1 Top boundary
     * @param progress Animation progress value
     */
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

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            TextureConstants.LOGO,
            iconX,
            iconY,
            0,
            0,
            iconSize,
            iconSize,
            iconSize,
            iconSize
        );

        int textY = headerY1 + (headerHeight - this.font.lineHeight) / 2;
        graphics.text(this.font, Component.literal(getWindowTitle()), iconX + 25, textY, textColor, false);

        String versionText = "Version " + ClientConstants.VERSION;
        int versionX = x2 - 20 - this.font.width(versionText);

        graphics.text(this.font, Component.literal(versionText), versionX, textY, versionColor, false);
    }

    /**
     * Draws a simplified rounded rectangle using multiple fill segments
     *
     * @param g Rendering context used for drawing
     * @param x1 Left boundary
     * @param y1 Top boundary
     * @param x2 Right boundary
     * @param y2 Bottom boundary
     * @param c ARGB color value
     */
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

    /**
     * Handles keyboard input events (default implementation)
     *
     * @param event Key input event
     * @return Result of parent handling
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        return super.keyPressed(event);
    }

    /**
     * Indicates that this screen does not pause the game
     *
     * @return false always, allowing gameplay to continue
     */
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
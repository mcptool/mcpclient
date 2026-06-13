package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

public abstract class AbstractSettingComponent {

    /**
     * Returns the vertical size occupied by the component when rendered
     * inside a settings container layout.
     *
     * @return The component height in screen pixels.
     */
    public abstract int getHeight();

    /**
     * Renders the component contents, visuals, and animations using
     * the provided rendering context and layout parameters.
     *
     * @param graphics The graphics extraction rendering context.
     * @param font The font renderer used for text drawing operations.
     * @param x The left horizontal rendering position.
     * @param y The top vertical rendering position.
     * @param width The available rendering width.
     * @param mouseX The current mouse horizontal position.
     * @param mouseY The current mouse vertical position.
     * @param progress The animation progress interpolation value.
     */
    public abstract void render(
        GuiGraphicsExtractor graphics,
        net.minecraft.client.gui.Font font,
        int x,
        int y,
        int width,
        int mouseX,
        int mouseY,
        float progress
    );

    /**
     * Handles mouse click interactions performed within the component area.
     *
     * @param mouseX The mouse horizontal position.
     * @param mouseY The mouse vertical position.
     * @param button The mouse button identifier.
     * @param x The component left position.
     * @param y The component top position.
     * @param width The component width.
     * @return True if the click was consumed by the component; otherwise false.
     */
    public abstract boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button,
        int x,
        int y,
        int width
    );

    /**
     * Processes keyboard input events while the component is focused
     * or actively receiving user interaction.
     *
     * @param event The keyboard input event being processed.
     * @return True if the event was consumed by the component; otherwise false.
     */
    public boolean keyPressed(KeyEvent event) {
        return false;
    }

    /**
     * Determines whether the specified mouse coordinates are currently
     * located within the component bounds.
     *
     * @param mouseX The mouse horizontal position.
     * @param mouseY The mouse vertical position.
     * @param x The component left position.
     * @param y The component top position.
     * @param width The component width.
     * @return True if the mouse is inside the component area; otherwise false.
     */
    public boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight();
    }

    /**
     * Combines an RGB color value with a floating alpha component
     * to produce a packed ARGB color integer.
     *
     * @param rgb The RGB color value without transparency.
     * @param alpha The alpha transparency value ranging from 0.0 to 1.0.
     * @return The resulting ARGB color value.
     */
    protected int getAlphaColor(int rgb, float alpha) {
        return ((int) (alpha * 255) << 24) | rgb;
    }

    /**
     * Draws a rounded rectangle approximation using multiple fill operations
     * to create softened corners around the component background.
     *
     * @param graphics The graphics extraction rendering context.
     * @param x1 The left coordinate.
     * @param y1 The top coordinate.
     * @param x2 The right coordinate.
     * @param y2 The bottom coordinate.
     * @param color The ARGB color used for rendering.
     */
    protected void fillRoundedRect(
        GuiGraphicsExtractor graphics,
        int x1,
        int y1,
        int x2,
        int y2,
        int color
    ) {
        graphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, color);
        graphics.fill(x1 + 2, y1, x2 - 2, y1 + 1, color);
        graphics.fill(x1 + 2, y2 - 1, x2 - 2, y2, color);
        graphics.fill(x1, y1 + 2, x1 + 1, y2 - 2, color);
        graphics.fill(x2 - 1, y1 + 2, x2, y2 - 2, color);
    }

    /**
     * Renders a rounded border outline around a component using
     * individual fill operations for edges and corners.
     *
     * @param graphics The graphics extraction rendering context.
     * @param x The left coordinate.
     * @param y The top coordinate.
     * @param width The border width.
     * @param height The border height.
     * @param color The ARGB color used for rendering.
     */
    protected void renderBorder(
        GuiGraphicsExtractor graphics,
        int x,
        int y,
        int width,
        int height,
        int color
    ) {
        int x2 = x + width, y2 = y + height;
        graphics.fill(x + 2, y, x2 - 2, y + 1, color);
        graphics.fill(x + 2, y2 - 1, x2 - 2, y2, color);
        graphics.fill(x, y + 2, x + 1, y2 - 2, color);
        graphics.fill(x2 - 1, y + 2, x2, y2 - 2, color);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, color);
        graphics.fill(x2 - 2, y + 1, x2 - 1, y + 2, color);
        graphics.fill(x + 1, y2 - 2, x + 2, y2 - 1, color);
        graphics.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, color);
    }
}
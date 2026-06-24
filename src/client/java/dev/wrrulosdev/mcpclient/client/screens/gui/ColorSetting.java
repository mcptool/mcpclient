package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;
import java.util.function.Consumer;

public class ColorSetting extends AbstractSettingComponent {

    private final String label;
    private String text;
    private final Consumer<String> onChanged;
    private boolean isFocused;
    private boolean isPickerOpen = false;
    private boolean draggingCircle = false;

    public ColorSetting(
        String label,
        String defaultColor,
        Consumer<String> onChanged
    ) {
        this.label = label;
        this.text = defaultColor;
        this.onChanged = onChanged;
        this.isFocused = false;
    }

    /**
     * Returns the height of the component, which expands if the color picker is open.
     *
     * @return The height in pixels
     */
    @Override
    public int getHeight() {
        return isPickerOpen ? 105 : 28;
    }

    /**
     * Renders the color setting UI, including the label, color preview box,
     * text input field, and the optional color picker wheel.
     *
     * @param graphics The graphics utility for rendering
     * @param font The font renderer to use
     * @param x The X-coordinate
     * @param y The Y-coordinate
     * @param width The total width of the component
     * @param mouseX The current mouse X-coordinate
     * @param mouseY The current mouse Y-coordinate
     * @param progress The animation progress value
     */
    @Override
    public void render(
        GuiGraphicsExtractor graphics,
        Font font,
        int x,
        int y,
        int width,
        int mouseX,
        int mouseY,
        float progress
    ) {
        boolean hovered = isMouseOver(mouseX, mouseY, x, y, width) && mouseY <= y + 28;
        int bgCardColor = getAlphaColor(
            hovered ? 0x24262E : 0x1A1B22,
            220 * progress / 255
        );
        fillRoundedRect(graphics, x, y, x + width, y + getHeight(), bgCardColor);
        graphics.text(
            font,
            Component.literal(this.label),
            x + 12,
            y + (28 - font.lineHeight) / 2,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        int previewSize = 14;
        int previewX = x + width - 12 - previewSize;
        int previewY = y + (28 - previewSize) / 2;
        int currentColor = parseColor(this.text);
        graphics.fill(previewX - 1, previewY - 1, previewX + previewSize + 1, previewY + previewSize + 1, 0xFF45474F);
        graphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, getAlphaColor(currentColor, progress));

        int boxWidth = 65;
        int boxHeight = 14;
        int boxX = previewX - 8 - boxWidth;
        int boxY = y + (28 - boxHeight) / 2;
        int boxBgColor = this.isFocused ? 0x42444D : 0x2A2C33;
        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, getAlphaColor(boxBgColor, progress));
        String displayText = this.text;

        if (this.isFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
            displayText += "_";
        }

        while (font.width(displayText) > boxWidth - 4 && !displayText.isEmpty()) {
            displayText = displayText.substring(1);
        }

        graphics.text(
            font,
            Component.literal(displayText),
            boxX + 4,
            boxY + (boxHeight - font.lineHeight) / 2,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        if (isPickerOpen) {
            int red = (currentColor >> 16) & 0xFF;
            int green = (currentColor >> 8) & 0xFF;
            int blue = currentColor & 0xFF;
            float[] hsb = rgbToHsb(red, green, blue);
            float currentHue = hsb[0];
            float currentSat = hsb[1];
            float currentBrightness = hsb[2] == 0 ? 1.0f : hsb[2];
            int cx = x + width / 2;
            int cy = y + 64;
            int r = 32;

            if (draggingCircle && GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_PRESS) {
                draggingCircle = false;
            }

            if (draggingCircle) {
                double dx = mouseX - cx;
                double dy = mouseY - cy;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist > r) dist = r;
                double angle = Math.atan2(dy, dx);
                currentHue = (float) ((angle + Math.PI) / (2 * Math.PI));
                currentSat = (float) (dist / r);
                updateHexColor(currentHue, currentSat, currentBrightness);
            }

            int radiusSq = r * r;
            float floatR = (float) r;

            for (int row = -r; row <= r; row++) {
                for (int col = -r; col <= r; col++) {
                    float distSq = (float) (col * col + row * row);

                    if (distSq <= radiusSq) {
                        float dist = (float) Math.sqrt(distSq);
                        float alpha = 1.0f;

                        if (dist > floatR - 1.0f) {
                            alpha = 1.0f - (dist - (floatR - 1.0f));
                        }

                        double angle = Math.atan2(row, col);
                        float hue = (float) ((angle + Math.PI) / (2 * Math.PI));
                        float sat = dist / floatR;
                        int rgb = hsbToRgb(hue, sat, currentBrightness);
                        int alphaInt = (int) (alpha * 255);
                        int finalColor = (alphaInt << 24) | (rgb & 0x00FFFFFF);
                        graphics.fill(cx + col, cy + row, cx + col + 1, cy + row + 1, getAlphaColor(finalColor, progress));
                    }
                }
            }

            double currentAngle = currentHue * 2 * Math.PI - Math.PI;
            double currentDist = currentSat * r;
            int ix = (int) (cx + Math.cos(currentAngle) * currentDist);
            int iy = (int) (cy + Math.sin(currentAngle) * currentDist);
            graphics.fill(ix - 2, iy - 2, ix + 2, iy + 2, 0xFFFFFFFF); // Borde blanco de la mira
            graphics.fill(ix - 1, iy - 1, ix + 1, iy + 1, 0xFF000000); // Centro de contraste negro
        }
    }

    /**
     * Handles mouse click events, including toggling the picker, focusing the text input,
     * and dragging within the color wheel.
     *
     * @param mouseX The mouse X-coordinate
     * @param mouseY The mouse Y-coordinate
     * @param button The mouse button pressed
     * @param x The component X-coordinate
     * @param y The component Y-coordinate
     * @param width The component width
     * @return True if the event was consumed, false otherwise
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;

        int previewSize = 14;
        int previewX = x + width - 12 - previewSize;
        int previewY = y + (28 - previewSize) / 2;

        if (mouseX >= previewX && mouseX <= previewX + previewSize && mouseY >= previewY && mouseY <= previewY + previewSize) {
            this.isPickerOpen = !this.isPickerOpen;
            this.isFocused = false;
            return true;
        }

        int boxWidth = 65;
        int boxX = previewX - 8 - boxWidth;
        int boxY = y + (28 - 14) / 2;

        if (mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + 14) {
            this.isFocused = true;
            return true;
        } else {
            this.isFocused = false;
        }

        if (isPickerOpen) {
            int cx = x + width / 2;
            int cy = y + 64;
            int r = 32;
            double dx = mouseX - cx;
            double dy = mouseY - cy;

            if (dx * dx + dy * dy <= r * r) {
                this.draggingCircle = true;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.atan2(dy, dx);
                float hue = (float) ((angle + Math.PI) / (2 * Math.PI));
                float sat = (float) (dist / r);
                int c = parseColor(this.text);
                float brightness = rgbToHsb((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF)[2];
                if (brightness == 0) brightness = 1.0f;
                updateHexColor(hue, sat, brightness);
                return true;
            }
        }

        return false;
    }

    /**
     * Handles mouse release events to stop dragging the color wheel marker.
     *
     * @param mouseX The mouse X-coordinate
     * @param mouseY The mouse Y-coordinate
     * @param button The mouse button released
     * @return The result of the superclass method
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingCircle = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * Updates the internal hex string based on HSB color coordinates and notifies listeners.
     *
     * @param h The hue value
     * @param s The saturation value
     * @param b The brightness value
     */
    private void updateHexColor(float h, float s, float b) {
        int rgb = hsbToRgb(h, s, b);
        this.text = String.format("#%02X%02X%02X", (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);

        if (this.onChanged != null) {
            this.onChanged.accept(this.text);
        }
    }

    /**
     * Processes key press events for text input when the field is focused.
     *
     * @param keyCode The key code pressed
     * @return True if the key was processed, false otherwise
     */
    public boolean keyPressed(int keyCode) {
        if (!this.isFocused) return false;

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !this.text.isEmpty()) {
            this.text = this.text.substring(0, this.text.length() - 1);
            if (this.onChanged != null) this.onChanged.accept(this.text);
            return true;
        }

        return false;
    }

    /**
     * Processes typed characters for input into the text field.
     *
     * @param codePoint The character typed
     * @return True if the character was processed, false otherwise
     */
    public boolean charTyped(char codePoint) {
        if (!this.isFocused) return false;

        if (StringUtil.isAllowedChatCharacter(codePoint) && this.text.length() < 7) {
            this.text += codePoint;
            if (this.onChanged != null) this.onChanged.accept(this.text);
            return true;
        }

        return false;
    }

    /**
     * Returns the current color string.
     *
     * @return The hex or color code string
     */
    public String getText() {
        return this.text;
    }

    /**
     * Parses a color string (hex code or Minecraft color code) into an integer.
     *
     * @param input The color string to parse
     * @return The color as an RGB integer
     */
    private int parseColor(String input) {
        if (input == null || input.isEmpty()) return 0xFFFFFF;
        String parsed = input.trim();

        if (parsed.startsWith("#") && parsed.length() == 7) {
            try { return Integer.parseInt(parsed.substring(1), 16); }
            catch (NumberFormatException e) { return 0xFFFFFF; }
        } else if ((parsed.startsWith("&") || parsed.startsWith("§")) && parsed.length() == 2) {
            return getMinecraftColor(parsed.charAt(1));
        } else if (parsed.length() == 1) {
            return getMinecraftColor(parsed.charAt(0));
        }

        return 0xFFFFFF;
    }

    /**
     * Maps a Minecraft color code character to its RGB integer value.
     *
     * @param colorCode The color character
     * @return The RGB integer value
     */
    private int getMinecraftColor(char colorCode) {
        return switch (Character.toLowerCase(colorCode)) {
            case '0' -> 0x000000; case '1' -> 0x0000AA; case '2' -> 0x00AA00;
            case '3' -> 0x00AAAA; case '4' -> 0xAA0000; case '5' -> 0xAA00AA;
            case '6' -> 0xFFAA00; case '7' -> 0xAAAAAA; case '8' -> 0x555555;
            case '9' -> 0x5555FF; case 'a' -> 0x55FF55; case 'b' -> 0x55FFFF;
            case 'c' -> 0xFF5555; case 'd' -> 0xFF55FF; case 'e' -> 0xFFFF55;
            case 'f' -> 0xFFFFFF; default  -> 0xFFFFFF;
        };
    }

    /**
     * Converts RGB values to HSB (Hue, Saturation, Brightness) color components.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return An array containing H, S, and B values
     */
    private static float[] rgbToHsb(int r, int g, int b) {
        float h, s, v;
        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);
        v = max / 255.0f;
        float delta = max - min;
        if (max != 0) s = delta / max;
        else { s = 0; h = 0; return new float[]{h, s, v}; }
        if (delta == 0) h = 0;
        else if (r == max) h = (g - b) / delta;
        else if (g == max) h = 2 + (b - r) / delta;
        else h = 4 + (r - g) / delta;
        h *= 60;
        if (h < 0) h += 360;

        return new float[]{h / 360.0f, s, v};
    }

    /**
     * Converts HSB (Hue, Saturation, Brightness) values to an RGB integer.
     *
     * @param hue Hue value
     * @param saturation Saturation value
     * @param brightness Brightness value
     * @return The RGB integer value
     */
    private static int hsbToRgb(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;

        if (saturation == 0) {
            r = g = b = (int) (brightness * 255 + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - saturation * (1.0f - f));
            switch ((int) h) {
                case 0 -> { r = (int) (brightness * 255 + 0.5f); g = (int) (t * 255 + 0.5f); b = (int) (p * 255 + 0.5f); }
                case 1 -> { r = (int) (q * 255 + 0.5f); g = (int) (brightness * 255 + 0.5f); b = (int) (p * 255 + 0.5f); }
                case 2 -> { r = (int) (p * 255 + 0.5f); g = (int) (brightness * 255 + 0.5f); b = (int) (t * 255 + 0.5f); }
                case 3 -> { r = (int) (p * 255 + 0.5f); g = (int) (q * 255 + 0.5f); b = (int) (brightness * 255 + 0.5f); }
                case 4 -> { r = (int) (t * 255 + 0.5f); g = (int) (p * 255 + 0.5f); b = (int) (brightness * 255 + 0.5f); }
                case 5 -> { r = (int) (brightness * 255 + 0.5f); g = (int) (p * 255 + 0.5f); b = (int) (q * 255 + 0.5f); }
            }
        }

        return (r << 16) | (g << 8) | b;
    }
}
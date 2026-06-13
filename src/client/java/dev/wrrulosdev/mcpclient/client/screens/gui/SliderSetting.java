package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class SliderSetting extends AbstractSettingComponent {

    private final String label;
    private final float min;
    private final float max;
    private float value;
    private final String suffix;
    private boolean dragging = false;
    private final Consumer<Float> onChanged;

    /**
     * Creates a new slider setting component with a configurable range,
     * default value and change callback.
     *
     * @param label        The display name shown for the slider.
     * @param min          The minimum selectable value.
     * @param max          The maximum selectable value.
     * @param defaultValue The initial slider value.
     * @param suffix       The suffix appended to the displayed value.
     * @param onChanged    The callback executed whenever the value changes.
     */
    public SliderSetting(
        String label,
        float min,
        float max,
        float defaultValue,
        String suffix,
        Consumer<Float> onChanged
    ) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.suffix = suffix;
        this.onChanged = onChanged;
    }

    /**
     * Returns the fixed vertical size occupied by the slider component.
     *
     * @return The component height in pixels.
     */
    @Override
    public int getHeight() {
        return 34;
    }

    /**
     * Renders the slider component including its background, label,
     * current value display, progress bar and draggable knob.
     * Also processes active dragging updates while the mouse button remains pressed.
     *
     * @param graphics The graphical rendering extraction context.
     * @param font The font renderer used for text drawing operations.
     * @param x The horizontal component position.
     * @param y The vertical component position.
     * @param width The available component width.
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     * @param progress The animation progress factor used for opacity effects.
     */
    @Override
    public void render(
        GuiGraphicsExtractor graphics,
        net.minecraft.client.gui.Font font,
        int x,
        int y,
        int width,
        int mouseX,
        int mouseY,
        float progress
    ) {
        boolean hovered = isMouseOver(mouseX, mouseY, x, y, width);

        int bgCardColor = getAlphaColor(
            hovered ? 0x24262E : 0x1A1B22,
            220 * progress / 255
        );

        fillRoundedRect(
            graphics,
            x,
            y,
            x + width,
            y + getHeight(),
            bgCardColor
        );

        graphics.text(
            font,
            Component.literal(this.label),
            x + 12,
            y + 6,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        String valStr = String.format("%.1f", this.value) + " " + this.suffix;

        graphics.text(
            font,
            Component.literal(valStr),
            x + width - 12 - font.width(valStr),
            y + 6,
            getAlphaColor(0xB71C1C, progress),
            false
        );

        int trackX = x + 12;
        int trackY = y + 22;
        int trackW = width - 24;
        int trackH = 3;

        graphics.fill(
            trackX,
            trackY,
            trackX + trackW,
            trackY + trackH,
            getAlphaColor(0x3A3C45, progress)
        );

        float pct = (this.value - this.min) / (this.max - this.min);
        int fillW = (int) (trackW * pct);

        graphics.fill(
            trackX,
            trackY,
            trackX + fillW,
            trackY + trackH,
            getAlphaColor(0xB71C1C, progress)
        );

        int knobX = trackX + fillW;

        boolean knobHovered =
            mouseX >= knobX - 4
                && mouseX <= knobX + 4
                && mouseY >= trackY - 2
                && mouseY <= trackY + 5;

        int knobColor = (this.dragging || knobHovered)
            ? 0xFFFFFFFF
            : 0xFFD2D2D5;

        graphics.fill(
            knobX - 2,
            trackY - 2,
            knobX + 2,
            trackY + 5,
            getAlphaColor(knobColor, progress)
        );

        if (this.dragging) {
            if (
                GLFW.glfwGetMouseButton(
                    Minecraft.getInstance().getWindow().handle(),
                    GLFW.GLFW_MOUSE_BUTTON_LEFT
                ) == GLFW.GLFW_RELEASE
            ) {
                this.dragging = false;
            } else {
                float mousePct = (float) (mouseX - trackX) / trackW;
                mousePct = Math.clamp(mousePct, 0.0f, 1.0f);

                float newValue = this.min + mousePct * (this.max - this.min);

                if (newValue != this.value) {
                    this.value = newValue;

                    if (this.onChanged != null) {
                        this.onChanged.accept(this.value);
                    }
                }
            }
        }
    }

    /**
     * Handles mouse interactions with the slider track and starts
     * dragging mode when the user clicks inside the valid slider region.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param button The mouse button identifier.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The component width.
     * @return True if the click event was consumed by the slider.
     */
    @Override
    public boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button,
        int x,
        int y,
        int width
    ) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        int trackX = x + 12;
        int trackW = width - 24;

        if (!isMouseOver(mouseX, mouseY, x, y, width)) {
            return false;
        }

        if (mouseX < trackX - 10 || mouseX > trackX + trackW + 10) {
            return false;
        }

        this.dragging = true;

        float mousePct = (float) (mouseX - trackX) / trackW;
        mousePct = Math.clamp(mousePct, 0.0f, 1.0f);

        this.value = this.min + mousePct * (this.max - this.min);

        if (this.onChanged != null) {
            this.onChanged.accept(this.value);
        }

        return true;
    }

    /**
     * Returns the currently selected slider value.
     *
     * @return The active floating-point value.
     */
    public float getValue() {
        return this.value;
    }
}

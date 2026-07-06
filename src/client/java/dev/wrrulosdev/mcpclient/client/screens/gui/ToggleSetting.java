package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.function.Consumer;

public class ToggleSetting extends AbstractSettingComponent {

    private final String label;
    private boolean state;
    private final Consumer<Boolean> onChanged;

    /**
     * Creates a new toggle setting component with an initial state and a callback
     * that is invoked whenever the toggle value changes.
     *
     * @param label        The display name shown for the setting.
     * @param defaultState The initial enabled or disabled state.
     * @param onChanged    The callback executed when the state changes.
     */
    public ToggleSetting(
        String label,
        boolean defaultState,
        Consumer<Boolean> onChanged
    ) {
        this.label = label;
        this.state = defaultState;
        this.onChanged = onChanged;
    }

    /**
     * Returns the fixed vertical size occupied by the toggle component.
     *
     * @return The component height in pixels.
     */
    @Override
    public int getHeight() {
        return 28;
    }

    /**
     * Renders the toggle setting card including its label, switch background,
     * knob position and hover-state visual effects.
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
            y + (getHeight() - font.lineHeight) / 2,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        int swW = 20;
        int swH = 10;
        int swX = x + width - 12 - swW;
        int swY = y + (getHeight() - swH) / 2;
        int switchBg = this.state
            ? 0xB71C1C
            : 0x42444D;

        graphics.fill(
            swX,
            swY,
            swX + swW,
            swY + swH,
            getAlphaColor(switchBg, progress)
        );

        int knobSize = 10;
        int knobX = this.state
            ? (swX + swW - knobSize)
            : swX;

        graphics.fill(
            knobX,
            swY - 1,
            knobX + knobSize,
            swY + swH + 1,
            getAlphaColor(0xFFFFFF, progress)
        );
    }

    /**
     * Handles mouse click interactions to toggle the current state when the
     * component is clicked using the primary mouse button.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param button The mouse button identifier.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The component width.
     * @return True if the click event was consumed by this component.
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
        if (
            button != GLFW.GLFW_MOUSE_BUTTON_LEFT
                || !isMouseOver(mouseX, mouseY, x, y, width)
        ) {
            return false;
        }

        this.state = !this.state;

        if (this.onChanged != null) {
            this.onChanged.accept(this.state);
        }

        return true;
    }

    /**
     * Returns the current toggle state represented by this component.
     *
     * @return True if the setting is enabled, otherwise false.
     */
    public boolean getState() {
        return this.state;
    }
}
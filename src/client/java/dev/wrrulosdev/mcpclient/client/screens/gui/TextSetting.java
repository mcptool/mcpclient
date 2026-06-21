package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.function.Consumer;

public class TextSetting extends AbstractSettingComponent {

    private final String label;
    private String text;
    private final Consumer<String> onChanged;
    private boolean isFocused;

    /**
     * Creates a new text setting component with an initial text value and a callback
     * that is invoked whenever the text content changes.
     *
     * @param label       The display name shown for the setting.
     * @param defaultText The initial text stored in the component.
     * @param onChanged   The callback executed when the text changes.
     */
    public TextSetting(
        String label,
        String defaultText,
        Consumer<String> onChanged
    ) {
        this.label = label;
        this.text = defaultText;
        this.onChanged = onChanged;
        this.isFocused = false;
    }

    /**
     * Returns the fixed vertical size occupied by the text component.
     *
     * @return The component height in pixels.
     */
    @Override
    public int getHeight() {
        return 28;
    }

    /**
     * Renders the text setting card including its label, text box background,
     * current text content, blinking cursor, and hover-state visual effects.
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

        int boxWidth = 100;
        int boxHeight = 14;
        int boxX = x + width - 12 - boxWidth;
        int boxY = y + (getHeight() - boxHeight) / 2;

        int boxBgColor = this.isFocused ? 0x42444D : 0x2A2C33;

        graphics.fill(
            boxX,
            boxY,
            boxX + boxWidth,
            boxY + boxHeight,
            getAlphaColor(boxBgColor, progress)
        );

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
    }

    /**
     * Handles mouse click interactions to toggle the focus state of the text box
     * when the component is clicked using the primary mouse button.
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
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        boolean clickedInside = isMouseOver(mouseX, mouseY, x, y, width);
        this.isFocused = clickedInside;
        return clickedInside;
    }

    /**
     * Handles key press events for text manipulation, such as backspace
     * for removing characters, when the component has active focus.
     *
     * @param keyCode The GLFW key code of the pressed key.
     * @param scanCode The system-specific scancode of the key.
     * @param modifiers A bitfield describing which modifier keys were held down.
     * @return True if the key press was processed, otherwise false.
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !this.text.isEmpty()) {
            this.text = this.text.substring(0, this.text.length() - 1);
            if (this.onChanged != null) {
                this.onChanged.accept(this.text);
            }
            return true;
        }

        return false;
    }

    /**
     * Handles character typing events to append newly typed text to the component
     * when it has active focus.
     *
     * @param codePoint The Unicode code point of the typed character.
     * @param modifiers A bitfield describing which modifier keys were held down.
     * @return True if the typed character was processed, otherwise false.
     */
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isFocused) {
            return false;
        }

        if (net.minecraft.util.StringUtil.isAllowedChatCharacter(codePoint)) {
            this.text += codePoint;
            if (this.onChanged != null) {
                this.onChanged.accept(this.text);
            }
            return true;
        }

        return false;
    }

    /**
     * Returns the current text content stored in this component.
     *
     * @return The current text string.
     */
    public String getText() {
        return this.text;
    }
}
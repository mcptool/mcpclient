package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.function.Consumer;

public class KeybindSetting extends AbstractSettingComponent {

    private final String label;
    private int key;
    private boolean listening = false;
    private final Consumer<Integer> onChanged;

    /**
     * Creates a new keybind setting component with an initial key assignment
     * and a callback invoked whenever the keybind changes.
     *
     * @param label      The display name shown for the keybind setting.
     * @param defaultKey The initial keyboard key assigned to this setting.
     * @param onChanged  The callback executed when a new key is assigned.
     */
    public KeybindSetting(
        String label,
        int defaultKey,
        Consumer<Integer> onChanged
    ) {
        this.label = label;
        this.key = defaultKey;
        this.onChanged = onChanged;
    }

    /**
     * Returns the fixed vertical size occupied by the keybind component.
     *
     * @return The component height in pixels.
     */
    @Override
    public int getHeight() {
        return 28;
    }

    /**
     * Renders the keybind setting card including label text, assigned key display,
     * hover effects and listening-state visual feedback.
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

        String keyName = this.listening
            ? "[...]"
            : GLFW.glfwGetKeyName(this.key, 0);

        if (keyName == null || keyName.isEmpty()) {
            keyName = this.key == GLFW.GLFW_KEY_UNKNOWN
                ? "NONE"
                : "KEY " + this.key;
        }

        keyName = keyName.toUpperCase();

        int boxW = font.width(keyName) + 14;
        int boxH = 14;
        int boxX = x + width - 12 - boxW;
        int boxY = y + (getHeight() - boxH) / 2;

        int boxBg = this.listening
            ? 0xB71C1C
            : (hovered ? 0x30323D : 0x252731);

        graphics.fill(
            boxX,
            boxY,
            boxX + boxW,
            boxY + boxH,
            getAlphaColor(boxBg, progress)
        );

        graphics.text(
            font,
            Component.literal(keyName),
            boxX + (boxW - font.width(keyName)) / 2,
            boxY + (boxH - font.lineHeight) / 2,
            getAlphaColor(0xFFFFFF, progress),
            false
        );
    }

    /**
     * Handles mouse click interactions to enter or exit key listening mode.
     * When active, the next valid keyboard input will become the new keybind.
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
            button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                && isMouseOver(mouseX, mouseY, x, y, width)
        ) {
            this.listening = !this.listening;
            return true;
        } else if (this.listening) {
            this.listening = false;
            return true;
        }

        return false;
    }

    /**
     * Processes keyboard input while the component is listening for a new keybind.
     * Escape or Delete clears the assigned key while any other key becomes the
     * newly registered shortcut.
     *
     * @param event The keyboard event containing the pressed key information.
     * @return True if the event was consumed by the keybind listener.
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (!this.listening) {
            return false;
        }

        this.key = (
            event.key() == GLFW.GLFW_KEY_ESCAPE
                || event.key() == GLFW.GLFW_KEY_DELETE
        )
            ? GLFW.GLFW_KEY_UNKNOWN
            : event.key();

        this.listening = false;

        if (this.onChanged != null) {
            this.onChanged.accept(this.key);
        }

        return true;
    }

    /**
     * Returns the currently assigned keyboard key code.
     *
     * @return The active GLFW key identifier.
     */
    public int getKey() {
        return this.key;
    }
}
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
            : getKeyName(this.key);
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
     * Converts a GLFW key code into a user-friendly display name.
     *
     * @param key The GLFW key code.
     * @return A readable name for the specified key.
     */
    private static String getKeyName(int key) {
        String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));

        if (name != null && !name.isEmpty()) {
            return name.toUpperCase();
        }

        return switch (key) {
            case GLFW.GLFW_KEY_UNKNOWN -> "NONE";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            case GLFW.GLFW_KEY_LEFT_SUPER -> "LWIN";
            case GLFW.GLFW_KEY_RIGHT_SUPER -> "RWIN";
            case GLFW.GLFW_KEY_MENU -> "MENU";
            case GLFW.GLFW_KEY_TAB -> "TAB";
            case GLFW.GLFW_KEY_ENTER -> "ENTER";
            case GLFW.GLFW_KEY_ESCAPE -> "ESC";
            case GLFW.GLFW_KEY_BACKSPACE -> "BACKSPACE";
            case GLFW.GLFW_KEY_INSERT -> "INS";
            case GLFW.GLFW_KEY_DELETE -> "DEL";
            case GLFW.GLFW_KEY_HOME -> "HOME";
            case GLFW.GLFW_KEY_END -> "END";
            case GLFW.GLFW_KEY_PAGE_UP -> "PGUP";
            case GLFW.GLFW_KEY_PAGE_DOWN -> "PGDN";
            case GLFW.GLFW_KEY_UP -> "UP";
            case GLFW.GLFW_KEY_DOWN -> "DOWN";
            case GLFW.GLFW_KEY_LEFT -> "LEFT";
            case GLFW.GLFW_KEY_RIGHT -> "RIGHT";
            case GLFW.GLFW_KEY_CAPS_LOCK -> "CAPS";
            case GLFW.GLFW_KEY_SCROLL_LOCK -> "SCROLL";
            case GLFW.GLFW_KEY_NUM_LOCK -> "NUMLOCK";
            case GLFW.GLFW_KEY_PRINT_SCREEN -> "PRTSC";
            case GLFW.GLFW_KEY_PAUSE -> "PAUSE";
            case GLFW.GLFW_KEY_F1 -> "F1";
            case GLFW.GLFW_KEY_F2 -> "F2";
            case GLFW.GLFW_KEY_F3 -> "F3";
            case GLFW.GLFW_KEY_F4 -> "F4";
            case GLFW.GLFW_KEY_F5 -> "F5";
            case GLFW.GLFW_KEY_F6 -> "F6";
            case GLFW.GLFW_KEY_F7 -> "F7";
            case GLFW.GLFW_KEY_F8 -> "F8";
            case GLFW.GLFW_KEY_F9 -> "F9";
            case GLFW.GLFW_KEY_F10 -> "F10";
            case GLFW.GLFW_KEY_F11 -> "F11";
            case GLFW.GLFW_KEY_F12 -> "F12";
            case GLFW.GLFW_KEY_F13 -> "F13";
            case GLFW.GLFW_KEY_F14 -> "F14";
            case GLFW.GLFW_KEY_F15 -> "F15";
            case GLFW.GLFW_KEY_F16 -> "F16";
            case GLFW.GLFW_KEY_F17 -> "F17";
            case GLFW.GLFW_KEY_F18 -> "F18";
            case GLFW.GLFW_KEY_F19 -> "F19";
            case GLFW.GLFW_KEY_F20 -> "F20";
            case GLFW.GLFW_KEY_F21 -> "F21";
            case GLFW.GLFW_KEY_F22 -> "F22";
            case GLFW.GLFW_KEY_F23 -> "F23";
            case GLFW.GLFW_KEY_F24 -> "F24";
            case GLFW.GLFW_KEY_F25 -> "F25";
            case GLFW.GLFW_KEY_KP_0 -> "NUM0";
            case GLFW.GLFW_KEY_KP_1 -> "NUM1";
            case GLFW.GLFW_KEY_KP_2 -> "NUM2";
            case GLFW.GLFW_KEY_KP_3 -> "NUM3";
            case GLFW.GLFW_KEY_KP_4 -> "NUM4";
            case GLFW.GLFW_KEY_KP_5 -> "NUM5";
            case GLFW.GLFW_KEY_KP_6 -> "NUM6";
            case GLFW.GLFW_KEY_KP_7 -> "NUM7";
            case GLFW.GLFW_KEY_KP_8 -> "NUM8";
            case GLFW.GLFW_KEY_KP_9 -> "NUM9";
            case GLFW.GLFW_KEY_KP_DECIMAL -> "NUM.";
            case GLFW.GLFW_KEY_KP_DIVIDE -> "NUM/";
            case GLFW.GLFW_KEY_KP_MULTIPLY -> "NUM*";
            case GLFW.GLFW_KEY_KP_SUBTRACT -> "NUM-";
            case GLFW.GLFW_KEY_KP_ADD -> "NUM+";
            case GLFW.GLFW_KEY_KP_ENTER -> "NUMENTER";
            case GLFW.GLFW_KEY_KP_EQUAL -> "NUM=";
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_MINUS -> "-";
            case GLFW.GLFW_KEY_EQUAL -> "=";
            case GLFW.GLFW_KEY_LEFT_BRACKET -> "[";
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> "]";
            case GLFW.GLFW_KEY_BACKSLASH -> "\\";
            case GLFW.GLFW_KEY_SEMICOLON -> ";";
            case GLFW.GLFW_KEY_APOSTROPHE -> "'";
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> "`";
            case GLFW.GLFW_KEY_COMMA -> ",";
            case GLFW.GLFW_KEY_PERIOD -> ".";
            case GLFW.GLFW_KEY_SLASH -> "/";
            default -> "KEY " + key;
        };
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
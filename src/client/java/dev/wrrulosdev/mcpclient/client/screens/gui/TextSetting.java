package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;
import java.util.function.Consumer;

public class TextSetting extends AbstractSettingComponent {

    private final String label;
    private String text;
    private final Consumer<String> onChanged;
    private boolean isFocused;
    private int cursorIndex = 0;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    /**
     * Constructs a new TextSetting component.
     *
     * @param label       The display label for the setting
     * @param defaultText The initial string value
     * @param onChanged   Callback triggered whenever the text content changes
     */
    public TextSetting(String label, String defaultText, Consumer<String> onChanged) {
        this.label = label;
        this.text = defaultText;
        this.onChanged = onChanged;
        this.isFocused = false;
        this.cursorIndex = defaultText.length();
    }

    /**
     * @return The height of the component in pixels
     */
    @Override
    public int getHeight() {
        return 28;
    }

    /**
     * Renders the text setting, including the label, input box, text selection,
     * cursor, and scissor-based clipping for overflow.
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
        boolean hovered = isMouseOver(mouseX, mouseY, x, y, width);

        int bgCardColor = getAlphaColor(hovered ? 0x24262E : 0x1A1B22, 220 * progress / 255);
        fillRoundedRect(graphics, x, y, x + width, y + getHeight(), bgCardColor);
        graphics.text(font, Component.literal(this.label), x + 12, y + (getHeight() - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);

        int boxWidth = 100;
        int boxHeight = 14;
        int boxX = x + width - 12 - boxWidth;
        int boxY = y + (getHeight() - boxHeight) / 2;
        int boxBgColor = this.isFocused ? 0x42444D : 0x2A2C33;
        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, getAlphaColor(boxBgColor, progress));

        int cursorXPos = font.width(text.substring(0, cursorIndex));
        int scrollOffset = 0;

        if (cursorXPos > boxWidth - 10) {
            scrollOffset = cursorXPos - (boxWidth - 10);
        } else if (cursorXPos < 0) {
            scrollOffset = cursorXPos;
        }

        graphics.enableScissor(boxX + 2, boxY + 2, boxX + boxWidth - 2, boxY + boxHeight - 2);

        if (selectionStart != -1 && selectionEnd != -1) {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            int selX = boxX + 4 + font.width(text.substring(0, start)) - scrollOffset;
            int selW = font.width(text.substring(start, end));
            graphics.fill(selX, boxY + 2, selX + selW, boxY + boxHeight - 2, 0x800078D7);
        }

        graphics.text(font, Component.literal(this.text), boxX + 4 - scrollOffset, boxY + (boxHeight - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);

        if (this.isFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int cursorX = boxX + 4 + cursorXPos - scrollOffset;
            graphics.fill(cursorX, boxY + 2, cursorX + 1, boxY + boxHeight - 2, 0xFFFFFFFF);
        }

        graphics.disableScissor();
    }

    /**
     * Handles mouse click events to determine focus state.
     * * @return true if the click was within the component boundaries
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;
        boolean clickedInside = isMouseOver(mouseX, mouseY, x, y, width);
        this.isFocused = clickedInside;

        if (clickedInside) {
            selectionStart = -1;
            selectionEnd = -1;
        }
        return clickedInside;
    }

    /**
     * Processes keyboard input for navigation, text selection, and clipboard operations.
     *
     * @return true if the event was handled
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused) return false;

        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) {
            selectionStart = -1; selectionEnd = -1;
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            cursorIndex = Math.max(0, cursorIndex - 1);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            cursorIndex = Math.min(text.length(), cursorIndex + 1);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_A && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            selectionStart = 0;
            selectionEnd = text.length();
            cursorIndex = text.length();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_C && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            if (selectionStart != -1) Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(Math.min(selectionStart, selectionEnd), Math.max(selectionStart, selectionEnd)));
            else Minecraft.getInstance().keyboardHandler.setClipboard(text);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_V && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_X && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            if (selectionStart != -1) {
                Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(Math.min(selectionStart, selectionEnd), Math.max(selectionStart, selectionEnd)));
                insertText("");
            } else {
                Minecraft.getInstance().keyboardHandler.setClipboard(text);
                this.text = "";
                cursorIndex = 0;
                notifyChange();
            }

            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (selectionStart != -1) {
                insertText("");
                selectionStart = -1; selectionEnd = -1;
            } else if (cursorIndex > 0) {
                text = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex);
                cursorIndex--;
                notifyChange();
            }

            return true;
        }

        return false;
    }

    /**
     * Handles character input events.
     *
     * @return true if the character was inserted
     */
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isFocused) return false;

        if (StringUtil.isAllowedChatCharacter(codePoint)) {
            insertText(String.valueOf(codePoint));
            return true;
        }

        return false;
    }

    /**
     * Internal helper to insert text at the cursor position or replace a selection.
     *
     * @param str The string to insert
     */
    private void insertText(String str) {
        if (selectionStart != -1) {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            text = text.substring(0, start) + str + text.substring(end);
            cursorIndex = start + str.length();
            selectionStart = -1; selectionEnd = -1;
        } else {
            text = text.substring(0, cursorIndex) + str + text.substring(cursorIndex);
            cursorIndex += str.length();
        }

        notifyChange();
    }

    /**
     * Triggers the onChanged callback if it exists.
     */
    private void notifyChange() {
        if (this.onChanged != null) this.onChanged.accept(this.text);
    }

    /**
     * @return The current text in the component
     */
    public String getText() {
        return this.text;
    }
}
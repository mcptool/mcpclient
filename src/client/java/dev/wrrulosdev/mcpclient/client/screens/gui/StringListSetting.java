package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StringListSetting extends AbstractSettingComponent {

    private final String label;
    private final List<String> values;
    private final Consumer<List<String>> onChanged;
    private boolean isExpanded = false;
    private String currentInput = "";
    private boolean isInputFocused = false;
    private int cursorIndex = 0;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    /**
     * Constructs a new StringListSetting configuration component.
     *
     * @param label         The display label description for the setting
     * @param defaultValues The initial list configuration state of strings
     * @param onChanged     Callback triggered whenever elements are added or removed
     */
    public StringListSetting(String label, List<String> defaultValues, Consumer<List<String>> onChanged) {
        this.label = label;
        this.values = new ArrayList<>(defaultValues);
        this.onChanged = onChanged;
    }

    /**
     * Dynamically calculates the height of the component layout framework context.
     *
     * @return The exact height resolution configuration requirement in pixels.
     */
    @Override
    public int getHeight() {
        if (!this.isExpanded) {
            return 28;
        }

        return 28 + 26 + (this.values.size() * 20) + 6;
    }

    /**
     * Renders the complex list setting component layout fluidly based on the current dimensions.
     *
     * @param graphics The graphical rendering extraction context pipeline framework
     * @param font     The engine text font rendering asset core instance
     * @param x        The horizontal starting coordinate position
     * @param y        The vertical starting coordinate position
     * @param width    The dynamic responsive constraints width value boundary
     * @param mouseX   The current horizontal mouse pointer resolution map tracking
     * @param mouseY   The current vertical mouse pointer resolution map tracking
     * @param progress The layout alpha interpolation frame factor
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
        boolean headerHovered = isMouseOver(mouseX, mouseY, x, y, width) && mouseY < y + 28;
        int bgCardColor = getAlphaColor(headerHovered ? 0x24262E : 0x1A1B22, 220 * progress / 255);
        fillRoundedRect(graphics, x, y, x + width, y + getHeight(), bgCardColor);
        graphics.text(font, Component.literal(this.label), x + 12, y + (28 - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);
        String counterText = this.values.size() + (this.isExpanded ? " ▲" : " ▼");
        int counterWidth = font.width(counterText);
        graphics.text(font, Component.literal(counterText), x + width - 12 - counterWidth, y + (28 - font.lineHeight) / 2, getAlphaColor(0xAAAAAA, progress), false);

        if (!this.isExpanded) return;

        graphics.fill(x + 12, y + 28, x + width - 12, y + 29, getAlphaColor(0x33FFFFFF, progress));

        int boxX = x + 12;
        int boxY = y + 34;
        int boxWidth = width - 54;
        int boxHeight = 14;
        int boxBgColor = this.isInputFocused ? 0x42444D : 0x2A2C33;

        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, getAlphaColor(boxBgColor, progress));

        int cursorXPos = font.width(this.currentInput.substring(0, this.cursorIndex));
        int scrollOffset = 0;

        if (cursorXPos > boxWidth - 10) {
            scrollOffset = cursorXPos - (boxWidth - 10);
        }

        graphics.enableScissor(boxX + 2, boxY + 2, boxX + boxWidth - 2, boxY + boxHeight - 2);

        if (this.selectionStart != -1 && this.selectionEnd != -1) {
            int start = Math.min(this.selectionStart, this.selectionEnd);
            int end = Math.max(this.selectionStart, this.selectionEnd);
            int selX = boxX + 4 + font.width(this.currentInput.substring(0, start)) - scrollOffset;
            int selW = font.width(this.currentInput.substring(start, end));
            graphics.fill(selX, boxY + 2, selX + selW, boxY + boxHeight - 2, 0x800078D7);
        }

        graphics.text(font, Component.literal(this.currentInput), boxX + 4 - scrollOffset, boxY + (boxHeight - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);

        if (this.isInputFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int cursorX = boxX + 4 + cursorXPos - scrollOffset;
            graphics.fill(cursorX, boxY + 2, cursorX + 1, boxY + boxHeight - 2, 0xFFFFFFFF);
        }

        graphics.disableScissor();

        int addBtnX = boxX + boxWidth + 6;
        boolean addHovered = isMouseOver(mouseX, mouseY, addBtnX, boxY, 24) && mouseY < boxY + boxHeight;
        int addBtnColor = addHovered ? 0x4CAF50 : 0x2E7D32;
        graphics.fill(addBtnX, boxY, addBtnX + 24, boxY + boxHeight, getAlphaColor(addBtnColor, progress));
        graphics.text(font, Component.literal("+"), addBtnX + (24 - font.width("+")) / 2, boxY + (boxHeight - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);
        int listStartY = y + 54;

        for (int i = 0; i < this.values.size(); i++) {
            int itemY = listStartY + (i * 20);
            String itemText = this.values.get(i);
            boolean rowHovered = isMouseOver(mouseX, mouseY, x + 12, itemY, width - 24) && mouseY < itemY + 18;
            int rowBgColor = rowHovered ? 0x2D303B : 0x22242C;
            fillRoundedRect(graphics, x + 12, itemY, x + width - 12, itemY + 18, getAlphaColor(rowBgColor, progress));
            graphics.enableScissor(x + 18, itemY, x + width - 38, itemY + 18);
            graphics.text(font, Component.literal(itemText), x + 18, itemY + (18 - font.lineHeight) / 2, getAlphaColor(0xDDDDDD, progress), false);
            graphics.disableScissor();
            int delBtnX = x + width - 28;
            int delBtnY = itemY + 2;
            boolean delHovered = isMouseOver(mouseX, mouseY, delBtnX, delBtnY, 14) && mouseY < delBtnY + 14;
            int delColor = delHovered ? 0xD32F2F : 0x757575;
            graphics.text(font, Component.literal("×"), delBtnX + (14 - font.width("×")) / 2, delBtnY + (14 - font.lineHeight) / 2, getAlphaColor(delColor, progress), false);
        }
    }

    /**
     * Intercepts and parses physical map selection coordinates vector points.
     *
     * @return true if the event cycle criteria parameters matches layout space context boundaries.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;

        if (isMouseOver(mouseX, mouseY, x, y, width) && mouseY < y + 28) {
            this.isExpanded = !this.isExpanded;
            if (!this.isExpanded) this.isInputFocused = false;
            return true;
        }

        if (!this.isExpanded) return false;

        int boxX = x + 12;
        int boxY = y + 34;
        int boxWidth = width - 54;
        int boxHeight = 14;

        if (mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + boxHeight) {
            this.isInputFocused = true;
            this.selectionStart = -1;
            this.selectionEnd = -1;
            return true;
        } else {
            this.isInputFocused = false;
        }

        int addBtnX = boxX + boxWidth + 6;

        if (mouseX >= addBtnX && mouseX <= addBtnX + 24 && mouseY >= boxY && mouseY <= boxY + boxHeight) {
            handleCommitEntry();
            return true;
        }

        int listStartY = y + 54;

        for (int i = 0; i < this.values.size(); i++) {
            int itemY = listStartY + (i * 20);
            int delBtnX = x + width - 28;
            int delBtnY = itemY + 2;

            if (mouseX >= delBtnX && mouseX <= delBtnX + 14 && mouseY >= delBtnY && mouseY <= delBtnY + 14) {
                this.values.remove(i);
                notifyChange();
                return true;
            }
        }

        return isMouseOver(mouseX, mouseY, x, y, width);
    }

    /**
     * Processes keyboard parameters events inside active matrix scopes.
     *
     * @return true if target navigation contexts criteria state updates successfully.
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isInputFocused || !this.isExpanded) return false;

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            handleCommitEntry();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.selectionStart = -1;
            this.selectionEnd = -1;
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.cursorIndex = Math.max(0, this.cursorIndex - 1);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.cursorIndex = Math.min(this.currentInput.length(), this.cursorIndex + 1);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_A && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            this.selectionStart = 0;
            this.selectionEnd = this.currentInput.length();
            this.cursorIndex = this.currentInput.length();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_C && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            if (this.selectionStart != -1) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.currentInput.substring(Math.min(this.selectionStart, this.selectionEnd), Math.max(this.selectionStart, this.selectionEnd)));
            } else {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.currentInput);
            }

            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_V && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_X && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            if (this.selectionStart != -1) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.currentInput.substring(Math.min(this.selectionStart, this.selectionEnd), Math.max(this.selectionStart, this.selectionEnd)));
                insertText("");
            } else {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.currentInput);
                this.currentInput = "";
                this.cursorIndex = 0;
            }

            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.selectionStart != -1) {
                insertText("");
                this.selectionStart = -1;
                this.selectionEnd = -1;
            } else if (this.cursorIndex > 0) {
                this.currentInput = this.currentInput.substring(0, this.cursorIndex - 1) + this.currentInput.substring(this.cursorIndex);
                this.cursorIndex--;
            }

            return true;
        }

        return false;
    }

    /**
     * Intercepts character typing engine loops pipeline requests mapping keys.
     *
     * @return true if entry mapping rules valid constraints match successfully.
     */
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isInputFocused || !this.isExpanded) return false;

        if (StringUtil.isAllowedChatCharacter(codePoint)) {
            insertText(String.valueOf(codePoint));
            return true;
        }

        return false;
    }

    /**
     * Helper action mapping parameters layout modifications.
     *
     * @param str Target dynamic characters insertion array data string.
     */
    private void insertText(String str) {
        if (this.selectionStart != -1) {
            int start = Math.min(this.selectionStart, this.selectionEnd);
            int end = Math.max(this.selectionStart, this.selectionEnd);
            this.currentInput = this.currentInput.substring(0, start) + str + this.currentInput.substring(end);
            this.cursorIndex = start + str.length();
            this.selectionStart = -1;
            this.selectionEnd = -1;
        } else {
            this.currentInput = this.currentInput.substring(0, this.cursorIndex) + str + this.currentInput.substring(this.cursorIndex);
            this.cursorIndex += str.length();
        }
    }

    /**
     * Evaluates text fields buffer storage variables to commit into the list context.
     */
    private void handleCommitEntry() {
        String trimmed = this.currentInput.trim();

        if (!trimmed.isEmpty()) {
            this.values.add(trimmed);
            this.currentInput = "";
            this.cursorIndex = 0;
            this.selectionStart = -1;
            this.selectionEnd = -1;
            notifyChange();
        }
    }

    /**
     * Triggers synchronization pipeline mappings tracking operations updates callback listeners.
     */
    private void notifyChange() {
        if (this.onChanged != null) {
            this.onChanged.accept(new ArrayList<>(this.values));
        }
    }

    /**
     * Returns the array list values data mapped criteria constraints.
     *
     * @return Current elements tracking instances reference mapping tree.
     */
    public List<String> getValues() {
        return new ArrayList<>(this.values);
    }
}
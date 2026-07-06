package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MapSetting extends AbstractSettingComponent {

    private final String label;
    private final Map<String, String> values;
    private final Consumer<Map<String, String>> onChanged;

    public String namePlaceholder;
    public String contentPlaceholder;
    public String nameInput = "";
    public String contentInput = "";

    private boolean isExpanded = false;
    private boolean isNameFocused = false;
    private boolean isContentFocused = false;

    /**
     * Initializes the map setting component.
     *
     * @param label          The display label for the setting.
     * @param defaultValues The initial key-value pairs.
     * @param initialName    The placeholder text for the name field.
     * @param initialContent The placeholder text for the content field.
     * @param onChanged      Callback triggered when the map changes.
     */
    public MapSetting(String label, Map<String, String> defaultValues, String initialName, String initialContent, Consumer<Map<String, String>> onChanged) {
        this.label = label;
        this.values = new LinkedHashMap<>(defaultValues);
        this.namePlaceholder = initialName != null && !initialName.isEmpty() ? initialName : "Name...";
        this.contentPlaceholder = initialContent != null && !initialContent.isEmpty() ? initialContent : "Content...";
        this.onChanged = onChanged;
    }

    /**
     * Calculates the total height of the component based on its expansion state.
     * * @return The calculated height in pixels.
     */
    @Override
    public int getHeight() {
        if (!this.isExpanded) return 28;
        return 28 + 26 + (this.values.size() * 20) + 6;
    }

    /**
     * Renders the UI component, including the header and, if expanded,
     * the input fields and the list of entries.
     *
     * @param graphics The graphics extractor for rendering.
     * @param font     The font renderer.
     * @param x        The x-coordinate.
     * @param y        The y-coordinate.
     * @param width    The width of the component.
     * @param mouseX   The current x-coordinate of the mouse.
     * @param mouseY   The current y-coordinate of the mouse.
     * @param progress The animation progress value.
     */
    @Override
    public void render(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, int mouseX, int mouseY, float progress) {
        boolean headerHovered = isMouseOver(mouseX, mouseY, x, y, width) && mouseY < y + 28;
        int bgColor = getAlphaColor(headerHovered ? 0x24262E : 0x1A1B22, 220 * progress / 255);
        fillRoundedRect(graphics, x, y, x + width, y + getHeight(), bgColor);

        graphics.text(font, Component.literal(this.label), x + 12, y + (28 - font.lineHeight) / 2, getAlphaColor(0xFFFFFF, progress), false);
        String counter = this.values.size() + (this.isExpanded ? " ▲" : " ▼");
        graphics.text(font, Component.literal(counter), x + width - 12 - font.width(counter), y + (28 - font.lineHeight) / 2, getAlphaColor(0xAAAAAA, progress), false);

        if (!this.isExpanded) return;

        graphics.fill(x + 12, y + 28, x + width - 12, y + 29, getAlphaColor(0x33FFFFFF, progress));

        renderInputFields(graphics, font, x, y, width, progress);
        renderList(graphics, font, x, y, width, mouseX, mouseY, progress);
    }

    /**
     * Renders the input fields for key/value pairs and the addition button.
     *
     * @param graphics The graphics extractor.
     * @param font     The font renderer.
     * @param x        The x-coordinate.
     * @param y        The y-coordinate.
     * @param width    The component width.
     * @param progress The animation progress.
     */
    private void renderInputFields(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, float progress) {
        int boxY = y + 34;
        int boxH = 14;
        int inputWidth = (width - 24 - 20 - 8) / 2;
        int addBtnX = x + width - 12 - 20;
        boolean isDuplicate = !nameInput.trim().isEmpty() && values.containsKey(nameInput.trim());
        drawInputField(graphics, font, x + 12, boxY, inputWidth, boxH, this.namePlaceholder, nameInput, isNameFocused, isDuplicate, progress);
        drawInputField(graphics, font, x + 12 + inputWidth + 4, boxY, inputWidth, boxH, this.contentPlaceholder, contentInput, isContentFocused, false, progress);
        graphics.fill(addBtnX, boxY, addBtnX + 20, boxY + boxH, getAlphaColor(0x2E7D32, progress));
        graphics.text(font, Component.literal("+"), addBtnX + 7, boxY + 3, getAlphaColor(0xFFFFFF, progress), false);
    }

    /**
     * Draws a single text input box with placeholder or user input.
     *
     * @param graphics    The graphics extractor.
     * @param font        The font renderer.
     * @param x           The x-coordinate.
     * @param y           The y-coordinate.
     * @param w           The width of the field.
     * @param h           The height of the field.
     * @param placeholder The placeholder text.
     * @param text        The current text input.
     * @param focused     Whether the field is focused.
     * @param isError     Whether the input is in an error state (duplicate).
     * @param progress    The animation progress.
     */
    private void drawInputField(GuiGraphicsExtractor graphics, Font font, int x, int y, int w, int h, String placeholder, String text, boolean focused, boolean isError, float progress) {
        int bgColor = focused ? 0x42444D : 0x2A2C33;
        graphics.fill(x, y, x + w, y + h, getAlphaColor(bgColor, progress));
        graphics.enableScissor(x + 2, y + 2, x + w - 2, y + h - 2);

        int textColor = isError ? 0xFF5555 : 0xFFFFFF;

        if (text.isEmpty() && !focused) {
            graphics.text(font, Component.literal(placeholder), x + 4, y + (h - font.lineHeight) / 2, getAlphaColor(0x55FFFFFF, progress), false);
        } else {
            graphics.text(font, Component.literal(text), x + 4, y + (h - font.lineHeight) / 2, getAlphaColor(textColor, progress), false);
        }

        graphics.disableScissor();
    }

    /**
     * Renders the current list of key-value pairs.
     *
     * @param graphics The graphics extractor.
     * @param font     The font renderer.
     * @param x        The x-coordinate.
     * @param y        The y-coordinate.
     * @param width    The component width.
     * @param mouseX   The current mouse x-coordinate.
     * @param mouseY   The current mouse y-coordinate.
     * @param progress The animation progress.
     */
    private void renderList(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, int mouseX, int mouseY, float progress) {
        int listStartY = y + 54;
        List<Map.Entry<String, String>> entries = new ArrayList<>(this.values.entrySet());

        for (int i = 0; i < entries.size(); i++) {
            int itemY = listStartY + (i * 20);
            Map.Entry<String, String> entry = entries.get(i);
            String text = entry.getKey() + " ➔ " + entry.getValue();
            fillRoundedRect(graphics, x + 12, itemY, x + width - 12, itemY + 18, getAlphaColor(0x22242C, progress));
            graphics.enableScissor(x + 18, itemY, x + width - 38, itemY + 18);
            graphics.text(font, Component.literal(text), x + 18, itemY + 5, getAlphaColor(0xDDDDDD, progress), false);
            graphics.disableScissor();
            graphics.text(font, Component.literal("×"), x + width - 24, itemY + 5, getAlphaColor(0x757575, progress), false);
        }
    }

    /**
     * Handles mouse clicks to toggle expansion, focus inputs, add items, or remove entries.
     *
     * @param mouseX The mouse x-coordinate.
     * @param mouseY The mouse y-coordinate.
     * @param button The button pressed.
     * @param x      The component x-coordinate.
     * @param y      The component y-coordinate.
     * @param width  The component width.
     * @return True if the click was consumed.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;

        if (isMouseOver(mouseX, mouseY, x, y, width) && mouseY < y + 28) {
            this.isExpanded = !this.isExpanded;
            return true;
        }

        if (!this.isExpanded) return false;

        int boxY = y + 34;
        int boxH = 14;
        int inputWidth = (width - 24 - 20 - 8) / 2;

        if (mouseX >= x + 12 && mouseX <= x + 12 + inputWidth && mouseY >= boxY && mouseY <= boxY + boxH) {
            isNameFocused = true; isContentFocused = false; return true;
        }

        if (mouseX >= x + 12 + inputWidth + 4 && mouseX <= x + 12 + (inputWidth * 2) + 4 && mouseY >= boxY && mouseY <= boxY + boxH) {
            isNameFocused = false; isContentFocused = true; return true;
        }

        int addBtnX = x + width - 12 - 20;

        if (mouseX >= addBtnX && mouseX <= addBtnX + 20 && mouseY >= boxY && mouseY <= boxY + boxH) {
            handleCommitEntry(); return true;
        }

        int listStartY = y + 54;
        List<String> keys = new ArrayList<>(values.keySet());

        for (int i = 0; i < keys.size(); i++) {
            int itemY = listStartY + (i * 20);

            if (mouseX >= x + width - 28 && mouseX <= x + width - 14 && mouseY >= itemY && mouseY <= itemY + 14) {
                values.remove(keys.get(i)); notifyChange(); return true;
            }
        }

        return false;
    }

    /**
     * Handles keyboard events for entering data or deleting text in focused inputs.
     *
     * @param keyCode   The key code.
     * @param scanCode  The scan code.
     * @param modifiers The active modifiers.
     * @return True if the key press was consumed.
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isExpanded || (!this.isNameFocused && !this.isContentFocused)) return false;

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            handleCommitEntry(); return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (isNameFocused && !nameInput.isEmpty()) nameInput = nameInput.substring(0, nameInput.length() - 1);
            else if (isContentFocused && !contentInput.isEmpty()) contentInput = contentInput.substring(0, contentInput.length() - 1);
            return true;
        }

        return false;
    }

    /**
     * Handles typing characters into the focused input field.
     *
     * @param codePoint The typed character.
     * @param modifiers The active modifiers.
     * @return True if the character was processed.
     */
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isExpanded || (!this.isNameFocused && !this.isContentFocused)) return false;

        if (StringUtil.isAllowedChatCharacter(codePoint)) {
            if (isNameFocused) nameInput += codePoint;
            else contentInput += codePoint;
            return true;
        }

        return false;
    }

    /**
     * Adds a new entry to the map if both input fields are non-empty,
     * then resets the input fields.
     */
    private void handleCommitEntry() {
        String trimmedName = nameInput.trim();
        String trimmedContent = contentInput.trim();

        if (!trimmedName.isEmpty() && !trimmedContent.isEmpty() && !this.values.containsKey(trimmedName)) {
            this.values.put(trimmedName, trimmedContent);
            this.nameInput = "";
            this.contentInput = "";
            notifyChange();
        }
    }

    /**
     * Triggers the change listener with the updated map values.
     */
    private void notifyChange() {
        if (this.onChanged != null) this.onChanged.accept(new LinkedHashMap<>(this.values));
    }
}
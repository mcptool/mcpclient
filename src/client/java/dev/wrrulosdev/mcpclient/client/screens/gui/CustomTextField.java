package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomTextField extends AbstractWidget {

    private final TextFieldStyle style;
    private final Component placeholder;
    private final Predicate<String> validator;
    private String text = "";
    private int maxLength = 256;
    private int cursorPosition = 0;
    private int selectionEnd = 0;
    private int displayOffset = 0;
    private long lastClickTime = 0;
    private Consumer<String> responder;
    private boolean wasHovered = false;

    /**
     * Private constructor to initialize the custom text field.
     *
     * @param x           The X position on the screen.
     * @param y           The Y position on the screen.
     * @param width       The width of the text field.
     * @param height      The height of the text field.
     * @param placeholder The text component displayable when the field is empty.
     * @param style       The structural and visual configuration of the text field.
     * @param validator   An optional predicate to validate text and change its color.
     */
    private CustomTextField(int x, int y, int width, int height, Component placeholder, TextFieldStyle style, Predicate<String> validator) {
        super(x, y, width, height, Component.empty());
        this.placeholder = placeholder;
        this.style = style;
        this.validator = validator;
    }

    /**
     * Extracts and processes the visual widget state to render background, text, cursor, and placeholder.
     *
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param mouseX   The current X coordinate of the cursor.
     * @param mouseY   The current Y coordinate of the cursor.
     * @param delta    The partial ticks time delta elapsed since last tick frame.
     */
    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int bgColor = getBackgroundColor();
        renderRoundedBackground(graphics, getX(), getY(), width, height, bgColor);

        if (style.borderEnabled) {
            renderBorder(graphics);
        }

        var font = Minecraft.getInstance().font;
        int innerX = getX() + 4;
        int innerY = getY() + (height - font.lineHeight) / 2;
        int maxTextWidth = width - 8;

        String visibleText = font.plainSubstrByWidth(this.text.substring(this.displayOffset), maxTextWidth);

        if (this.text.isEmpty() && !this.isFocused()) {
            String visiblePlaceholder = font.plainSubstrByWidth(this.placeholder.getString(), maxTextWidth);
            graphics.text(font, Component.literal(visiblePlaceholder), innerX, innerY, style.placeholderColor, true);
        } else {
            graphics.text(font, Component.literal(visibleText), innerX, innerY, getTextColor(), true);
        }

        if (this.isFocused()) {
            drawCursorAndSelection(graphics, font, innerX, innerY, visibleText);
        }

        boolean hovered = this.active && this.isMouseOver(mouseX, mouseY);
        long window = Minecraft.getInstance().getWindow().handle();

        if (hovered != wasHovered) {
            GLFW.glfwSetCursor(window, hovered ? GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR) : GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
            wasHovered = hovered;
        }
    }

    /**
     * Draws the blinking cursor line and highlights selected text if applicable.
     *
     * @param graphics    The screen graphics rendering pipeline extractor context.
     * @param font        The current Minecraft font instance.
     * @param innerX      The starting X position for inner text rendering.
     * @param innerY      The starting Y position for inner text rendering.
     * @param visibleText The string currently visible within the text field bounds.
     */
    private void drawCursorAndSelection(GuiGraphicsExtractor graphics, net.minecraft.client.gui.Font font, int innerX, int innerY, String visibleText) {
        boolean renderCursor = (System.currentTimeMillis() / 500L) % 2L == 0L;
        int cursorRelativePos = this.cursorPosition - this.displayOffset;

        if (cursorRelativePos >= 0 && cursorRelativePos <= visibleText.length()) {
            int cursorX = innerX + font.width(visibleText.substring(0, cursorRelativePos));

            if (renderCursor && this.cursorPosition == this.selectionEnd) {
                graphics.fill(cursorX, innerY - 1, cursorX + 1, innerY + font.lineHeight + 1, style.cursorColor);
            }
        }

        if (this.cursorPosition != this.selectionEnd) {
            int start = Math.max(0, Math.min(this.cursorPosition, this.selectionEnd) - this.displayOffset);
            int end = Math.min(visibleText.length(), Math.max(this.cursorPosition, this.selectionEnd) - this.displayOffset);

            if (start < visibleText.length() && end > 0) {
                int startX = innerX + font.width(visibleText.substring(0, start));
                int endX = innerX + font.width(visibleText.substring(0, end));
                graphics.fill(startX, innerY - 1, endX, innerY + font.lineHeight + 1, style.selectionColor);
            }
        }
    }

    /**
     * Generates a procedurally constructed background matrix layer applying a localized 2-pixel corner indentation sequence.
     *
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param x        The relative horizontal screen coordinate offset.
     * @param y        The relative vertical screen coordinate offset.
     * @param width    The complete pixel scale length bounds metric of the component.
     * @param height   The complete pixel scale vertical height bounds metric of the component.
     * @param color    The designated integer hexadecimal ARGB standard profile color payload value.
     */
    private void renderRoundedBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x + 2, y, x + width - 2, y + 1, color);
        graphics.fill(x + 1, y + 1, x + width - 1, y + 2, color);
        graphics.fill(x, y + 2, x + width, y + height - 2, color);
        graphics.fill(x + 1, y + height - 2, x + width - 1, y + height - 1, color);
        graphics.fill(x + 2, y + height - 1, x + width - 2, y + height, color);
    }

    /**
     * Draws segment vectors mapping an identical customized rounded profile structure boundary across target coordinate scales.
     *
     * @param graphics The screen graphics rendering pipeline extractor context.
     */
    private void renderBorder(GuiGraphicsExtractor graphics) {
        int borderColor;

        if (!this.active) {
            borderColor = style.disabledBorderColor;
        } else {
            borderColor = this.isFocused() ? style.focusedBorderColor : style.borderColor;
        }

        int x = getX();
        int y = getY();
        graphics.fill(x + 2, y, x + width - 2, y + 1, borderColor);
        graphics.fill(x + 2, y + height - 1, x + width - 2, y + height, borderColor);
        graphics.fill(x, y + 2, x + 1, y + height - 2, borderColor);
        graphics.fill(x + width - 1, y + 2, x + width, y + height - 2, borderColor);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, borderColor);
        graphics.fill(x + width - 2, y + 1, x + width - 1, y + 2, borderColor);
        graphics.fill(x + 1, y + height - 2, x + 2, y + height - 1, borderColor);
        graphics.fill(x + width - 2, y + height - 2, x + width - 1, y + height - 1, borderColor);
    }

    /**
     * Calculates the correct background color variant based on focus states.
     *
     * @return The dynamic standard integer ARGB representation hex color.
     */
    private int getBackgroundColor() {
        return this.isFocused() ? style.focusedBackgroundColor : style.normalBackgroundColor;
    }

    /**
     * Resolves the label text format color evaluating active condition parameters and optional validation.
     *
     * @return The active, validated, or standard hex color value.
     */
    private int getTextColor() {
        if (!this.active) {
            return 0xFF777777;
        }

        if (this.validator != null) {
            return this.validator.test(this.text)
                ? 0xFF55FF55
                : 0xFFFF5555;
        }

        return style.normalTextColor;
    }

    /**
     * Handles keyboard events for navigation, deletion, and clipboard shortcuts.
     *
     * @param event KeyEvent object
     */
    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (!this.isFocused()) return false;

        boolean isCtrlDown = (event.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0;
        boolean isShiftDown = (event.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0;

        if (isCtrlDown) {
            switch (event.key()) {
                case GLFW.GLFW_KEY_A:
                    this.cursorPosition = this.text.length();
                    this.selectionEnd = 0;
                    return true;
                case GLFW.GLFW_KEY_C:
                    Minecraft.getInstance().keyboardHandler.setClipboard(getSelectedText());
                    return true;
                case GLFW.GLFW_KEY_X:
                    Minecraft.getInstance().keyboardHandler.setClipboard(getSelectedText());
                    insertText("");
                    return true;
                case GLFW.GLFW_KEY_V:
                    insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                    return true;
            }
        }

        return switch (event.key()) {
            case GLFW.GLFW_KEY_LEFT -> {
                moveCursor(-1, isShiftDown);
                yield true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                moveCursor(1, isShiftDown);
                yield true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                deleteText(-1);
                yield true;
            }
            case GLFW.GLFW_KEY_DELETE -> {
                deleteText(1);
                yield true;
            }

            default -> super.keyPressed(event);
        };
    }

    /**
     * Inserts typed characters into the text field at the current cursor location.
     *
     * @param event The CharacterEvent object
     * @return True if the character was inserted, false otherwise.
     */
    @Override
    public boolean charTyped(@NonNull CharacterEvent event) {
        if (!this.isFocused() || !StringUtil.isAllowedChatCharacter(event.codepoint())) {
            return false;
        }
        insertText(Character.toString(event.codepoint()));
        return true;
    }

    /**
     * Handles mouse clicks to focus the field and position the cursor appropriately.
     *
     * @param event       The encapsulation mouse cursor signal arguments.
     * @param doubleClick True if action was registered sequentially under rapid response time frame.
     */
    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        this.setFocused(true);

        var font = Minecraft.getInstance().font;
        int clickX = (int) event.x() - getX() - 4;
        String visibleText = font.plainSubstrByWidth(this.text.substring(this.displayOffset), width - 8);

        this.cursorPosition = this.displayOffset + font.plainSubstrByWidth(visibleText, clickX).length();
        this.selectionEnd = this.cursorPosition;
    }

    /**
     * Unfocuses the field when clicking outside of it.
     *
     * @param event The encapsulation mouse cursor signal arguments.
     */
    @Override
    public void onRelease(@NonNull MouseButtonEvent event) {}

    /**
     * Retrieves the currently selected portion of the text.
     *
     * @return The substring that is currently highlighted by the user.
     */
    private String getSelectedText() {
        int start = Math.min(this.cursorPosition, this.selectionEnd);
        int end = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(start, end);
    }

    /**
     * Inserts a string at the current cursor position, replacing any selected text.
     *
     * @param incoming The string to inject into the text field.
     */
    private void insertText(String incoming) {
        int start = Math.min(this.cursorPosition, this.selectionEnd);
        int end = Math.max(this.cursorPosition, this.selectionEnd);

        String newText = this.text.substring(0, start) + incoming + this.text.substring(end);

        if (newText.length() <= maxLength) {
            this.text = newText;
            this.cursorPosition = start + incoming.length();
            this.selectionEnd = this.cursorPosition;
            updateDisplayOffset();
            if (this.responder != null) this.responder.accept(this.text);
        }
    }

    /**
     * Removes characters from the text field based on the direction provided.
     *
     * @param direction A negative value for backspace, positive for delete.
     */
    private void deleteText(int direction) {
        if (this.cursorPosition != this.selectionEnd) {
            insertText("");
            return;
        }

        if (direction < 0 && this.cursorPosition > 0) {
            this.text = this.text.substring(0, this.cursorPosition - 1) + this.text.substring(this.cursorPosition);
            this.cursorPosition--;
        } else if (direction > 0 && this.cursorPosition < this.text.length()) {
            this.text = this.text.substring(0, this.cursorPosition) + this.text.substring(this.cursorPosition + 1);
        }
        this.selectionEnd = this.cursorPosition;
        updateDisplayOffset();
        if (this.responder != null) this.responder.accept(this.text);
    }

    /**
     * Moves the typing cursor by a specified amount and handles text selection.
     *
     * @param amount      The number of characters to move the cursor by.
     * @param isShiftDown True if the selection should be expanded during movement.
     */
    private void moveCursor(int amount, boolean isShiftDown) {
        this.cursorPosition = Math.clamp(this.text.length(), 0, this.cursorPosition + amount);
        if (!isShiftDown) {
            this.selectionEnd = this.cursorPosition;
        }
        updateDisplayOffset();
    }

    /**
     * Adjusts the display offset to ensure the cursor always remains visible inside the bounds.
     */
    private void updateDisplayOffset() {
        var font = Minecraft.getInstance().font;
        int maxTextWidth = width - 8;

        if (this.cursorPosition < this.displayOffset) {
            this.displayOffset = this.cursorPosition;
        } else {
            String visiblePart = this.text.substring(this.displayOffset, this.cursorPosition);
            while (font.width(visiblePart) > maxTextWidth) {
                this.displayOffset++;
                visiblePart = this.text.substring(this.displayOffset, this.cursorPosition);
            }
        }
    }

    /**
     * Sets the current text of the field.
     *
     * @param text The text to display.
     */
    public void setText(String text) {
        if (text == null) {
            text = "";
        }

        if (text.length() > this.maxLength) {
            text = text.substring(0, this.maxLength);
        }

        this.text = text;
        this.cursorPosition = text.length();
        this.selectionEnd = this.cursorPosition;
        this.displayOffset = 0;
        updateDisplayOffset();

        if (this.responder != null) {
            this.responder.accept(this.text);
        }
    }

    /**
     * Handles accessibility tools and reader narratives updating screen changes.
     *
     * @param builder The targeted container handling output feedback context structure information.
     */
    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput builder) {
    }

    /**
     * Gets the current text written in the field.
     * * @return The current string value.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Creates a new instance of the modular builder configuration stack helper.
     *
     * @param placeholder The label text component applied when the field is empty.
     * @return An incremental chainable Builder utility object instance.
     */
    public static Builder builder(Component placeholder) {
        return new Builder(placeholder);
    }

    public static class Builder {
        private final Component placeholder;
        private int x, y;
        private int width = 200;
        private int height = 20;
        private final TextFieldStyle style = new TextFieldStyle();
        private Predicate<String> validator = null;
        private Consumer<String> responder = null;

        /**
         * Builder constructor assigning mandatory localized placeholder values.
         *
         * @param placeholder The empty state text target.
         */
        public Builder(Component placeholder) {
            this.placeholder = placeholder;
        }

        public Builder responder(Consumer<String> responder) {
            this.responder = responder;
            return this;
        }

        /**
         * Sets relative screen coordinate assignments inside modern frame logic layouts.
         *
         * @param x Horizontal pixel position coordinate offset.
         * @param y Vertical pixel position coordinate offset.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Configures absolute rectangular dimension specifications framework definitions.
         *
         * @param width  Horizontal pixel boundary layout length metrics target.
         * @param height Vertical frame box thickness dimension length metrics.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Modifies visual attributes dynamically exposing modular background styles variables context hooks.
         *
         * @param styleConsumer The block handler function setting contextual color definitions configuration variables.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder style(Consumer<TextFieldStyle> styleConsumer) {
            styleConsumer.accept(this.style);
            return this;
        }

        /**
         * Assigns a custom validation logic block to process text states dynamically.
         *
         * @param validator A predicate function mapping the string content to a boolean condition.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder validator(Predicate<String> validator) {
            this.validator = validator;
            return this;
        }

        /**
         * Collects structured build configurations pipeline arguments data formatting and return operations object targets.
         *
         * @return A newly initialized CustomTextField matching context options properties data.
         */
        public CustomTextField build() {
            CustomTextField field = new CustomTextField(x, y, width, height, placeholder, style, validator);
            field.setResponder(this.responder);
            return field;
        }
    }

    public void setResponder(Consumer<String> responder) {
        this.responder = responder;
    }

    public static class TextFieldStyle {
        private boolean borderEnabled = true;

        private int normalBackgroundColor = 0x88440000;
        private int focusedBackgroundColor = 0xAA660000;

        private final int normalTextColor = 0xFFFFFFFF;
        private final int placeholderColor = 0xFFAAAAAA;

        private int borderColor = 0xFF880000;
        private int focusedBorderColor = 0xFFFF5555;
        private int disabledBorderColor = 0xFF555555;

        private final int cursorColor = 0xFFFFAAAA;
        private final int selectionColor = 0x55FF5555;

        /**
         * Sets visibility boundaries criteria over borders outline layer components layouts.
         *
         * @param enabled Toggles the borders rendering sequence layer flags visibility status.
         * @return The current configuration instance state reference frame context.
         */
        public TextFieldStyle border(boolean enabled) {
            this.borderEnabled = enabled;
            return this;
        }

        /**
         * Sets standard color profiles arrays matching explicit interaction behaviors state context values.
         *
         * @param normal  Standard idle condition state hex background configuration profile colors context code.
         * @param focused Active visual focus hex colors code background configurations profile context metrics.
         * @return The current configuration instance state reference frame context.
         */
        public TextFieldStyle backgroundColors(int normal, int focused) {
            this.normalBackgroundColor = normal;
            this.focusedBackgroundColor = focused;
            return this;
        }

        /**
         * Sets the border color to be used when the text field is in a disabled state.
         *
         * @param color The border color to apply
         * @return The current {@link TextFieldStyle} instance for chaining
         */
        public TextFieldStyle disabledBorderColor(int color) {
            this.disabledBorderColor = color;
            return this;
        }

        /**
         * Adjusts lines border frame colors settings profiles structures directly.
         *
         * @param normal  Standard bounding frame profile border layout line colors hex value.
         * @param focused Active input component structure border layer display line values colors.
         * @return The current configuration instance state reference frame context.
         */
        public TextFieldStyle borderColors(int normal, int focused) {
            this.borderColor = normal;
            this.focusedBorderColor = focused;
            return this;
        }
    }
}
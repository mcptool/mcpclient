package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class CustomConfirmScreen extends Screen {
    protected final Component message;
    protected final BooleanConsumer callback;
    protected final Component yesButtonComponent;
    protected final Component noButtonComponent;

    private CustomButton yesButton;
    private CustomButton noButton;
    private int delayTicker;

    /**
     * Constructs a new confirmation screen with default Yes/No button labels.
     *
     * @param callback The callback to execute when a decision is made
     * @param title    The title of the screen
     * @param message  The message text to display
     */
    public CustomConfirmScreen(BooleanConsumer callback, Component title, Component message) {
        this(callback, title, message, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
    }

    /**
     * Constructs a new confirmation screen with custom button labels.
     *
     * @param callback           The callback to execute when a decision is made
     * @param title              The title of the screen
     * @param message            The message text to display
     * @param yesButtonComponent The label for the affirmative button
     * @param noButtonComponent  The label for the negative button
     */
    public CustomConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButtonComponent, Component noButtonComponent) {
        super(title);
        this.callback = callback;
        this.message = message;
        this.yesButtonComponent = yesButtonComponent;
        this.noButtonComponent = noButtonComponent;
    }

    /**
     * Initializes the screen elements, including the title, message widget,
     * and confirmation/cancellation buttons.
     */
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        StringWidget titleWidget = new StringWidget(this.title, this.font);
        titleWidget.setPosition(centerX - titleWidget.getWidth() / 2, centerY - 50);
        this.addRenderableWidget(titleWidget);

        MultiLineTextWidget messageWidget = new MultiLineTextWidget(this.message, this.font);
        messageWidget.setMaxWidth(this.width - 50);
        messageWidget.setCentered(true);
        messageWidget.setPosition(centerX - messageWidget.getWidth() / 2, centerY - 20);
        this.addRenderableWidget(messageWidget);

        this.yesButton = CustomButton.builder(this.yesButtonComponent)
            .position(centerX - 105, centerY + 20)
            .size(100, 20)
            .onPress(btn -> this.callback.accept(true))
            .build();
        this.addRenderableWidget(this.yesButton);

        this.noButton = CustomButton.builder(this.noButtonComponent)
            .position(centerX + 5, centerY + 20)
            .size(100, 20)
            .onPress(btn -> this.callback.accept(false))
            .build();
        this.addRenderableWidget(this.noButton);

        if (this.delayTicker > 0) {
            this.setDelay(this.delayTicker);
        }
    }

    /**
     * Sets a delay before the buttons become active.
     *
     * @param delay The number of ticks to wait
     */
    public void setDelay(int delay) {
        this.delayTicker = delay;
        if (this.yesButton != null && this.noButton != null) {
            this.yesButton.active = false;
            this.noButton.active = false;
        }
    }

    /**
     * Updates the delay timer every tick and enables the buttons once the timer expires.
     */
    @Override
    public void tick() {
        super.tick();
        if (this.delayTicker > 0) {
            if (--this.delayTicker == 0) {
                this.yesButton.active = true;
                this.noButton.active = true;
            }
        }
    }

    /**
     * Prevents the screen from closing automatically when the Escape key is pressed.
     *
     * @return False to disable automatic escape closing
     */
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    /**
     * Handles key press events, specifically triggering the cancellation callback
     * when Escape is pressed, provided the delay has expired.
     *
     * @param event The key event
     * @return True if the event was handled, false otherwise
     */
    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (this.delayTicker <= 0 && event.isEscape()) {
            this.callback.accept(false);
            return true;
        }

        return super.keyPressed(event);
    }

    /**
     * Extracts and renders the state for the confirmation screen.
     *
     * @param graphics The graphics extraction utility
     * @param mouseX   The current X position of the mouse
     * @param mouseY   The current Y position of the mouse
     * @param a        The partial tick progress
     */
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
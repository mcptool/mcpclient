package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomTextField;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class CustomDirectJoinServerScreen extends Screen {
    private static final Component ENTER_IP_LABEL = Component.translatable("manageServer.enterIp");
    private static final Component TITLE = Component.translatable("selectServer.direct");

    private final BooleanConsumer callback;
    private final ServerData serverData;
    private final Screen lastScreen;

    private CustomTextField ipEdit;
    private CustomButton selectButton;

    public CustomDirectJoinServerScreen(Screen lastScreen, BooleanConsumer callback, ServerData serverData) {
        super(TITLE);
        this.lastScreen = lastScreen;
        this.callback = callback;
        this.serverData = serverData;
    }

    /**
     * Initializes the screen elements, including the IP input field, selection button,
     * and cancellation button, while setting the initial focus.
     */
    @Override
    protected void init() {
        int centerX = this.width / 2 - 100;

        this.ipEdit = CustomTextField.builder(ENTER_IP_LABEL)
            .position(centerX, 116)
            .size(200, 20)
            .build();
        this.ipEdit.setText(this.minecraft.options.lastMpIp);
        this.addRenderableWidget(this.ipEdit);

        this.selectButton = CustomButton.builder(Component.translatable("selectServer.select"))
            .position(centerX, this.height / 4 + 96 + 12)
            .size(200, 20)
            .onPress(btn -> this.onSelect())
            .build();
        this.addRenderableWidget(this.selectButton);

        this.addRenderableWidget(CustomButton.builder(CommonComponents.GUI_CANCEL)
            .position(centerX, this.height / 4 + 120 + 12)
            .size(200, 20)
            .onPress(btn -> this.onClose())
            .build());

        this.setInitialFocus(this.ipEdit);
        this.updateSelectButtonStatus();
    }

    /**
     * Handles key press events, allowing the confirmation key to trigger the select action.
     *
     * @param event The key event
     * @return True if the event was handled, false otherwise
     */
    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (this.selectButton.active && this.getFocused() == this.ipEdit && event.isConfirmation()) {
            this.onSelect();
            return true;
        }
        return super.keyPressed(event);
    }

    /**
     * Updates the UI state every tick to ensure the select button accurately reflects
     * the validity of the current input.
     */
    @Override
    public void tick() {
        super.tick();
        this.updateSelectButtonStatus();
    }

    /**
     * Updates the enabled state of the select button based on the validity of the IP address.
     */
    private void updateSelectButtonStatus() {
        if (this.selectButton != null && this.ipEdit != null) {
            this.selectButton.active = ServerAddress.isValidAddress(this.ipEdit.getText());
        }
    }

    /**
     * Saves the entered IP to the server data and client options, then executes the callback.
     */
    private void onSelect() {
        this.serverData.ip = this.ipEdit.getText();
        this.minecraft.options.lastMpIp = this.ipEdit.getText();
        this.minecraft.options.save();
        this.callback.accept(true);
    }

    /**
     * Closes the current screen and returns to the previous screen.
     */
    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.lastScreen);
    }

    /**
     * Saves the final state of the IP input to the client options when the screen is removed.
     */
    @Override
    public void removed() {
        this.minecraft.options.lastMpIp = this.ipEdit.getText();
        this.minecraft.options.save();
    }

    /**
     * Extracts and renders the UI labels for the screen components.
     *
     * @param graphics The graphics extraction utility
     * @param mouseX   The current X position of the mouse
     * @param mouseY   The current Y position of the mouse
     * @param a        The partial tick progress
     */
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        graphics.text(this.font, ENTER_IP_LABEL, this.width / 2 - 100 + 1, 100, -6250336);
    }
}
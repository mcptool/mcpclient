package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomTextField;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData.ServerPackStatus;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.CommonComponents;
import org.jspecify.annotations.NonNull;

public class CustomManageServerScreen extends Screen {
    private final Screen lastScreen;
    private final BooleanConsumer callback;
    private final ServerData serverData;

    private CustomTextField nameEdit;
    private CustomTextField ipEdit;
    private CustomButton doneButton;

    private static final Component NAME_LABEL = Component.translatable("manageServer.enterName");
    private static final Component IP_LABEL = Component.translatable("manageServer.enterIp");
    private static final Component RESOURCE_PACK_LABEL = Component.translatable("manageServer.resourcePack");

    public CustomManageServerScreen(Screen lastScreen, Component title, BooleanConsumer callback, ServerData serverData) {
        super(title);
        this.lastScreen = lastScreen;
        this.callback = callback;
        this.serverData = serverData;
    }

    /**
     * Initializes the screen elements, including text fields for server details,
     * the resource pack status toggle button, and action buttons.
     */
    @Override
    protected void init() {
        int centerX = this.width / 2 - 100;
        int startY = 66;

        this.nameEdit = CustomTextField.builder(NAME_LABEL)
            .position(centerX, startY)
            .size(200, 20)
            .build();
        this.nameEdit.setText(this.serverData.name);
        this.addRenderableWidget(this.nameEdit);

        this.ipEdit = CustomTextField.builder(IP_LABEL)
            .position(centerX, startY + 40)
            .size(200, 20)
            .build();
        this.ipEdit.setText(this.serverData.ip);
        this.addRenderableWidget(this.ipEdit);

        CustomButton resourcePackButton = CustomButton.builder(this.serverData.getResourcePackStatus().getName())
            .position(centerX, startY + 80)
            .size(200, 20)
            .onPress(btn -> {
                ServerPackStatus[] values = ServerPackStatus.values();
                int nextIndex = (this.serverData.getResourcePackStatus().ordinal() + 1) % values.length;
                ServerPackStatus next = values[nextIndex];
                this.serverData.setResourcePackStatus(next);
                btn.setMessage(next.getName());
            })
            .build();
        this.addRenderableWidget(resourcePackButton);

        this.doneButton = CustomButton.builder(Component.literal("Done"))
            .position(centerX, this.height / 4 + 96 + 18)
            .size(200, 20)
            .onPress(btn -> onSave())
            .build();
        this.addRenderableWidget(this.doneButton);

        this.addRenderableWidget(CustomButton.builder(CommonComponents.GUI_CANCEL)
            .position(centerX, this.height / 4 + 120 + 18)
            .size(200, 20)
            .onPress(btn -> this.onClose())
            .build());
    }

    /**
     * Updates the screen state every tick, validating input fields to enable
     * or disable the "Done" button accordingly.
     */
    @Override
    public void tick() {
        super.tick();
        boolean isNameValid = !this.nameEdit.getText().trim().isEmpty();
        boolean isIpValid = !this.ipEdit.getText().trim().isEmpty();
        this.doneButton.active = isNameValid && isIpValid;
    }

    /**
     * Saves the current text field inputs into the server data object and triggers
     * the callback to indicate a successful save operation.
     */
    private void onSave() {
        this.serverData.name = this.nameEdit.getText();
        this.serverData.ip = this.ipEdit.getText();
        this.callback.accept(true);
    }

    /**
     * Handles the closing of the screen, returning the user to the previous screen.
     */
    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.lastScreen);
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

        graphics.centeredText(this.font, this.title, this.width / 2, 17, 0xFFFFFF);
        int centerX = this.width / 2 - 100;
        graphics.text(this.font, NAME_LABEL, centerX, 53, -6250336);
        graphics.text(this.font, IP_LABEL, centerX, 93, -6250336);
        graphics.text(this.font, RESOURCE_PACK_LABEL, centerX, 133, -6250336);
    }
}
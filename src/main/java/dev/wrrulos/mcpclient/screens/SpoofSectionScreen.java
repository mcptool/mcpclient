package dev.wrrulos.mcpclient.screens;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.session.SessionController;
import dev.wrrulos.mcpclient.utils.SpoofScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class SpoofSectionScreen extends Screen {
    SessionController sessionController;

    // Username variables
    private TextWidget usernameTextWidget;
    private TextFieldWidget usernameTextFieldWidget;

    // UUID variables
    private TextWidget uuidTextWidget;
    private TextFieldWidget uuidTextFieldWidget;
    private ButtonWidget uuidStatusButtonWidget;

    // FakeIP variables
    private TextWidget fakeIPTextWidget;
    private TextFieldWidget fakeIPTextFieldWidget;
    private ButtonWidget fakeIPStatusButtonWidget;

    // FakeHostname variables
    private TextWidget fakeHostnameTextWidget;
    private TextFieldWidget fakeHostnameTextFieldWidget;
    private ButtonWidget fakeHostnameStatusButtonWidget;

    // Save and back buttons
    private ButtonWidget backButtonWidget;
    private ButtonWidget saveButtonWidget;

    // Spoof utils
    private SpoofScreenUtil spoofScreenUtil = new SpoofScreenUtil();

    // Parent
    private final Screen parent;

    /**
     * Constructor for the SpoofSectionScreen
     * @param parent Parent screen
     */
    public SpoofSectionScreen(Screen parent) {
        super(Text.literal("Spoof section"));
        this.parent = parent;
        this.sessionController = Mcpclient.getSessionController();
    }

    @Override
    protected void init() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // Username text widget
        this.usernameTextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 110,
            200,
            20,
            Text.literal(""),
            textRenderer
        );
        this.addDrawableChild(this.usernameTextWidget);

        // Username text field widget
        this.usernameTextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2 - 90,
            200,
            20,
            Text.literal("Username")
        );
        this.usernameTextFieldWidget.setChangedListener(this::onUsernameTextChanged);
        this.usernameTextFieldWidget.setMaxLength(16);
        this.usernameTextFieldWidget.setText(sessionController.getUsername());
        this.addDrawableChild(this.usernameTextFieldWidget);

        // UUID text widget
        this.uuidTextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 65,
            200,
            20,
            Text.literal(""),
            textRenderer
        );
        this.addDrawableChild(this.uuidTextWidget);

        // UUID text field widget
        this.uuidTextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2 - 45,
            200,
            20,
            Text.literal("UUID")
        );
        this.uuidTextFieldWidget.setChangedListener(this::onUUIDTextChanged);
        this.uuidTextFieldWidget.setMaxLength(36);
        this.uuidTextFieldWidget.setText(sessionController.getCurrentUUID());
        this.addDrawableChild(this.uuidTextFieldWidget);

        // UUID status button
        this.uuidStatusButtonWidget = ButtonWidget.builder(
            this.getUUIDStatusText(),
            this::onUUIDStatusButtonPressed
        ).width(50).position(this.width / 2 - 155, this.height / 2 - 45).build();
        this.addDrawableChild(this.uuidStatusButtonWidget);

        // FakeIP text widget
        this.fakeIPTextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 20,
            200,
            20,
            Text.literal(""),
            textRenderer
        );
        this.addDrawableChild(this.fakeIPTextWidget);

        // FakeIP text field widget
        this.fakeIPTextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2,
            200,
            20,
            Text.literal("FakeIP")
        );
        this.fakeIPTextFieldWidget.setChangedListener(this::onFakeIPTextChanged);
        this.fakeIPTextFieldWidget.setMaxLength(16);
        this.fakeIPTextFieldWidget.setText(sessionController.getCurrentFakeIP());
        this.addDrawableChild(this.fakeIPTextFieldWidget);

        // FakeIP Button widget
        this.fakeIPStatusButtonWidget = ButtonWidget.builder(
            this.getFakeIPStatusText(),
            this::onFakeIPStatusButtonPressed
        ).width(50).position(this.width / 2 - 155, this.height / 2).build();
        this.addDrawableChild(this.fakeIPStatusButtonWidget);

        // FakeHostname text widget
        this.fakeHostnameTextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 + 25,
            200,
            20,
            Text.literal(""),
            textRenderer
        );
        this.addDrawableChild(this.fakeHostnameTextWidget);

        // FakeHostname text field widget
        this.fakeHostnameTextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2 + 45,
            200,
            20,
            Text.literal("FakeHostname")
        );
        this.fakeHostnameTextFieldWidget.setChangedListener(this::onFakeHostnameTextChanged);
        this.fakeHostnameTextFieldWidget.setMaxLength(16);
        this.fakeHostnameTextFieldWidget.setText(sessionController.getCurrentFakeHostname());
        this.addDrawableChild(this.fakeHostnameTextFieldWidget);

        // FakeHostname button widget
        this.fakeHostnameStatusButtonWidget = ButtonWidget.builder(
            this.getFakeHostnameStatusText(),
            this::onFakeHostnameStatusButtonPressed
        ).width(50).position(this.width / 2 - 155, this.height / 2 + 45).build();
        this.addDrawableChild(this.fakeHostnameStatusButtonWidget);

        // Back button
        this.backButtonWidget = ButtonWidget.builder(
            Text.literal("Back"),
            this::onBackButtonPressed
        ).width(200).position(this.width / 2 - 100, this.height / 2 + 75).build();
        this.addDrawableChild(this.backButtonWidget);

        // Save button
        this.saveButtonWidget = ButtonWidget.builder(
            Text.literal("Save"),
            this::onSaveButtonPressed
        ).width(200).position(this.width / 2 - 100, this.height / 2 + 100).build();
        this.addDrawableChild(this.saveButtonWidget);

        // Add messages to widgets
        this.usernameTextWidget.setMessage(this.getUsernameText());
        this.uuidTextWidget.setMessage(this.getUUIDText());
        this.fakeIPTextWidget.setMessage(this.getFakeIPText());
        this.fakeHostnameTextWidget.setMessage(this.getFakeHostnameText());
    }

    /**
     * Set the username text widget message
     * @param username Username
     */
    private void onUsernameTextChanged(String username) {
        this.usernameTextWidget.setMessage(getUsernameText());
    }

    /**
     * Set the UUID text widget message
     * @param username Username
     */
    private void onUUIDTextChanged(String username) {
        this.uuidTextWidget.setMessage(getUUIDText());
    }

    /**
     * Set the FakeIP text widget message
     * @param username Username
     */
    private void onFakeIPTextChanged(String username) {
        this.fakeIPTextWidget.setMessage(getFakeIPText());
    }

    /**
     * Set the FakeHostname text widget message
     * @param username Username
     */
    private void onFakeHostnameTextChanged(String username) {
        this.fakeHostnameTextWidget.setMessage(getFakeHostnameText());
    }

    /**
     * Set the UUID status button message and toggle the UUID spoof status
     * @param buttonWidget Button widget
     */
    private void onUUIDStatusButtonPressed(ButtonWidget buttonWidget) {
        this.sessionController.setUUIDSpoofStatus(!this.sessionController.getUUIDSpoofStatus());
        buttonWidget.setMessage(this.getUUIDStatusText());
        this.uuidTextWidget.setMessage(this.getUUIDText());
        this.uuidTextFieldWidget.active = this.sessionController.getUUIDSpoofStatus();
    }

    /**
     * Set the FakeIP status button message and toggle the FakeIP spoof status
     * @param buttonWidget Button widget
     */
    private void onFakeIPStatusButtonPressed(ButtonWidget buttonWidget) {
        this.sessionController.setFakeIPStatus(!this.sessionController.getFakeIPStatus());
        buttonWidget.setMessage(this.getFakeIPStatusText());
        this.fakeIPTextWidget.setMessage(this.getFakeIPText());
        this.fakeIPTextFieldWidget.active = this.sessionController.getFakeIPStatus();
    }

    /**
     * Set the FakeHostname status button message and toggle the FakeHostname spoof status
     * @param buttonWidget Button widget
     */
    private void onFakeHostnameStatusButtonPressed(ButtonWidget buttonWidget) {
        this.sessionController.setFakeHostnameStatus(!this.sessionController.getFakeHostnameStatus());
        buttonWidget.setMessage(this.getFakeHostnameStatusText());
        this.fakeHostnameTextWidget.setMessage(this.getFakeHostnameText());
        this.fakeHostnameTextFieldWidget.active = this.sessionController.getFakeHostnameStatus();
    }

    /**
     * Go back to the parent screen
     * @param buttonWidget Button widget
     */
    private void onBackButtonPressed(ButtonWidget buttonWidget) {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    /**
     * Save the spoofed data and go back to the parent screen
     * @param buttonWidget Button widget
     */
    private void onSaveButtonPressed(ButtonWidget buttonWidget) {
        //
        // The username is not checked since on vanilla servers you can have very strange names.
        //
        if (this.sessionController.getUUIDSpoofStatus() && this.sessionController.invalidUUID) {
            return;
        }

        if (this.sessionController.getFakeIPStatus() && this.sessionController.invalidFakeIP) {
            return;
        }

        if (this.sessionController.getFakeHostnameStatus() && this.sessionController.invalidFakeHostname) {
            return;
        }

        sessionController.setUsername(this.usernameTextFieldWidget.getText());
        sessionController.setUUID(this.uuidTextFieldWidget.getText());
        sessionController.setCurrentFakeIP(this.fakeIPTextFieldWidget.getText());
        sessionController.setCurrentFakeHostname(this.fakeHostnameTextFieldWidget.getText());
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    /**
     * Get the username text
     * @return Text object with the username text
     */
    private Text getUsernameText() {
        return Text.literal("Username").setStyle(spoofScreenUtil.getUsernameTextColor(this.usernameTextFieldWidget.getText()));
    }

    /**
     * Get the UUID text
     * @return Text object with the UUID text
     */
    private Text getUUIDText() {
        return Text.literal("UUID (Bungee)").setStyle(spoofScreenUtil.getUUIDTextColor(this.uuidTextFieldWidget.getText()));
    }

    /**
     * Get the FakeIP text
     * @return Text object with the FakeIP text
     */
    private Text getFakeIPText() {
        return Text.literal("FakeIP (Bungee)").setStyle(spoofScreenUtil.getFakeIPTextColor(this.fakeIPTextFieldWidget.getText()));
    }

    /**
     * Get the FakeHostname text
     * @return Text object with the FakeHostname text
     */
    private Text getFakeHostnameText() {
        return Text.literal("FakeHostname").setStyle(spoofScreenUtil.getFakeHostnameTextColor(this.fakeHostnameTextFieldWidget.getText()));
    }

    /**
     * Get the UUID status text
     * @return Text object with the UUID status text
     */
    private Text getUUIDStatusText() {
        if (sessionController.getUUIDSpoofStatus()) {
            return Text.literal("ON").setStyle(spoofScreenUtil.getStatusTextColor(true));
        }

        return Text.literal("OFF").setStyle(spoofScreenUtil.getStatusTextColor(false));
    }

    /**
     * Get the FakeIP status text
     * @return Text object with the FakeIP status text
     */
    private Text getFakeIPStatusText() {
        if (sessionController.getFakeIPStatus()) {
            return Text.literal("ON").setStyle(spoofScreenUtil.getStatusTextColor(true));
        }

        return Text.literal("OFF").setStyle(spoofScreenUtil.getStatusTextColor(false));
    }

    /**
     * Get the FakeHostname status text
     * @return Text object with the FakeHostname status text
     */
    private Text getFakeHostnameStatusText() {
        if (sessionController.getFakeHostnameStatus()) {
            return Text.literal("ON").setStyle(spoofScreenUtil.getStatusTextColor(true));
        }

        return Text.literal("OFF").setStyle(spoofScreenUtil.getStatusTextColor(false));
    }
}

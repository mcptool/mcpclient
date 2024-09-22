package dev.wrrulos.mcpclient.screens;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.keybinds.KeyBindManager;
import dev.wrrulos.mcpclient.session.SessionController;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import dev.wrrulos.mcpclient.utils.MCPToolSettingsScreenUtil;
import dev.wrrulos.mcpclient.utils.SpoofScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class MCPToolSettingsSectionScreen extends Screen {
    MCPToolSettings settings;

    // AutoCommand's variables
    private TextWidget autoCommand1TextWidget;
    private TextFieldWidget autoCommand1TextFieldWidget;

    private TextWidget autoCommand2TextWidget;
    private TextFieldWidget autoCommand2TextFieldWidget;

    private TextWidget autoCommand3TextWidget;
    private TextFieldWidget autoCommand3TextFieldWidget;

    // Stealth mode variables
    private ButtonWidget stealhModeStatusButtonWidget;

    // Back and save buttons
    private ButtonWidget backButtonWidget;
    private ButtonWidget saveButtonWidget;

    // Settings util
    private MCPToolSettingsScreenUtil mcpToolSettingsScreenUtil = new MCPToolSettingsScreenUtil();
    private KeyBindManager keyBindManager = Mcpclient.getKeyBindManager();

    // Parent
    private final Screen parent;

    /**
     * Constructor for the SpoofSectionScreen
     * @param parent Parent screen
     */
    public MCPToolSettingsSectionScreen(Screen parent) {
        super(Text.literal("Spoof section"));
        this.parent = parent;
        this.settings = Mcpclient.getSettings();
    }

    @Override
    protected void init() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // AutoCommand 1 text widget
        this.autoCommand1TextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 110,
            200,
            20,
            Text.literal(""),
            textRenderer
        );
        this.addDrawableChild(this.autoCommand1TextWidget);

        // AutoCommand 1 text field widget
        this.autoCommand1TextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2 - 90,
            200,
            20,
            Text.literal("Auto Command 1")
        );
        this.autoCommand1TextFieldWidget.setChangedListener(this::onAutoCommand1TextChanged);
        this.autoCommand1TextFieldWidget.setMaxLength(100);
        this.autoCommand1TextFieldWidget.setText(this.settings.getAutoCommand1());
        this.addDrawableChild(this.autoCommand1TextFieldWidget);

        // AutoCommand 2 text widget
        this.autoCommand2TextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 65,
            200,
            20,
            Text.literal("Auto Command 2"),
            textRenderer
        );
        this.addDrawableChild(this.autoCommand2TextWidget);

        // AutoCommand 2 text field widget
        this.autoCommand2TextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2 - 45,
            200,
            20,
            Text.literal("Auto Command 2")
        );
        this.autoCommand2TextFieldWidget.setChangedListener(this::onAutoCommand2TextChanged);
        this.autoCommand2TextFieldWidget.setMaxLength(100);
        this.autoCommand2TextFieldWidget.setText(this.settings.getAutoCommand2());
        this.addDrawableChild(this.autoCommand2TextFieldWidget);

        // AutoCommand 3 text widget
        this.autoCommand3TextWidget = new TextWidget(
            this.width / 2 - 100,
            this.height / 2 - 20,
            200,
            20,
            Text.literal("Auto Command 3"),
            textRenderer
        );
        this.addDrawableChild(this.autoCommand3TextWidget);

        // AutoCommand 3 text field widget
        this.autoCommand3TextFieldWidget = new TextFieldWidget(
            textRenderer,
            this.width / 2 - 100,
            this.height / 2,
            200,
            20,
            Text.literal("Auto Command 3")
        );
        this.autoCommand2TextFieldWidget.setChangedListener(this::onAutoCommand3TextChanged);
        this.autoCommand3TextFieldWidget.setMaxLength(100);
        this.autoCommand3TextFieldWidget.setText(this.settings.getAutoCommand3());
        this.addDrawableChild(this.autoCommand3TextFieldWidget);

        // Stealth mode status button
        this.stealhModeStatusButtonWidget = ButtonWidget.builder(
            this.getStealthModeStatusButtonText(),
            this::onStealthModeButtonPressed
        ).width(200).position(this.width / 2 - 100, this.height / 2 + 30).build();
        this.addDrawableChild(this.stealhModeStatusButtonWidget);

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
    }

    /**
     * Set the text of the auto command 1 text widget
     * @param username Username
     */
    private void onAutoCommand1TextChanged(String username) {
        this.autoCommand1TextWidget.setMessage(this.getAutoCommand1Text());
    }

    /**
     * Set the text of the auto command 2 text widget
     * @param username Username
     */
    private void onAutoCommand2TextChanged(String username) {
        this.autoCommand2TextWidget.setMessage(this.getAutoCommand2Text());
    }

    /**
     * Set the text of the auto command 3 text widget
     * @param username Username
     */
    private void onAutoCommand3TextChanged(String username) {
        this.autoCommand3TextWidget.setMessage(this.getAutoCommand3Text());
    }

    /**
     * When the stealth mode button is pressed
     * @param buttonWidget Button widget
     */
    private void onStealthModeButtonPressed(ButtonWidget buttonWidget) {
        this.settings.setStealthMode(!this.settings.isStealthMode());
        this.stealhModeStatusButtonWidget.setMessage(this.getStealthModeStatusButtonText());
    }

    /**
     * Get the auto command 1 text
     * @return Text object with the username text
     */
    private Text getAutoCommand1Text() {
        return Text.literal("Auto Command 1").setStyle(mcpToolSettingsScreenUtil.getAutoCommandTextColor(this.autoCommand1TextFieldWidget.getText()));
    }

    /**
     * Get the auto command 2 text
     * @return Text object with the username text
     */
    private Text getAutoCommand2Text() {
        return Text.literal("Auto Command 2").setStyle(mcpToolSettingsScreenUtil.getAutoCommandTextColor(this.autoCommand2TextFieldWidget.getText()));
    }

    /**
     * Get the auto command 3 text
     * @return Text object with the username text
     */
    private Text getAutoCommand3Text() {
        return Text.literal("Auto Command 3").setStyle(mcpToolSettingsScreenUtil.getAutoCommandTextColor(this.autoCommand3TextFieldWidget.getText()));
    }

    /**
     * Get the FakeHostname status text
     * @return Text object with the FakeHostname status text
     */
    private Text getStealthModeStatusButtonText() {
        if (this.settings.isStealthMode()) {
            return Text.literal("Stealth Mode").setStyle(mcpToolSettingsScreenUtil.getStatusTextColor(true));
        }

        return Text.literal("Stealth Mode").setStyle(mcpToolSettingsScreenUtil.getStatusTextColor(false));
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
        this.settings.setAutoCommand1(this.autoCommand1TextFieldWidget.getText());
        this.settings.setAutoCommand2(this.autoCommand2TextFieldWidget.getText());
        this.settings.setAutoCommand3(this.autoCommand3TextFieldWidget.getText());
        this.settings.setStealthMode(this.settings.isStealthMode());
        this.settings.save();
        this.keyBindManager.setKeyBindCommands();
        MinecraftClient.getInstance().setScreen(this.parent);
    }
}

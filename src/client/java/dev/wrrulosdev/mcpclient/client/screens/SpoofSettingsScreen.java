package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomTextField;
import dev.wrrulosdev.mcpclient.client.settings.SpoofingSettings;
import dev.wrrulosdev.mcpclient.client.utilities.validators.IPValidators;
import dev.wrrulosdev.mcpclient.client.utilities.validators.UuidValidators;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class SpoofSettingsScreen extends Screen {

    private final Screen previousScreen;

    private CustomTextField usernameField;

    private CustomTextField uuidField;
    private CustomButton uuidToggleButton;

    private CustomTextField ipField;
    private CustomButton ipToggleButton;

    private CustomTextField hostnameField;
    private CustomButton hostnameToggleButton;

    private CustomButton toggleSpoofButton;
    private CustomButton backButton;

    private final String spoofEnabledText = "Spoof: Enabled";
    private final String spoofDisabledText = "Spoof: Disabled";
    private final String buttonEnabledText = "ON";
    private final String buttonDisabledText = "OFF";
    private final SpoofingSettings spoofingSettings;

    private boolean spoofEnabled;
    private boolean uuidSpoofEnabled;
    private boolean ipSpoofEnabled;
    private boolean hostnameSpoofEnabled;

    /**
     * Initializes a new instance of the spoof settings screen structure.
     *
     * @param previousScreen The parental screen instance to return to upon exiting.
     */
    public SpoofSettingsScreen(Screen previousScreen) {
        super(Component.literal("Spoof Settings"));
        this.previousScreen = previousScreen;
        this.spoofingSettings = MCPClient.getSettingsManager().getSpoofingSettings();
    }

    /**
     * Bootstraps the visual widget elements, assigning dimensions and responsive coordinates parameters.
     */
    @Override
    protected void init() {
        int centerX = this.width / 2 - 100;
        int startY = this.height / 2 - 110;
        int totalWidth = 200;
        int fieldHeight = 20;
        int toggleWidth = 35;
        int gap = 5;
        int subFieldWidth = totalWidth - toggleWidth - gap;
        this.spoofEnabled = this.spoofingSettings.isSpoofingEnabled();
        this.uuidSpoofEnabled = this.spoofingSettings.isUuidSpoofingEnabled();
        this.ipSpoofEnabled = this.spoofingSettings.isIpSpoofingEnabled();
        this.hostnameSpoofEnabled = this.spoofingSettings.isHostnameSpoofingEnabled();
        String currentUsername = MCPClient.getSpoofingManager().getUsername();

        this.usernameField = CustomTextField.builder(Component.literal(currentUsername))
            .position(centerX, startY + 22)
            .size(totalWidth, fieldHeight)
            .responder((username) -> {
                MCPClient.getSpoofingManager().setUsername(username);
            })
            .build();
        this.usernameField.setText(currentUsername);
        this.addRenderableWidget(this.usernameField);

        this.uuidField = CustomTextField.builder(Component.literal(this.spoofingSettings.getSpoofedUuid()))
            .position(centerX, startY + 58)
            .size(subFieldWidth, fieldHeight)
            .validator(UuidValidators::isValidMinecraftUuid)
            .responder((uuid) -> {
                if (!UuidValidators.isValidMinecraftUuid(uuid)) return;
                this.spoofingSettings.setSpoofedUuid(uuid);
            })
            .build();
        this.uuidField.setText(this.spoofingSettings.getSpoofedUuid());
        this.addRenderableWidget(this.uuidField);

        this.uuidToggleButton = CustomButton.builder(Component.literal(this.getToggleButtonText(this.uuidSpoofEnabled)))
            .position(centerX + subFieldWidth + gap, startY + 58)
            .size(toggleWidth, fieldHeight)
            .onPress(button -> {
                this.uuidSpoofEnabled = !this.uuidSpoofEnabled;
                this.spoofingSettings.setUuidSpoofingEnabled(this.uuidSpoofEnabled);
                button.setMessage(Component.literal(this.getToggleButtonText(this.uuidSpoofEnabled)));
                this.updateVisibility();
            })
            .build();
        this.addRenderableWidget(this.uuidToggleButton);

        this.ipField = CustomTextField.builder(Component.literal(this.spoofingSettings.getSpoofedIp()))
            .position(centerX, startY + 94)
            .size(subFieldWidth, fieldHeight)
            .validator(IPValidators::isValidIp)
            .responder((ip) -> {
                if (!IPValidators.isValidIp(ip)) return;
                this.spoofingSettings.setSpoofedIp(ip);
            })
            .build();
        this.ipField.setText(this.spoofingSettings.getSpoofedIp());
        this.addRenderableWidget(this.ipField);

        this.ipToggleButton = CustomButton.builder(Component.literal(this.getToggleButtonText(this.ipSpoofEnabled)))
            .position(centerX + subFieldWidth + gap, startY + 94)
            .size(toggleWidth, fieldHeight)
            .onPress(button -> {
                this.ipSpoofEnabled = !this.ipSpoofEnabled;
                this.spoofingSettings.setIpSpoofingEnabled(this.ipSpoofEnabled);
                button.setMessage(Component.literal(this.getToggleButtonText(this.ipSpoofEnabled)));
                this.updateVisibility();
            })
            .build();
        this.addRenderableWidget(this.ipToggleButton);

        this.hostnameField = CustomTextField.builder(Component.literal(this.spoofingSettings.getHostnameSpoofed()))
            .position(centerX, startY + 130)
            .size(subFieldWidth, fieldHeight)
            .responder(this.spoofingSettings::setHostnameSpoofed)
            .build();
        this.hostnameField.setText(this.spoofingSettings.getHostnameSpoofed());
        this.addRenderableWidget(this.hostnameField);

        this.hostnameToggleButton = CustomButton.builder(Component.literal(this.getToggleButtonText(this.hostnameSpoofEnabled)))
            .position(centerX + subFieldWidth + gap, startY + 130)
            .size(toggleWidth, fieldHeight)
            .onPress(button -> {
                this.hostnameSpoofEnabled = !this.hostnameSpoofEnabled;
                this.spoofingSettings.setHostnameSpoofingEnabled(this.hostnameSpoofEnabled);
                button.setMessage(Component.literal(this.getToggleButtonText(this.hostnameSpoofEnabled)));
                this.updateVisibility();
            })
            .build();
        this.addRenderableWidget(this.hostnameToggleButton);

        this.toggleSpoofButton = CustomButton.builder(Component.literal(spoofEnabled ? spoofEnabledText : spoofDisabledText))
            .position(centerX, startY + 160)
            .size(totalWidth, fieldHeight)
            .onPress(button -> {
                this.spoofEnabled = !this.spoofEnabled;
                this.spoofingSettings.setSpoofingEnabled(this.spoofEnabled);

                if (!this.spoofEnabled) {
                    MCPClient.getSpoofingManager().setOriginalUsername();
                }

                this.updateVisibility();
            })
            .build();
        this.addRenderableWidget(this.toggleSpoofButton);

        // Back Button
        this.backButton = CustomButton.builder(Component.literal("Back to Menu"))
            .position(centerX, startY + 185)
            .size(totalWidth, fieldHeight)
            .onPress(button -> {
                this.minecraft.gui.setScreen(this.previousScreen);
            })
            .build();
        this.addRenderableWidget(this.backButton);

        this.updateVisibility();
    }

    /**
     * Updates the visibility and interaction states of optional fields based on the spoof toggle configuration.
     */
    private void updateVisibility() {
        this.usernameField.active = this.spoofEnabled;
        this.uuidField.active = this.spoofEnabled && this.uuidSpoofEnabled;
        this.ipField.active = this.spoofEnabled && this.ipSpoofEnabled;
        this.hostnameField.active = this.spoofEnabled && this.hostnameSpoofEnabled;
        this.uuidToggleButton.active = this.spoofEnabled;
        this.ipToggleButton.active = this.spoofEnabled;
        this.hostnameToggleButton.active = this.spoofEnabled;

        Component newText = Component.literal(this.spoofEnabled ? spoofEnabledText : spoofDisabledText);
        this.toggleSpoofButton.setMessage(newText);
    }

    private String getToggleButtonText(boolean state) {
        return state ? this.buttonEnabledText : this.buttonDisabledText;
    }

    /**
     * Processes layout rendering extraction pipelines drawing background gradients and text label matrix arrays.
     *
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param mouseX   The current X coordinate of the cursor.
     * @param mouseY   The current Y coordinate of the cursor.
     * @param a        The partial ticks time delta elapsed.
     */
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        int centerX = this.width / 2 - 100;
        int startY = this.height / 2 - 110;
        int textColor = 0xFFFFFFFF;

        graphics.text(this.font, Component.literal("Your Nickname"), centerX, startY + 10, textColor, true);
        graphics.text(this.font, Component.literal("Custom UUID"), centerX, startY + 46, textColor, true);
        graphics.text(this.font, Component.literal("IP Spoof"), centerX, startY + 82, textColor, true);
        graphics.text(this.font, Component.literal("Hostname spoof"), centerX, startY + 118, textColor, true);
    }
}
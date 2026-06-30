package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class ClientHud extends OptionsBase {

    public static ClientHud INSTANCE;

    /**
     * Initializes the static singleton instance of the Client Hud option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new ClientHud(clientSettings);
    }

    /**
     * Constructs a new ClientHud instance with the provided client settings.
     *
     * @param clientSettings The global client settings object
     */
    private ClientHud(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Retrieves the display name of the Client Hud option.
     *
     * @return The option name string
     */
    @Override
    public String getName() {
        return "Client Hud";
    }

    /**
     * Retrieves a short description of the Client Hud functionality.
     *
     * @return The short description string
     */
    @Override
    public String getShortDescription() {
        return "Enable or disable the HUD information box";
    }

    /**
     * Retrieves a detailed description of the Client Hud functionality.
     *
     * @return The long description string
     */
    @Override
    public String getLongDescription() {
        return "Enable or disable the HUD information box.";
    }

    /**
     * Checks whether the Client Hud option is currently enabled.
     *
     * @return True if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isClientHudEnabled();
    }

    /**
     * Updates the enabled state of the Client Hud option.
     *
     * @param enabled The new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setClientHudEnabled(enabled);
    }
}
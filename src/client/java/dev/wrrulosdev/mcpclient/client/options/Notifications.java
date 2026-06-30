package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class Notifications extends OptionsBase {

    public static Notifications INSTANCE;

    /**
     * Initializes the static singleton instance of the Notifications option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new Notifications(clientSettings);
    }

    private Notifications(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Retrieves the display name of the Notifications option.
     *
     * @return The option name string
     */
    @Override
    public String getName() {
        return "Notifications";
    }

    /**
     * Retrieves a short description of the Notifications functionality.
     *
     * @return The short description string
     */
    @Override
    public String getShortDescription() {
        return "Turn notifications on or off";
    }

    /**
     * Retrieves a detailed description of the Notifications functionality.
     *
     * @return The long description string
     */
    @Override
    public String getLongDescription() {
        return "Enable or disable MCPClient notifications";
    }

    /**
     * Checks whether the Notifications option is currently enabled.
     *
     * @return True if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isNotificationsEnabled();
    }

    /**
     * Updates the enabled state of the Notifications option.
     *
     * @param enabled The new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setNotificationsEnabled(enabled);
    }
}
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

    @Override
    public String getName() {
        return "Notifications";
    }

    @Override
    public String getShortDescription() {
        return "Turn notifications on or off";
    }

    @Override
    public String getLongDescription() {
        return "Enable or disable MCPClient notifications";
    }

    @Override
    public boolean isEnabled() {
        return clientSettings.isNotificationsEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setNotificationsEnabled(enabled);
    }
}
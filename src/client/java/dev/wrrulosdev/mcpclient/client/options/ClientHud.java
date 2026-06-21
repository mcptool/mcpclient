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

    private ClientHud(ClientSettings clientSettings) {
        super(clientSettings);
    }

    @Override
    public String getName() {
        return "ClientHud";
    }

    @Override
    public String getShortDescription() {
        return "Enable or disable the HUD information box";
    }

    @Override
    public String getLongDescription() {
        return "Enable or disable the HUD information box.";
    }

    @Override
    public boolean isEnabled() {
        return clientSettings.isClientHudEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setClientHudEnabled(enabled);
    }
}
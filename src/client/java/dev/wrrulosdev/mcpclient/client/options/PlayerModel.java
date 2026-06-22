package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class PlayerModel extends OptionsBase {

    public static PlayerModel INSTANCE;

    /**
     * Initializes the static singleton instance of the Player Model option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new PlayerModel(clientSettings);
    }

    private PlayerModel(ClientSettings clientSettings) {
        super(clientSettings);
    }

    @Override
    public String getName() {
        return "Player Model";
    }

    @Override
    public String getShortDescription() {
        return "Enable or disable the player model";
    }

    @Override
    public String getLongDescription() {
        return "Enable or disable the player model.";
    }

    @Override
    public boolean isEnabled() {
        return clientSettings.isPlayerModelEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setPlayerModelEnabled(enabled);
    }
}
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

    /**
     * Retrieves the display name of the player model option.
     *
     * @return The option name string
     */
    @Override
    public String getName() {
        return "Player Model";
    }

    /**
     * Retrieves a short description of the option's functionality.
     *
     * @return The short description string
     */
    @Override
    public String getShortDescription() {
        return "Enable or disable the player model";
    }

    /**
     * Retrieves a detailed description of the option's functionality.
     *
     * @return The long description string
     */
    @Override
    public String getLongDescription() {
        return "Enable or disable the player model.";
    }

    /**
     * Checks whether the player model option is currently enabled.
     *
     * @return True if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isPlayerModelEnabled();
    }

    /**
     * Updates the enabled state of the player model option.
     *
     * @param enabled The new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setPlayerModelEnabled(enabled);
    }
}
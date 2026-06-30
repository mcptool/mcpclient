package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class NameTag extends OptionsBase {

    public static NameTag INSTANCE;

    /**
     * Initializes the static singleton instance of the NameTag option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new NameTag(clientSettings);
    }

    /**
     * Constructs a new NameTag instance with the provided client settings.
     *
     * @param clientSettings The global client settings object
     */
    private NameTag(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Retrieves the display name of the NameTag option.
     *
     * @return The option name string
     */
    @Override
    public String getName() {
        return "NameTag";
    }

    /**
     * Retrieves a short description of the NameTag functionality.
     *
     * @return The short description string
     */
    @Override
    public String getShortDescription() {
        return "Activate or deactivate your nametag";
    }

    /**
     * Retrieves a detailed description of the NameTag functionality.
     *
     * @return The long description string
     */
    @Override
    public String getLongDescription() {
        return "It allows you to activate or deactivate your player's name tag.";
    }

    /**
     * Checks whether the NameTag option is currently enabled.
     *
     * @return True if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isNameTagEnabled();
    }

    /**
     * Updates the enabled state of the NameTag option.
     *
     * @param enabled The new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setNameTagEnabled(enabled);
    }
}
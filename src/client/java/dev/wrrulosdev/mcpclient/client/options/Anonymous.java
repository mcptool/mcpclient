package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class Anonymous extends OptionsBase {

    public static Anonymous INSTANCE;

    /**
     * Initializes the static singleton instance of the Anonymous option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new Anonymous(clientSettings);
    }

    private Anonymous(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Returns the display name of this option.
     *
     * @return The string "Anonymous"
     */
    @Override
    public String getName() {
        return "Anonymous";
    }

    /**
     * Returns a brief description of the option's purpose.
     *
     * @return A short summary string
     */
    @Override
    public String getShortDescription() {
        return "Hide your name in the client";
    }

    /**
     * Returns a detailed description of the option's functionality.
     *
     * @return A detailed explanation string
     */
    @Override
    public String getLongDescription() {
        return "It allows you to hide your name in your client.";
    }

    /**
     * Checks if the anonymous mode is currently active in the client settings.
     *
     * @return True if anonymous mode is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isAnonymousModeEnabled();
    }

    /**
     * Updates the anonymous mode state in the client settings.
     *
     * @param enabled The new state to apply
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setAnonymousModeEnabled(enabled);
    }
}
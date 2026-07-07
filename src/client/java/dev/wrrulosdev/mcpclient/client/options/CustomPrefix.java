package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public class CustomPrefix extends OptionsBase {

    /**
     * Singleton instance of the CustomPrefix module.
     */
    public static CustomPrefix INSTANCE;

    /**
     * Initializes the singleton instance with the provided client settings.
     *
     * @param clientSettings The current client configuration
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new CustomPrefix(clientSettings);
    }

    /**
     * Constructs a new CustomPrefix instance.
     *
     * @param clientSettings The client settings used to track the enabled state
     */
    protected CustomPrefix(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Returns the display name of this module.
     *
     * @return "CustomPrefix"
     */
    @Override
    public String getName() {
        return "CustomPrefix";
    }

    /**
     * Returns a short description for UI display.
     *
     * @return A brief summary of the module's functionality
     */
    @Override
    public String getShortDescription() {
        return "Assign a custom prefix to users.";
    }

    /**
     * Returns a detailed description of the module.
     *
     * @return A detailed explanation of the module's purpose
     */
    @Override
    public String getLongDescription() {
        return "Assign a custom prefix to specific usernames.";
    }

    /**
     * Checks if this module is currently enabled.
     *
     * @return True if AnvilButtons is enabled in client settings
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isCustomPrefixEnabled();
    }

    /**
     * Sets the enabled state of this module.
     *
     * @param enabled The new state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setCustomPrefixEnabled(enabled);
    }
}
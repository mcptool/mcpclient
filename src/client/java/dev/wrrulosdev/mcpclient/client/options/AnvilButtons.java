package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public class AnvilButtons extends OptionsBase {

    /**
     * Singleton instance of the AnvilButtons module.
     */
    public static AnvilButtons INSTANCE;

    /**
     * Initializes the singleton instance with the provided client settings.
     *
     * @param clientSettings The current client configuration
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new AnvilButtons(clientSettings);
    }

    /**
     * Constructs a new AnvilButtons instance.
     *
     * @param clientSettings The client settings used to track the enabled state
     */
    protected AnvilButtons(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Returns the display name of this module.
     *
     * @return "AnvilButtons"
     */
    @Override
    public String getName() {
        return "AnvilButtons";
    }

    /**
     * Returns a short description for UI display.
     *
     * @return A brief summary of the module's functionality
     */
    @Override
    public String getShortDescription() {
        return "Create or delete pre-loaded names in the anvils";
    }

    /**
     * Returns a detailed description of the module.
     *
     * @return A detailed explanation of the module's purpose
     */
    @Override
    public String getLongDescription() {
        return "Create or delete pre-loaded names in the anvil menu.";
    }

    /**
     * Checks if this module is currently enabled.
     *
     * @return True if AnvilButtons is enabled in client settings
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isAnvilButtonsEnabled();
    }

    /**
     * Sets the enabled state of this module.
     *
     * @param enabled The new state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setAnvilButtonsEnabled(enabled);
    }
}
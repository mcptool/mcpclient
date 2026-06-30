package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class ChatAnimation extends OptionsBase {

    public static ChatAnimation INSTANCE;

    /**
     * Initializes the static singleton instance of the Chat Animation option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new ChatAnimation(clientSettings);
    }

    /**
     * Constructs a new ChatAnimation instance with the provided client settings.
     *
     * @param clientSettings The global client settings object
     */
    private ChatAnimation(ClientSettings clientSettings) {
        super(clientSettings);
    }

    /**
     * Retrieves the display name of the Chat Animation option.
     *
     * @return The option name string
     */
    @Override
    public String getName() {
        return "Chat Animation";
    }

    /**
     * Retrieves a short description of the Chat Animation functionality.
     *
     * @return The short description string
     */
    @Override
    public String getShortDescription() {
        return "Enable or disable the chat animation";
    }

    /**
     * Retrieves a detailed description of the Chat Animation functionality.
     *
     * @return The long description string
     */
    @Override
    public String getLongDescription() {
        return "Enable or disable the chat animation";
    }

    /**
     * Checks whether the Chat Animation option is currently enabled.
     *
     * @return True if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return clientSettings.isChatAnimationEnabled();
    }

    /**
     * Updates the enabled state of the Chat Animation option.
     *
     * @param enabled The new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setChatAnimationEnabled(enabled);
    }
}
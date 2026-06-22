package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class ChatAnimation extends OptionsBase {

    public static ChatAnimation INSTANCE;

    /**
     * Initializes the static singleton instance of the Client Hud option.
     *
     * @param clientSettings The global client settings object
     */
    public static void init(ClientSettings clientSettings) {
        INSTANCE = new ChatAnimation(clientSettings);
    }

    private ChatAnimation(ClientSettings clientSettings) {
        super(clientSettings);
    }

    @Override
    public String getName() {
        return "Chat Animation";
    }

    @Override
    public String getShortDescription() {
        return "Enable or disable the chat animation";
    }

    @Override
    public String getLongDescription() {
        return "Enable or disable the chat animation";
    }

    @Override
    public boolean isEnabled() {
        return clientSettings.isChatAnimationEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setChatAnimationEnabled(enabled);
    }
}
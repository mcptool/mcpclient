package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public final class NameTag extends OptionsBase {

    public static NameTag INSTANCE;

    public static void init(ClientSettings clientSettings) {
        INSTANCE = new NameTag(clientSettings);
    }

    private NameTag(ClientSettings clientSettings) {
        super(clientSettings);
    }

    @Override
    public String getName() {
        return "NameTag";
    }

    @Override
    public String getShortDescription() {
        return "Activate or deactivate your nametag";
    }

    @Override
    public String getLongDescription() {
        return "It allows you to activate or deactivate your player's name tag.";
    }

    @Override
    public boolean isEnabled() {
        return clientSettings.isNameTagEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        clientSettings.setNameTagEnabled(enabled);
    }
}
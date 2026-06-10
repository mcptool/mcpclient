package dev.wrrulosdev.mcpclient.client.settings;

import dev.wrrulosdev.mcpclient.client.MCPClient;

public class CheatsSettings {

    private boolean flyEnabled = false;
    private boolean fakeGmEnabled = false;
    private boolean jesusEnabled = false;

    private void save() {
        MCPClient.saveSettings();
    }

    public boolean isFlyEnabled() {
        return flyEnabled;
    }

    public void setFlyEnabled(boolean flyEnabled) {
        this.flyEnabled = flyEnabled;
        save();
    }

    public boolean isFakeGmEnabled() {
        return fakeGmEnabled;
    }

    public void setFakeGmEnabled(boolean fakeGmEnabled) {
        this.fakeGmEnabled = fakeGmEnabled;
        save();
    }

    public boolean isJesusEnabled() {
        return jesusEnabled;
    }

    public void setJesusEnabled(boolean jesusEnabled) {
        this.jesusEnabled = jesusEnabled;
        save();
    }
}

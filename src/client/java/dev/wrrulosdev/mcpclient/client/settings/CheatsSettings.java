package dev.wrrulosdev.mcpclient.client.settings;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;

import java.util.HashMap;
import java.util.Map;

public class CheatsSettings {

    private final Map<String, Integer> keyBinds = new HashMap<>();

    private boolean flyEnabled = false;
    private float flySpeed = 0.5f;

    private boolean fakeGmEnabled = false;

    private boolean jesusEnabled = false;
    private boolean jesusWaterEnabled = true;
    private boolean jesusLavaEnabled = true;

    private boolean spiderEnabled = false;
    private boolean wallhackEnabled = false;
    private boolean antikbEnabled = false;
    private boolean hClipEnabled = false;
    private boolean vClipEnabled = false;
    private boolean fullBrightEnabled = false;
    private boolean noFallEnabled = false;
    private boolean blockTrackerEnabled = false;

    private void save() {
        MCPClient.saveSettings();
    }

    public int getKeyForKeyBind(String id) {
        return keyBinds.getOrDefault(id, ClientConstants.DEFAULT_INVALID_KEYBIND);
    }

    public void setKeyForKeyBind(String id, int keyCode) {
        keyBinds.put(id, keyCode);
        save();
    }

    public boolean isFlyEnabled() { return flyEnabled; }
    public void setFlyEnabled(boolean flyEnabled) { this.flyEnabled = flyEnabled; save(); }

    public float getFlySpeed() { return flySpeed; }
    public void setFlySpeed(float flySpeed) { this.flySpeed = flySpeed; save(); }

    public boolean isFakeGmEnabled() { return fakeGmEnabled; }
    public void setFakeGmEnabled(boolean fakeGmEnabled) { this.fakeGmEnabled = fakeGmEnabled; save(); }

    public boolean isJesusEnabled() { return jesusEnabled; }
    public void setJesusEnabled(boolean jesusEnabled) { this.jesusEnabled = jesusEnabled; save(); }

    public boolean isJesusWaterEnabled() {
        return jesusWaterEnabled;
    }

    public void setJesusWaterEnabled(boolean jesusWater) {
        this.jesusWaterEnabled = jesusWater;
        save();
    }

    public boolean isJesusLavaEnabled() {
        return jesusLavaEnabled;
    }

    public void setJesusLavaEnabled(boolean jesusLava) {
        this.jesusLavaEnabled = jesusLava;
        save();
    }

    public boolean isWallhackEnabled() { return wallhackEnabled; }
    public void setWallhackEnabled(boolean wallhackEnabled) { this.wallhackEnabled = wallhackEnabled; save(); }

    public boolean isSpiderEnabled() { return spiderEnabled; }
    public void setSpiderEnabled(boolean spiderEnabled) { this.spiderEnabled = spiderEnabled; save(); }

    public boolean isAntikbEnabled() { return antikbEnabled; }
    public void setAntikbEnabled(boolean antikbEnabled) { this.antikbEnabled = antikbEnabled; save(); }

    public boolean isHClipEnabled() { return hClipEnabled; }
    public void setHClipEnabled(boolean hClipEnabled) { this.hClipEnabled = hClipEnabled; save(); }

    public boolean isVClipEnabled() { return vClipEnabled; }
    public void setVClipEnabled(boolean vClipEnabled) { this.vClipEnabled = vClipEnabled; save(); }

    public boolean isFullBrightEnabled() { return fullBrightEnabled; }
    public void setFullBrightEnabled(boolean fullBrightEnabled) { this.fullBrightEnabled = fullBrightEnabled; save(); }

    public boolean isNoFallEnabled() { return noFallEnabled; }
    public void setNoFallEnabled(boolean noFallEnabled) { this.noFallEnabled = noFallEnabled; save(); }

    public boolean isBlockTrackerEnabled() { return blockTrackerEnabled; }
    public void setBlockTrackerEnabled(boolean blockTrackerEnabled) { this.blockTrackerEnabled = blockTrackerEnabled; save(); }

}
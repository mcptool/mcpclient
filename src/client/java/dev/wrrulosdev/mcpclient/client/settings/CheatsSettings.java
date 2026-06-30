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
    private double jesusSpeed = 1.0D;

    private boolean spiderEnabled = false;
    private double spiderSpeed = 0.2D;

    private boolean wallhackEnabled = false;
    private boolean wallHackBoxesEnabled = false;
    private boolean wallHackStickManEnabled = false;

    private boolean antikbEnabled = false;

    private boolean hClipEnabled = false;
    private double hClipDistance = 0.0D;

    private boolean vClipEnabled = false;
    private double vClipDistance = 0.0D;

    private boolean fullBrightEnabled = false;
    private double fullBrightAmount = 100.0D;

    private boolean noFallEnabled = false;

    private boolean blockTrackerEnabled = false;
    private boolean blockTrackerCoalOresEnabled = false;
    private boolean blockTrackerIronOresEnabled = true;
    private boolean blockTrackerCopperOresEnabled = false;
    private boolean blockTrackerGoldOresEnabled = true;
    private boolean blockTrackerRedstoneOresEnabled = false;
    private boolean blockTrackerLapisOresEnabled = false;
    private boolean blockTrackerDiamondOresEnabled = true;
    private boolean blockTrackerEmeraldOresEnabled = false;
    private boolean blockTrackerNetherOresEnabled = false;
    private boolean blockTrackerAncientDebrisEnabled = false;
    private boolean blockTrackerMineralBlocksEnabled = false;
    private boolean blockTrackerStorageEnabled = false;
    private boolean blockTrackerUtilityEnabled = false;
    private boolean blockTrackerRedstoneEnabled = false;
    private float blockTrackerScanRadius = 32.0f;
    private float blockTrackerScanDelay = 2.0f;

    /**
     * Persists the current cheat settings to the client configuration file.
     */
    private void save() {
        MCPClient.saveSettings();
    }

    /**
     * Retrieves the key code associated with a specific cheat identifier.
     *
     * @param id The cheat identifier
     * @return The key code, or the default invalid key if not set
     */
    public int getKeyForKeyBind(String id) {
        return keyBinds.getOrDefault(id, ClientConstants.DEFAULT_INVALID_KEYBIND);
    }

    /**
     * Sets the key code for a specific cheat identifier and saves the settings.
     *
     * @param id      The cheat identifier
     * @param keyCode The key code to assign
     */
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

    public boolean isJesusWaterEnabled() { return jesusWaterEnabled; }
    public void setJesusWaterEnabled(boolean jesusWater) { this.jesusWaterEnabled = jesusWater; save(); }

    public boolean isJesusLavaEnabled() { return jesusLavaEnabled; }
    public void setJesusLavaEnabled(boolean jesusLava) { this.jesusLavaEnabled = jesusLava; save(); }

    public double getJesusSpeed() { return jesusSpeed; }
    public void setJesusSpeed(double jesusSpeed) { this.jesusSpeed = jesusSpeed; save(); }

    public boolean isWallhackEnabled() { return wallhackEnabled; }
    public void setWallhackEnabled(boolean wallhackEnabled) { this.wallhackEnabled = wallhackEnabled; save(); }

    public boolean isWallHackStickManEnabled() { return wallHackStickManEnabled; }
    public void setWallHackStickManEnabled(boolean wallHackStickManEnabled) { this.wallHackStickManEnabled = wallHackStickManEnabled; save(); }

    public boolean isWallHackBoxesEnabled() { return wallHackBoxesEnabled; }
    public void setWallHackBoxesEnabled(boolean wallHackBoxesEnabled) { this.wallHackBoxesEnabled = wallHackBoxesEnabled; save(); }

    public boolean isSpiderEnabled() { return spiderEnabled; }
    public void setSpiderEnabled(boolean spiderEnabled) { this.spiderEnabled = spiderEnabled; save(); }

    public double getSpiderSpeed() { return spiderSpeed; }
    public void setSpiderSpeed(double spiderSpeed) { this.spiderSpeed = spiderSpeed; save(); }

    public boolean isAntikbEnabled() { return antikbEnabled; }
    public void setAntikbEnabled(boolean antikbEnabled) { this.antikbEnabled = antikbEnabled; save(); }

    public boolean isHClipEnabled() { return hClipEnabled; }
    public void setHClipEnabled(boolean hClipEnabled) { this.hClipEnabled = hClipEnabled; save(); }

    public double gethClipDistance() { return hClipDistance; }
    public void sethClipDistance(double hClipDistance) { this.hClipDistance = hClipDistance; save(); }

    public boolean isVClipEnabled() { return vClipEnabled; }
    public void setVClipEnabled(boolean vClipEnabled) { this.vClipEnabled = vClipEnabled; save(); }

    public double getvClipDistance() { return vClipDistance; }
    public void setvClipDistance(double vClipDistance) { this.vClipDistance = vClipDistance; save(); }

    public boolean isFullBrightEnabled() { return fullBrightEnabled; }
    public void setFullBrightEnabled(boolean fullBrightEnabled) { this.fullBrightEnabled = fullBrightEnabled; save(); }

    public double getFullBrightAmount() { return fullBrightAmount; }
    public void setFullBrightAmount(double fullBrightAmount) { this.fullBrightAmount = fullBrightAmount; save(); }

    public boolean isNoFallEnabled() { return noFallEnabled; }
    public void setNoFallEnabled(boolean noFallEnabled) { this.noFallEnabled = noFallEnabled; save(); }

    public boolean isBlockTrackerEnabled() { return blockTrackerEnabled; }
    public void setBlockTrackerEnabled(boolean blockTrackerEnabled) { this.blockTrackerEnabled = blockTrackerEnabled; save(); }

    public boolean isBlockTrackerCoalOresEnabled() { return blockTrackerCoalOresEnabled; }
    public void setBlockTrackerCoalOresEnabled(boolean value) { this.blockTrackerCoalOresEnabled = value; save(); }

    public boolean isBlockTrackerIronOresEnabled() { return blockTrackerIronOresEnabled; }
    public void setBlockTrackerIronOresEnabled(boolean value) { this.blockTrackerIronOresEnabled = value; save(); }

    public boolean isBlockTrackerCopperOresEnabled() { return blockTrackerCopperOresEnabled; }
    public void setBlockTrackerCopperOresEnabled(boolean value) { this.blockTrackerCopperOresEnabled = value; save(); }

    public boolean isBlockTrackerGoldOresEnabled() { return blockTrackerGoldOresEnabled; }
    public void setBlockTrackerGoldOresEnabled(boolean value) { this.blockTrackerGoldOresEnabled = value; save(); }

    public boolean isBlockTrackerRedstoneOresEnabled() { return blockTrackerRedstoneOresEnabled; }
    public void setBlockTrackerRedstoneOresEnabled(boolean value) { this.blockTrackerRedstoneOresEnabled = value; save(); }

    public boolean isBlockTrackerLapisOresEnabled() { return blockTrackerLapisOresEnabled; }
    public void setBlockTrackerLapisOresEnabled(boolean value) { this.blockTrackerLapisOresEnabled = value; save(); }

    public boolean isBlockTrackerDiamondOresEnabled() { return blockTrackerDiamondOresEnabled; }
    public void setBlockTrackerDiamondOresEnabled(boolean value) { this.blockTrackerDiamondOresEnabled = value; save(); }

    public boolean isBlockTrackerEmeraldOresEnabled() { return blockTrackerEmeraldOresEnabled; }
    public void setBlockTrackerEmeraldOresEnabled(boolean value) { this.blockTrackerEmeraldOresEnabled = value; save(); }

    public boolean isBlockTrackerNetherOresEnabled() { return blockTrackerNetherOresEnabled; }
    public void setBlockTrackerNetherOresEnabled(boolean value) { this.blockTrackerNetherOresEnabled = value; save(); }

    public boolean isBlockTrackerAncientDebrisEnabled() { return blockTrackerAncientDebrisEnabled; }
    public void setBlockTrackerAncientDebrisEnabled(boolean value) { this.blockTrackerAncientDebrisEnabled = value; save(); }

    public boolean isBlockTrackerMineralBlocksEnabled() { return blockTrackerMineralBlocksEnabled; }
    public void setBlockTrackerMineralBlocksEnabled(boolean blockTrackerMineralBlocksEnabled) { this.blockTrackerMineralBlocksEnabled = blockTrackerMineralBlocksEnabled; save(); }

    public boolean isBlockTrackerStorageEnabled() { return blockTrackerStorageEnabled; }
    public void setBlockTrackerStorageEnabled(boolean blockTrackerStorageEnabled) { this.blockTrackerStorageEnabled = blockTrackerStorageEnabled; save(); }

    public boolean isBlockTrackerUtilityEnabled() { return blockTrackerUtilityEnabled; }
    public void setBlockTrackerUtilityEnabled(boolean blockTrackerUtilityEnabled) { this.blockTrackerUtilityEnabled = blockTrackerUtilityEnabled; save(); }

    public boolean isBlockTrackerRedstoneEnabled() { return blockTrackerRedstoneEnabled; }
    public void setBlockTrackerRedstoneEnabled(boolean blockTrackerRedstoneEnabled) { this.blockTrackerRedstoneEnabled = blockTrackerRedstoneEnabled; save(); }

    public float getBlockTrackerScanRadius() { return blockTrackerScanRadius; }
    public void setBlockTrackerScanRadius(float blockTrackerScanRadius) { this.blockTrackerScanRadius = blockTrackerScanRadius; save(); }

    public float getBlockTrackerScanDelay() { return blockTrackerScanDelay; }
    public void setBlockTrackerScanDelay(float blockTrackerScanDelay) { this.blockTrackerScanDelay = blockTrackerScanDelay; save(); }
}
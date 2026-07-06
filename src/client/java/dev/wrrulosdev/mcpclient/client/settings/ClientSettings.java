package dev.wrrulosdev.mcpclient.client.settings;

import dev.wrrulosdev.mcpclient.client.MCPClient;

import java.util.HashMap;
import java.util.Map;

public class ClientSettings {

    private boolean anonymousModeEnabled = false;

    private boolean anonymousScoreboardEnabled = true;
    private boolean anonymousTabListEnabled = true;
    private boolean anonymousChatEnabled = true;
    private boolean anonymousHologramsEnabled = true;
    private boolean anonymousNameTagsEnabled = true;
    private String newAnonymousName = "***";

    private boolean clientHudEnabled = true;
    private boolean clientHudFpsEnabled = true;

    private boolean nameTagEnabled = true;
    private boolean nameTagColorEnabled = true;
    private int nameTagColor = 0xFFFFFF;

    private boolean chatAnimationEnabled = true;
    private double chatAnimationDuration = 0.015;

    private boolean notificationsEnabled = true;
    private double notificationDuration = 3;

    private boolean playerModelEnabled = true;
    private double playerModelMarginX = 5;
    private double playerModelMarginY = 45;
    private double playerModelSize = 30;

    private boolean anvilButtonsEnabled = false;
    private Map<String, String> anvilButtons = new HashMap<>();

    /**
     * Persists the current configuration state to the client's storage.
     */
    private void save() {
        MCPClient.saveSettings();
    }

    public boolean isAnonymousModeEnabled() {
        return anonymousModeEnabled;
    }

    public void setAnonymousModeEnabled(boolean anonymousModeEnabled) {
        this.anonymousModeEnabled = anonymousModeEnabled;
        save();
    }

    public boolean isAnonymousScoreboardEnabled() {
        return anonymousScoreboardEnabled;
    }

    public void setAnonymousScoreboardEnabled(boolean anonymousScoreboardEnabled) {
        this.anonymousScoreboardEnabled = anonymousScoreboardEnabled;
        save();
    }

    public boolean isAnonymousTabListEnabled() {
        return anonymousTabListEnabled;
    }

    public void setAnonymousTabListEnabled(boolean anonymousTabListEnabled) {
        this.anonymousTabListEnabled = anonymousTabListEnabled;
        save();
    }

    public boolean isAnonymousChatEnabled() {
        return anonymousChatEnabled;
    }

    public void setAnonymousChatEnabled(boolean anonymousChatEnabled) {
        this.anonymousChatEnabled = anonymousChatEnabled;
        save();
    }

    public boolean isAnonymousHologramsEnabled() {
        return anonymousHologramsEnabled;
    }

    public void setAnonymousHologramsEnabled(boolean anonymousHologramsEnabled) {
        this.anonymousHologramsEnabled = anonymousHologramsEnabled;
        save();
    }

    public boolean isAnonymousNameTagsEnabled() {
        return anonymousNameTagsEnabled;
    }

    public void setAnonymousNameTagsEnabled(boolean anonymousNameTagsEnabled) {
        this.anonymousNameTagsEnabled = anonymousNameTagsEnabled;
        save();
    }

    public String getNewAnonymousName() {
        return newAnonymousName;
    }

    public void setNewAnonymousName(String newAnonymousName) {
        this.newAnonymousName = newAnonymousName;
        save();
    }

    public boolean isClientHudEnabled() {
        return clientHudEnabled;
    }

    public void setClientHudEnabled(boolean clientHudEnabled) {
        this.clientHudEnabled = clientHudEnabled;
        save();
    }

    public boolean isClientHudFpsEnabled() {
        return clientHudFpsEnabled;
    }

    public void setClientHudFpsEnabled(boolean clientHudFpsEnabled) {
        this.clientHudFpsEnabled = clientHudFpsEnabled;
        save();
    }

    public boolean isNameTagEnabled() {
        return nameTagEnabled;
    }

    public void setNameTagEnabled(boolean nameTagEnabled) {
        this.nameTagEnabled = nameTagEnabled;
        save();
    }

    public int getNameTagColor() {
        return nameTagColor;
    }

    public void setNameTagColor(int nameTagColor) {
        this.nameTagColor = nameTagColor;
        save();
    }

    public boolean isNameTagColorEnabled() {
        return nameTagColorEnabled;
    }

    public void setNameTagColorEnabled(boolean nameTagColorEnabled) {
        this.nameTagColorEnabled = nameTagColorEnabled;
        save();
    }

    public boolean isChatAnimationEnabled() {
        return chatAnimationEnabled;
    }

    public void setChatAnimationEnabled(boolean chatAnimationEnabled) {
        this.chatAnimationEnabled = chatAnimationEnabled;
        save();
    }

    public double getChatAnimationDuration() {
        return chatAnimationDuration;
    }

    public void setChatAnimationDuration(double chatAnimationDuration) {
        this.chatAnimationDuration = chatAnimationDuration;
        save();
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
        save();
    }

    public double getNotificationDuration() {
        return notificationDuration;
    }

    public void setNotificationDuration(double notificationDuration) {
        this.notificationDuration = notificationDuration;
        save();
    }

    public boolean isPlayerModelEnabled() {
        return playerModelEnabled;
    }

    public void setPlayerModelEnabled(boolean playerModelEnabled) {
        this.playerModelEnabled = playerModelEnabled;
        save();
    }

    public double getPlayerModelMarginX() {
        return playerModelMarginX;
    }

    public void setPlayerModelMarginX(double playerModelMarginX) {
        this.playerModelMarginX = playerModelMarginX;
        save();
    }

    public double getPlayerModelMarginY() {
        return playerModelMarginY;
    }

    public void setPlayerModelMarginY(double playerModelMarginY) {
        this.playerModelMarginY = playerModelMarginY;
        save();
    }

    public double getPlayerModelSize() {
        return playerModelSize;
    }

    public void setPlayerModelSize(double playerModelSize) {
        this.playerModelSize = playerModelSize;
        save();
    }

    public boolean isAnvilButtonsEnabled() {
        return anvilButtonsEnabled;
    }

    public void setAnvilButtonsEnabled(boolean anvilButtonsEnabled) {
        this.anvilButtonsEnabled = anvilButtonsEnabled;
        save();
    }

    public Map<String, String> getAnvilButtons() {
        return anvilButtons;
    }

    public void setAnvilButtons(Map<String, String> newMap) {
        this.anvilButtons = new HashMap<>(newMap);
        save();
    }
}
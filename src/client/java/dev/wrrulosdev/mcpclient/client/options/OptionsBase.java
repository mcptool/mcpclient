package dev.wrrulosdev.mcpclient.client.options;

import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;

public abstract class OptionsBase {

    protected final ClientSettings clientSettings;

    protected OptionsBase(ClientSettings clientSettings) {
        this.clientSettings = clientSettings;
    }

    /**
     * Returns the human-readable name of the option.
     *
     * @return The name string
     */
    public abstract String getName();

    /**
     * Returns a brief summary of what the option does.
     *
     * @return The short description string
     */
    public abstract String getShortDescription();

    /**
     * Returns a detailed explanation of the option's functionality.
     *
     * @return The long description string
     */
    public abstract String getLongDescription();

    /**
     * Checks if the current option is enabled.
     *
     * @return True if enabled, false otherwise
     */
    public abstract boolean isEnabled();

    /**
     * Sets the enabled state of the option.
     *
     * @param enabled The new state to apply
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Toggles the current state of the option and displays a notification
     * to the user indicating the new status.
     */
    public final void toggle() {
        boolean newState = !isEnabled();
        String optionName = getName();
        setEnabled(newState);
        NotificationManager.show(
            optionName,
            optionName + " was " + (isEnabled() ? "activated" : "deactivated"),
            isEnabled() ? NotificationType.SUCCESS : NotificationType.WARNING
        );
    }
}

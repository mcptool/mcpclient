package dev.wrrulosdev.mcpclient.client.notifications;

import net.minecraft.util.Util;

public class Notification {

    public final String title;
    public final String message;
    public final NotificationType type;
    public final long startTime;
    public final long duration;

    /**
     * Current interpolated Y position used for smooth vertical stacking animation.
     * A value of -1 indicates the notification has not yet been positioned.
     */
    public float currentY;

    /**
     * Creates a new notification instance with a defined duration.
     *
     * @param title Notification title text
     * @param message Notification body text
     * @param type Visual type defining color and behavior
     * @param duration Lifetime of the notification in milliseconds
     */
    public Notification(String title, String message, NotificationType type, long duration) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.duration = duration;
        this.startTime = Util.getMillis();
        this.currentY = -1;
    }
}
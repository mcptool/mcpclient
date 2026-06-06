package dev.wrrulosdev.mcpclient.client.notifications;

public enum NotificationType {

    INFO(0x00A8FF),
    SUCCESS(0x20BF6B),
    WARNING(0xF7B731),
    ERROR(0xEB3B5A);

    /**
     * ARGB accent color associated with the notification type.
     * Used for UI elements such as side bars and progress indicators.
     */
    public final int color;

    /**
     * Creates a notification type with a defined accent color.
     *
     * @param color RGB color value used for rendering the notification accent
     */
    NotificationType(int color) {
        this.color = color;
    }
}
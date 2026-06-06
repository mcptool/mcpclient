package dev.wrrulosdev.mcpclient.client.notifications;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {

    private static final List<Notification> notifications = new ArrayList<>();

    private static final int NOTIFICATION_WIDTH = 160;
    private static final int MIN_NOTIFICATION_HEIGHT = 45;
    private static final int GAP = 12;
    private static final int MARGIN_RIGHT = 15;
    private static final int MARGIN_BOTTOM = 15;

    private static final int TEXT_MARGIN_LEFT = 14;
    private static final int TEXT_MARGIN_RIGHT = 10;
    private static final int TEXT_MARGIN_Y = 8;
    private static final int TEXT_SPACING_Y = 14;
    private static final int TEXT_LINE_SPACING = 2;

    private static final long SLIDE_IN_TIME = 450;
    private static final long FADE_OUT_TIME = 400;

    /**
     * Creates and displays a notification using the default duration.
     *
     * @param title Notification title text
     * @param message Notification body text
     * @param type Notification visual style type
     */
    public static void show(String title, String message, NotificationType type) {
        notifications.add(new Notification(title, message, type, 3000));
    }

    /**
     * Creates and displays a notification with a custom duration.
     *
     * @param title Notification title text
     * @param message Notification body text
     * @param type Notification visual style type
     * @param duration Display time in milliseconds
     */
    public static void show(String title, String message, NotificationType type, long duration) {
        notifications.add(new Notification(title, message, type, duration));
    }

    /**
     * Renders and updates all active notifications each frame.
     * <p>
     * Handles animation timing, vertical stacking, fade effects, and dynamic layout calculation.
     * </p>
     *
     * @param graphics Render context used for drawing UI elements
     * @param deltaTracker Frame timing information
     */
    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (notifications.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        Font font = mc.font;

        long currentTime = Util.getMillis();
        int targetYOffset = 0;

        Iterator<Notification> iterator = notifications.iterator();

        while (iterator.hasNext()) {
            Notification notif = iterator.next();
            long timeAlive = currentTime - notif.startTime;

            if (timeAlive > notif.duration + SLIDE_IN_TIME + FADE_OUT_TIME) {
                iterator.remove();
                continue;
            }

            int maxTextWidth = NOTIFICATION_WIDTH - TEXT_MARGIN_LEFT - TEXT_MARGIN_RIGHT;
            List<String> messageLines = wrapText(font, notif.message, maxTextWidth);

            int contentHeight =
                TEXT_MARGIN_Y + TEXT_SPACING_Y +
                    (messageLines.size() * (font.lineHeight + TEXT_LINE_SPACING)) + 8;

            int currentHeight = Math.max(MIN_NOTIFICATION_HEIGHT, contentHeight);

            if (notif.currentY == -1) {
                notif.currentY = screenHeight + 20;
            }

            int expectedY = screenHeight - MARGIN_BOTTOM - currentHeight - targetYOffset;
            notif.currentY += (expectedY - notif.currentY) * 0.15f;

            targetYOffset += currentHeight + GAP;

            float currentX = screenWidth - NOTIFICATION_WIDTH - MARGIN_RIGHT;
            float alpha = 1.0f;

            if (timeAlive < SLIDE_IN_TIME) {
                float progress = (float) timeAlive / SLIDE_IN_TIME;
                float p = progress - 1.0f;
                float easeOut = p * p * ((1.5f + 1.0f) * p + 1.5f) + 1.0f;

                currentX = screenWidth - ((NOTIFICATION_WIDTH + MARGIN_RIGHT) * easeOut);
            } else if (timeAlive > notif.duration + SLIDE_IN_TIME) {
                long timeFading = timeAlive - (notif.duration + SLIDE_IN_TIME);
                float progress = (float) timeFading / FADE_OUT_TIME;

                alpha = Math.max(0.0f, 1.0f - progress);
            }

            renderCard(
                graphics,
                font,
                notif,
                (int) currentX,
                (int) notif.currentY,
                currentHeight,
                alpha,
                timeAlive,
                messageLines,
                maxTextWidth
            );
        }
    }

    /**
     * Renders a single notification card including background, text content,
     * accent bar, and progress indicator.
     *
     * @param graphics Render context used for drawing
     * @param font Font renderer
     * @param notif Notification instance
     * @param x X screen position
     * @param y Y screen position
     * @param height Computed dynamic height
     * @param alpha Global opacity value
     * @param timeAlive Time since notification creation
     * @param messageLines Wrapped message lines
     * @param maxTextWidth Maximum width allowed for text rendering
     */
    private static void renderCard(GuiGraphicsExtractor graphics, Font font, Notification notif,
                                   int x, int y, int height, float alpha, long timeAlive,
                                   List<String> messageLines, int maxTextWidth) {

        int bgAlpha = (int) (220 * alpha);
        int shadowAlpha = (int) (40 * alpha);
        int textAlpha = (int) (255 * alpha);

        int bgColor = (bgAlpha << 24) | 0x141518;
        int outlineColor = (bgAlpha << 24) | 0x2A2B30;
        int titleColor = (textAlpha << 24) | 0xFFFFFF;
        int descColor = (textAlpha << 24) | 0xA0A0A0;
        int typeColor = (textAlpha << 24) | notif.type.color;
        int shadowColor = (shadowAlpha << 24);

        fillRoundedRect(graphics, x - 2, y - 2, x + NOTIFICATION_WIDTH + 2, y + height + 2, shadowColor);
        fillRoundedRect(graphics, x - 1, y - 1, x + NOTIFICATION_WIDTH + 1, y + height + 1, shadowColor);

        fillRoundedRect(graphics, x - 1, y - 1, x + NOTIFICATION_WIDTH + 1, y + height + 1, outlineColor);
        fillRoundedRect(graphics, x, y, x + NOTIFICATION_WIDTH, y + height, bgColor);

        graphics.fill(x + 2, y + 4, x + 4, y + height - 4, typeColor);

        float timeProgress = Math.clamp((float) (timeAlive - SLIDE_IN_TIME) / notif.duration,
            0.0f, 1.0f);

        int maxBarWidth = NOTIFICATION_WIDTH - 8;
        int barWidth = (int) (maxBarWidth * (1.0f - timeProgress));

        if (barWidth > 0) {
            graphics.fill(x + 4, y + height - 3, x + 4 + barWidth, y + height - 2, typeColor);
        }

        int textX = x + TEXT_MARGIN_LEFT;
        int titleY = y + TEXT_MARGIN_Y;

        String displayTitle = trimStringToWidth(font, notif.title, maxTextWidth);
        graphics.text(font, Component.literal(displayTitle), textX, titleY, titleColor, true);

        int currentDescY = titleY + TEXT_SPACING_Y;

        for (String line : messageLines) {
            graphics.text(font, Component.literal(line), textX, currentDescY, descColor, false);
            currentDescY += font.lineHeight + TEXT_LINE_SPACING;
        }
    }

    /**
     * Splits a string into multiple lines without breaking words.
     *
     * @param font Font renderer used for measuring text width
     * @param text Input text
     * @param maxWidth Maximum allowed line width
     * @return Wrapped lines of text
     */
    private static List<String> wrapText(Font font, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;

            if (font.width(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    /**
     * Trims a string to fit within a maximum width and appends ellipsis if required.
     *
     * @param font Font renderer used for measuring text width
     * @param text Input text
     * @param maxWidth Maximum allowed width
     * @return Trimmed text with ellipsis if needed
     */
    private static String trimStringToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }

        String ellipsis = "...";
        int ellipsisWidth = font.width(ellipsis);

        while (!text.isEmpty() && font.width(text) + ellipsisWidth > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }

        return text + ellipsis;
    }

    /**
     * Draws a rounded rectangle using segmented fill operations.
     *
     * @param g Render context
     * @param x1 Left position
     * @param y1 Top position
     * @param x2 Right position
     * @param y2 Bottom position
     * @param c ARGB color value
     */
    private static void fillRoundedRect(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int c) {
        g.fill(x1 + 4, y1, x2 - 4, y2, c);
        g.fill(x1, y1 + 4, x2, y2 - 4, c);

        g.fill(x1 + 1, y1 + 2, x1 + 4, y1 + 4, c);
        g.fill(x1 + 2, y1 + 1, x1 + 4, y1 + 2, c);

        g.fill(x2 - 4, y1 + 2, x2 - 1, y1 + 4, c);
        g.fill(x2 - 4, y1 + 1, x2 - 2, y1 + 2, c);

        g.fill(x1 + 1, y2 - 4, x1 + 4, y2 - 2, c);
        g.fill(x1 + 2, y2 - 2, x1 + 4, y2 - 1, c);

        g.fill(x2 - 4, y2 - 4, x2 - 1, y2 - 2, c);
        g.fill(x2 - 4, y2 - 2, x2 - 2, y2 - 1, c);
    }
}
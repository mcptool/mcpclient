package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class CategoryCard {

    private static final int FIXED_CARD_HEIGHT = 45;
    private static final int VERTICAL_GAP = 5;
    private static final int ICON_SIZE = 16;
    private static final int PADDING_X = 12;
    private static final int ICON_TEXT_SPACING = 8;

    private final Component title;
    private final Component description;
    private final Identifier icon;
    private final Runnable onClickAction;

    private int lastX, lastY, lastWidth;

    public CategoryCard(String title, String description, Identifier icon, Runnable onClickAction) {
        this.title = Component.literal(title);
        this.description = Component.literal(description);
        this.icon = icon;
        this.onClickAction = onClickAction;
    }

    /**
     * Renders the category card and updates internal state for interaction detection.
     *
     * @param graphics    The {@link GuiGraphicsExtractor} used for drawing.
     * @param font        The font renderer.
     * @param x           The X position of the card.
     * @param y           The Y position of the card.
     * @param width       The width of the card.
     * @param rawProgress The animation progress coefficient.
     * @param mouseX      The current mouse X coordinate.
     * @param mouseY      The current mouse Y coordinate.
     * @return The constant height of the card.
     */
    public int render(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, float rawProgress, int mouseX, int mouseY) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;

        boolean isHovered = isMouseOver(mouseX, mouseY);

        renderBackground(graphics, x, y, width, isHovered, rawProgress);
        renderIcon(graphics, x, y, rawProgress);
        renderText(graphics, font, x, y, width, isHovered, rawProgress);

        return FIXED_CARD_HEIGHT;
    }

    /**
     * Renders the background and border of the card.
     */
    private void renderBackground(GuiGraphicsExtractor graphics, int x, int y, int width, boolean hovered, float progress) {
        int color = getAlphaColor(0x1E1F25, 200 * progress / 255);
        fillRoundedRect(graphics, x, y, x + width, y + FIXED_CARD_HEIGHT, color);

        int borderColor = getAlphaColor(hovered ? 0xB71C1C : 0x444444, progress);
        renderBorder(graphics, x, y, width, FIXED_CARD_HEIGHT, borderColor);
    }

    /**
     * Renders the card icon.
     */
    private void renderIcon(GuiGraphicsExtractor graphics, int x, int y, float progress) {
        int iconX = x + PADDING_X;
        int iconY = y + (FIXED_CARD_HEIGHT / 2) - (ICON_SIZE / 2);

        if (this.icon != null) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, this.icon, iconX, iconY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        } else {
            graphics.fill(iconX, iconY, iconX + ICON_SIZE, iconY + ICON_SIZE, getAlphaColor(0xAAAAAA, progress));
        }
    }

    /**
     * Renders the text content including title and description.
     */
    private void renderText(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, boolean isHovered, float progress) {
        float descScale = 0.92f;
        int arrowWidth = font.width("->");

        int titleX = x + PADDING_X + ICON_SIZE + ICON_TEXT_SPACING;
        int usedWidth = (PADDING_X * 2) + ICON_SIZE + ICON_TEXT_SPACING + arrowWidth;
        int descMaxWidth = (int) ((width - usedWidth) / descScale);

        List<FormattedCharSequence> lines = font.split(this.description, descMaxWidth);

        int titleHeight = font.lineHeight;
        int descHeight = (int) ((lines.size() * (font.lineHeight + 2)) * descScale);
        int totalContentHeight = titleHeight + VERTICAL_GAP + descHeight;

        int textBlockY = y + (FIXED_CARD_HEIGHT - totalContentHeight) / 2;

        graphics.text(font, this.title, titleX, textBlockY, getAlphaColor(0xFFFFFF, progress), false);

        float invScale = 1.0f / descScale;
        var pose = graphics.pose();
        pose.pushMatrix();
        pose.scale(descScale, descScale);

        float descY = (textBlockY + titleHeight + VERTICAL_GAP) * invScale;

        for (FormattedCharSequence line : lines) {
            graphics.text(font, line, (int) (titleX * invScale), (int) descY, getAlphaColor(0xAAAAAA, progress * 0.7f), false);
            descY += (font.lineHeight + 2) * invScale;
        }
        pose.popMatrix();

        int arrowX = x + width - PADDING_X - arrowWidth;
        int arrowY = y + (FIXED_CARD_HEIGHT / 2) - (font.lineHeight / 2);
        int arrowColor = isHovered ? getAlphaColor(0xB71C1C, progress) : getAlphaColor(0xFFFFFF, progress);

        graphics.text(font, "->", arrowX, arrowY, arrowColor, false);
    }

    /**
     * Checks if the card was clicked and triggers the associated action.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param button The mouse button pressed.
     * @return True if the event was consumed.
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isMouseOver((int) mouseX, (int) mouseY)) {
            if (this.onClickAction != null) onClickAction.run();
            return true;
        }
        return false;
    }

    /**
     * Verifies if the mouse is hovering over the card.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @return True if the mouse is within the card bounds.
     */
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= lastX && mouseX <= lastX + lastWidth &&
            mouseY >= lastY && mouseY <= lastY + FIXED_CARD_HEIGHT;
    }

    private int getAlphaColor(int rgb, float alpha) {
        return ((int) (alpha * 255) << 24) | rgb;
    }

    private void fillRoundedRect(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int color) {
        graphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, color);
        graphics.fill(x1 + 2, y1, x2 - 2, y1 + 1, color);
        graphics.fill(x1 + 2, y2 - 1, x2 - 2, y2, color);
        graphics.fill(x1, y1 + 2, x1 + 1, y2 - 2, color);
        graphics.fill(x2 - 1, y1 + 2, x2, y2 - 2, color);
    }

    private void renderBorder(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int color) {
        int x2 = x + width, y2 = y + height;
        graphics.fill(x + 2, y, x2 - 2, y + 1, color);
        graphics.fill(x + 2, y2 - 1, x2 - 2, y2, color);
        graphics.fill(x, y + 2, x + 1, y2 - 2, color);
        graphics.fill(x2 - 1, y + 2, x2, y2 - 2, color);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, color);
        graphics.fill(x2 - 2, y + 1, x2 - 1, y + 2, color);
        graphics.fill(x + 1, y2 - 2, x + 2, y2 - 1, color);
        graphics.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, color);
    }
}
package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class CategoryCard extends AbstractSettingComponent {

    private static final int FIXED_CARD_HEIGHT = 45;
    private static final int VERTICAL_GAP = 5;
    private static final int ICON_SIZE = 16;
    private static final int PADDING_X = 12;
    private static final int ICON_TEXT_SPACING = 8;

    private final Component title;
    private final Component description;
    private final Identifier icon;
    private final Runnable onClickAction;

    private int lastX;
    private int lastY;
    private int lastWidth;

    /**
     * Creates a new category card instance with display information and click behavior.
     *
     * @param title         The primary title text displayed on the card.
     * @param description   The secondary descriptive text displayed below the title.
     * @param icon          The texture identifier used as the category icon.
     * @param onClickAction The action executed when the card is clicked.
     */
    public CategoryCard(
        String title,
        String description,
        Identifier icon,
        Runnable onClickAction
    ) {
        this.title = Component.literal(title);
        this.description = Component.literal(description);
        this.icon = icon;
        this.onClickAction = onClickAction;
    }

    /**
     * Returns the fixed vertical size occupied by the category card component.
     *
     * @return The total card height in pixels.
     */
    @Override
    public int getHeight() {
        return FIXED_CARD_HEIGHT;
    }

    /**
     * Renders the complete category card including background, icon, text content
     * and hover state visual effects.
     *
     * @param graphics The graphical rendering extraction context.
     * @param font The font renderer used for text drawing operations.
     * @param x The horizontal component position.
     * @param y The vertical component position.
     * @param width The available card width.
     * @param mouseX The current mouse X position.
     * @param mouseY The current mouse Y position.
     * @param progress The animation progress multiplier used for fade effects.
     */
    @Override
    public void render(
        GuiGraphicsExtractor graphics,
        Font font,
        int x,
        int y,
        int width,
        int mouseX,
        int mouseY,
        float progress
    ) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;

        boolean isHovered = isMouseOver(mouseX, mouseY, x, y, width);

        renderBackground(graphics, x, y, width, isHovered, progress);
        renderIcon(graphics, x, y, progress);
        renderText(graphics, font, x, y, width, isHovered, progress);
    }

    /**
     * Processes mouse click interactions and executes the assigned action
     * when the card receives a valid left-click event.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param button The mouse button identifier.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The component width.
     * @return True if the click was handled by this component.
     */
    @Override
    public boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button,
        int x,
        int y,
        int width
    ) {
        if (button == 0 && isMouseOver(mouseX, mouseY, x, y, width)) {
            if (this.onClickAction != null) {
                onClickAction.run();
            }

            return true;
        }

        return false;
    }

    /**
     * Renders the card background surface and border styling while applying
     * hover-state visual feedback and transparency animations.
     *
     * @param graphics The graphical rendering extraction context.
     * @param x The card X position.
     * @param y The card Y position.
     * @param width The card width.
     * @param hovered Whether the card is currently hovered by the cursor.
     * @param progress The animation progress factor.
     */
    private void renderBackground(
        GuiGraphicsExtractor graphics,
        int x,
        int y,
        int width,
        boolean hovered,
        float progress
    ) {
        int color = getAlphaColor(0x1E1F25, 200 * progress / 255);

        fillRoundedRect(
            graphics,
            x,
            y,
            x + width,
            y + FIXED_CARD_HEIGHT,
            color
        );

        int borderColor = getAlphaColor(
            hovered ? 0xB71C1C : 0x444444,
            progress
        );

        renderBorder(
            graphics,
            x,
            y,
            width,
            FIXED_CARD_HEIGHT,
            borderColor
        );
    }

    /**
     * Renders the category icon centered vertically within the card layout.
     * Displays a placeholder rectangle when no icon is available.
     *
     * @param graphics The graphical rendering extraction context.
     * @param x The card X position.
     * @param y The card Y position.
     * @param progress The animation progress factor.
     */
    private void renderIcon(
        GuiGraphicsExtractor graphics,
        int x,
        int y,
        float progress
    ) {
        int iconX = x + PADDING_X;
        int iconY = y + (FIXED_CARD_HEIGHT / 2) - (ICON_SIZE / 2);

        if (this.icon != null) {
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                this.icon,
                iconX,
                iconY,
                0,
                0,
                ICON_SIZE,
                ICON_SIZE,
                ICON_SIZE,
                ICON_SIZE
            );
        } else {
            graphics.fill(
                iconX,
                iconY,
                iconX + ICON_SIZE,
                iconY + ICON_SIZE,
                getAlphaColor(0xAAAAAA, progress)
            );
        }
    }

    /**
     * Renders the title, description and navigation arrow while handling
     * scaling, multiline text wrapping and hover color transitions.
     *
     * @param graphics The graphical rendering extraction context.
     * @param font The font renderer used for text drawing operations.
     * @param x The card X position.
     * @param y The card Y position.
     * @param width The card width.
     * @param isHovered Whether the card is currently hovered.
     * @param progress The animation progress factor.
     */
    private void renderText(
        GuiGraphicsExtractor graphics,
        Font font,
        int x,
        int y,
        int width,
        boolean isHovered,
        float progress
    ) {
        float descScale = 0.92f;
        int arrowWidth = font.width("->");

        int titleX = x + PADDING_X + ICON_SIZE + ICON_TEXT_SPACING;
        int usedWidth = (PADDING_X * 2) + ICON_SIZE + ICON_TEXT_SPACING + arrowWidth;
        int descMaxWidth = (int) ((width - usedWidth) / descScale);

        List<FormattedCharSequence> lines = font.split(
            this.description,
            descMaxWidth
        );

        int titleHeight = font.lineHeight;
        int descHeight = (int) ((lines.size() * (font.lineHeight + 2)) * descScale);
        int totalContentHeight = titleHeight + VERTICAL_GAP + descHeight;

        int textBlockY = y + (FIXED_CARD_HEIGHT - totalContentHeight) / 2;

        graphics.text(
            font,
            this.title,
            titleX,
            textBlockY,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        float invScale = 1.0f / descScale;

        var pose = graphics.pose();
        pose.pushMatrix();
        pose.scale(descScale, descScale);

        float descY = (textBlockY + titleHeight + VERTICAL_GAP) * invScale;

        for (FormattedCharSequence line : lines) {
            graphics.text(
                font,
                line,
                (int) (titleX * invScale),
                (int) descY,
                getAlphaColor(0xAAAAAA, progress * 0.7f),
                false
            );

            descY += (font.lineHeight + 2) * invScale;
        }

        pose.popMatrix();

        int arrowX = x + width - PADDING_X - arrowWidth;
        int arrowY = y + (FIXED_CARD_HEIGHT / 2) - (font.lineHeight / 2);

        int arrowColor = isHovered
            ? getAlphaColor(0xB71C1C, progress)
            : getAlphaColor(0xFFFFFF, progress);

        graphics.text(
            font,
            "->",
            arrowX,
            arrowY,
            arrowColor,
            false
        );
    }
}
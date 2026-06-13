package dev.wrrulosdev.mcpclient.client.screens.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SwitchOptionCard extends AbstractSettingComponent {

    private int height = 45;

    private static final int VERTICAL_GAP = 5;
    private static final int INNER_PADDING = 15;
    private static final int TEXTURE_SPACING = 10;
    private static final int SWITCH_WIDTH = 24;
    private static final int SWITCH_HEIGHT = 20;
    private static final int SETTINGS_ICON_SIZE = 16;

    private final Component title;
    private final Component description;
    private final Identifier textureOn;
    private final Identifier textureOff;
    private final Identifier settingsIcon;
    private final Identifier settingsHoverIcon;
    private final Consumer<Boolean> onToggle;
    private final Supplier<Boolean> stateSupplier;
    private final Runnable onSettingsClick;

    private int lastX;
    private int lastY;
    private int lastWidth;

    /**
     * Creates a new switch option card with toggle controls, descriptive content
     * and optional settings interaction support.
     *
     * @param title The primary feature name displayed on the card.
     * @param description The descriptive text explaining the feature.
     * @param stateSupplier Supplies the current enabled or disabled state.
     * @param textureOn The texture displayed when the feature is enabled.
     * @param textureOff The texture displayed when the feature is disabled.
     * @param settingsIcon The default settings button texture.
     * @param settingsHoverIcon The settings button texture shown while hovered.
     * @param onToggle The callback invoked when the switch state changes.
     * @param onSettingsClick The callback invoked when the settings button is clicked.
     */
    public SwitchOptionCard(
        String title,
        String description,
        Supplier<Boolean> stateSupplier,
        Identifier textureOn,
        Identifier textureOff,
        Identifier settingsIcon,
        Identifier settingsHoverIcon,
        Consumer<Boolean> onToggle,
        Runnable onSettingsClick
    ) {
        this.title = Component.literal(title);
        this.description = Component.literal(description);
        this.stateSupplier = stateSupplier;
        this.textureOn = textureOn;
        this.textureOff = textureOff;
        this.settingsIcon = settingsIcon;
        this.settingsHoverIcon = settingsHoverIcon;
        this.onToggle = onToggle;
        this.onSettingsClick = onSettingsClick;
    }

    /**
     * Updates the vertical size of the card component.
     *
     * @param height The new component height in pixels.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns the current height occupied by the card.
     *
     * @return The component height in pixels.
     */
    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Renders the complete switch option card including background,
     * descriptive text and interactive control elements.
     *
     * @param graphics The graphical rendering extraction context.
     * @param font The font renderer used for text drawing.
     * @param x The horizontal component position.
     * @param y The vertical component position.
     * @param width The available component width.
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     * @param progress The animation progress factor used for opacity effects.
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

        renderBackground(
            graphics,
            x,
            y,
            width,
            isHovered,
            progress
        );

        renderText(
            graphics,
            font,
            x,
            y,
            width,
            progress
        );

        renderControls(
            graphics,
            x,
            y,
            width,
            progress,
            mouseX,
            mouseY
        );
    }

    /**
     * Handles mouse click interactions for both the toggle switch and
     * optional settings button contained within the card.
     *
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @param button The pressed mouse button identifier.
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
        if (button != 0 || !isMouseOver(mouseX, mouseY, x, y, width)) {
            return false;
        }

        int switchX = x + width - INNER_PADDING - SWITCH_WIDTH;
        int settingsX = switchX - TEXTURE_SPACING - SETTINGS_ICON_SIZE;

        if (mouseX >= settingsX && mouseX <= settingsX + SETTINGS_ICON_SIZE) {
            if (onSettingsClick != null) {
                onSettingsClick.run();
            }
        } else {
            boolean newState = !this.stateSupplier.get();

            if (this.onToggle != null) {
                this.onToggle.accept(newState);
            }
        }

        return true;
    }

    /**
     * Renders the card background and border styling according to
     * the current toggle state and hover state.
     *
     * @param graphics The graphical rendering extraction context.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The component width.
     * @param hovered Whether the card is currently hovered.
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
        boolean currentState = this.stateSupplier.get();

        int baseColor = currentState
            ? 0x1E1F25
            : 0x101115;

        int color = getAlphaColor(
            baseColor,
            200 * progress / 255
        );

        fillRoundedRect(
            graphics,
            x,
            y,
            x + width,
            y + this.height,
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
            this.height,
            borderColor
        );
    }

    /**
     * Renders the title and description text while automatically handling
     * clipping, scaling and line limiting for layout consistency.
     *
     * @param graphics The graphical rendering extraction context.
     * @param font The font renderer used for text drawing.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The available component width.
     * @param progress The animation progress factor.
     */
    private void renderText(
        GuiGraphicsExtractor graphics,
        Font font,
        int x,
        int y,
        int width,
        float progress
    ) {
        float descScale = 0.92f;

        int titleX = x + INNER_PADDING;

        int reservedRightSpace =
            INNER_PADDING +
                SWITCH_WIDTH +
                TEXTURE_SPACING +
                SETTINGS_ICON_SIZE +
                TEXTURE_SPACING;

        int maxTextWidth = Math.max(
            0,
            width - INNER_PADDING - reservedRightSpace
        );

        String titleStr = this.title.getString();

        if (font.width(titleStr) > maxTextWidth) {
            titleStr =
                font.plainSubstrByWidth(
                    titleStr,
                    maxTextWidth - font.width("...")
                ) + "...";
        }

        int descMaxWidth = (int) (maxTextWidth / descScale);

        List<FormattedCharSequence> lines =
            font.split(this.description, descMaxWidth);

        if (lines.size() > 2) {
            lines = lines.subList(0, 2);
        }

        int titleHeight = font.lineHeight;

        int descHeight =
            (int) ((lines.size() * (font.lineHeight + 2)) * descScale);

        int totalContentHeight =
            titleHeight +
                (lines.isEmpty() ? 0 : VERTICAL_GAP) +
                descHeight;

        int textBlockY =
            y + (this.height - totalContentHeight) / 2;

        graphics.text(
            font,
            Component.literal(titleStr),
            titleX,
            textBlockY,
            getAlphaColor(0xFFFFFF, progress),
            false
        );

        if (!lines.isEmpty()) {
            float invScale = 1.0f / descScale;

            var pose = graphics.pose();

            pose.pushMatrix();
            pose.scale(descScale, descScale);

            float descY =
                (textBlockY + titleHeight + VERTICAL_GAP)
                    * invScale;

            for (FormattedCharSequence line : lines) {
                graphics.text(
                    font,
                    line,
                    (int) (titleX * invScale),
                    (int) descY,
                    getAlphaColor(0xAAAAAA, progress * 0.7f),
                    false
                );

                descY +=
                    (font.lineHeight + 2) * invScale;
            }

            pose.popMatrix();
        }
    }

    /**
     * Renders the switch toggle and optional settings icon while applying
     * hover-sensitive visual states and texture selection.
     *
     * @param graphics The graphical rendering extraction context.
     * @param x The component X position.
     * @param y The component Y position.
     * @param width The available component width.
     * @param progress The animation progress factor.
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     */
    private void renderControls(
        GuiGraphicsExtractor graphics,
        int x,
        int y,
        int width,
        float progress,
        int mouseX,
        int mouseY
    ) {
        int switchX =
            x + width - INNER_PADDING - SWITCH_WIDTH;

        int centerY =
            y + (this.height / 2);

        boolean currentState =
            this.stateSupplier.get();

        Identifier currentTexture =
            currentState
                ? this.textureOn
                : this.textureOff;

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            currentTexture,
            switchX,
            centerY - (SWITCH_HEIGHT / 2),
            0,
            0,
            SWITCH_WIDTH,
            SWITCH_HEIGHT,
            SWITCH_WIDTH,
            SWITCH_HEIGHT
        );

        if (settingsIcon != null) {
            int settingsX =
                switchX - TEXTURE_SPACING - SETTINGS_ICON_SIZE;

            int settingsY =
                centerY - (SETTINGS_ICON_SIZE / 2);

            boolean isSettingsHovered =
                mouseX >= settingsX
                    && mouseX <= settingsX + SETTINGS_ICON_SIZE
                    && mouseY >= settingsY
                    && mouseY <= settingsY + SETTINGS_ICON_SIZE;

            Identifier iconToRender =
                (isSettingsHovered && settingsHoverIcon != null)
                    ? settingsHoverIcon
                    : settingsIcon;

            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                iconToRender,
                settingsX,
                settingsY,
                0,
                0,
                SETTINGS_ICON_SIZE,
                SETTINGS_ICON_SIZE,
                SETTINGS_ICON_SIZE,
                SETTINGS_ICON_SIZE
            );
        }
    }
}
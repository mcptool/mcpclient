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

public class SwitchOptionCard {

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

    private int lastX, lastY, lastWidth;

    public SwitchOptionCard(String title, String description, Supplier<Boolean> stateSupplier,
                            Identifier textureOn, Identifier textureOff,
                            Identifier settingsIcon, Identifier settingsHoverIcon,
                            Consumer<Boolean> onToggle, Runnable onSettingsClick) {
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int render(GuiGraphicsExtractor graphics, Font font, int x, int y, int width,
                      float rawProgress, int mouseX, int mouseY) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;

        boolean isHovered = isMouseOver(mouseX, mouseY);

        renderBackground(graphics, x, y, width, isHovered, rawProgress);
        renderText(graphics, font, x, y, width, rawProgress);
        renderControls(graphics, x, y, width, rawProgress, mouseX, mouseY);

        return this.height;
    }

    private void renderBackground(GuiGraphicsExtractor graphics, int x, int y, int width,
                                  boolean hovered, float progress) {
        // Lee el estado directo de los ajustes, si es true usa un color, si es false otro
        boolean currentState = this.stateSupplier.get();
        int baseColor = currentState ? 0x1E1F25 : 0x101115;

        int color = getAlphaColor(baseColor, 200 * progress / 255);
        fillRoundedRect(graphics, x, y, x + width, y + this.height, color);

        int borderColor = getAlphaColor(hovered ? 0xB71C1C : 0x444444, progress);
        renderBorder(graphics, x, y, width, this.height, borderColor);
    }

    private void renderText(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, float progress) {
        float descScale = 0.92f;
        int titleX = x + INNER_PADDING;
        int reservedRightSpace = INNER_PADDING + SWITCH_WIDTH + TEXTURE_SPACING + SETTINGS_ICON_SIZE + TEXTURE_SPACING;
        int maxTextWidth = Math.max(0, width - INNER_PADDING - reservedRightSpace);

        String titleStr = this.title.getString();
        if (font.width(titleStr) > maxTextWidth) {
            titleStr = font.plainSubstrByWidth(titleStr, maxTextWidth - font.width("...")) + "...";
        }

        int descMaxWidth = (int) (maxTextWidth / descScale);
        List<FormattedCharSequence> lines = font.split(this.description, descMaxWidth);
        if (lines.size() > 2) {
            lines = lines.subList(0, 2);
        }

        int titleHeight = font.lineHeight;
        int descHeight = (int) ((lines.size() * (font.lineHeight + 2)) * descScale);
        int totalContentHeight = titleHeight + (lines.isEmpty() ? 0 : VERTICAL_GAP) + descHeight;
        int textBlockY = y + (this.height - totalContentHeight) / 2;

        graphics.text(font, Component.literal(titleStr), titleX, textBlockY, getAlphaColor(0xFFFFFF, progress), false);

        if (!lines.isEmpty()) {
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
        }
    }

    private void renderControls(GuiGraphicsExtractor graphics, int x, int y, int width, float progress, int mouseX, int mouseY) {
        int switchX = x + width - INNER_PADDING - SWITCH_WIDTH;
        int centerY = y + (this.height / 2);

        // Si stateSupplier.get() es true, renderiza textureOn, si es false renderiza textureOff.
        boolean currentState = this.stateSupplier.get();
        Identifier currentTexture = currentState ? this.textureOn : this.textureOff;

        graphics.blit(RenderPipelines.GUI_TEXTURED, currentTexture, switchX, centerY - (SWITCH_HEIGHT / 2), 0, 0, SWITCH_WIDTH, SWITCH_HEIGHT, SWITCH_WIDTH, SWITCH_HEIGHT);

        if (settingsIcon != null) {
            int settingsX = switchX - TEXTURE_SPACING - SETTINGS_ICON_SIZE;
            int settingsY = centerY - (SETTINGS_ICON_SIZE / 2);

            boolean isSettingsHovered = mouseX >= settingsX && mouseX <= settingsX + SETTINGS_ICON_SIZE &&
                mouseY >= settingsY && mouseY <= settingsY + SETTINGS_ICON_SIZE;

            Identifier iconToRender = (isSettingsHovered && settingsHoverIcon != null) ? settingsHoverIcon : settingsIcon;

            graphics.blit(RenderPipelines.GUI_TEXTURED, iconToRender, settingsX, settingsY, 0, 0, SETTINGS_ICON_SIZE, SETTINGS_ICON_SIZE, SETTINGS_ICON_SIZE, SETTINGS_ICON_SIZE);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 || !isMouseOver((int) mouseX, (int) mouseY)) {
            return false;
        }

        int switchX = lastX + lastWidth - INNER_PADDING - SWITCH_WIDTH;
        int settingsX = switchX - TEXTURE_SPACING - SETTINGS_ICON_SIZE;

        if (mouseX >= settingsX && mouseX <= settingsX + SETTINGS_ICON_SIZE) {
            if (onSettingsClick != null) {
                onSettingsClick.run();
            }
        } else {
            // Invertimos el estado actual y se lo mandamos al consumer
            boolean newState = !this.stateSupplier.get();
            if (this.onToggle != null) {
                this.onToggle.accept(newState);
            }
        }
        return true;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= lastX && mouseX <= lastX + lastWidth
            && mouseY >= lastY && mouseY <= lastY + this.height;
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
        int x2 = x + width;
        int y2 = y + height;
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
package dev.wrrulosdev.mcpclient.client.mixins.screen.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class CategoryCard {
    private final Component title;
    private final Component description;

    public CategoryCard(String title, String description) {
        this.title = Component.literal(title);
        this.description = Component.literal(description);
    }

    /**
     * Dibuja la tarjeta y retorna el ALTO TOTAL utilizado.
     * Retornar el alto es muy útil por si luego quieres poner otra tarjeta justo debajo.
     */
    public int render(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, float rawProgress) {
        int iconSize = 16;
        int padding = 5;

        // --- 1. PRE-CÁLCULO DEL TEXTO ---
        int arrowWidth = font.width("->");
        // El ancho máximo del texto es el ancho total de la tarjeta, menos los márgenes, menos la flecha
        int descMaxWidth = width - (padding * 2) - arrowWidth - 5;

        // Dividimos el texto en líneas ANTES de dibujar nada
        List<FormattedCharSequence> lines = font.split(this.description, descMaxWidth);
        // Cada línea ocupa su alto + 2 píxeles de separación
        int textHeight = lines.size() * (font.lineHeight + 2);

        // --- 2. CÁLCULO DEL ALTO DINÁMICO ---
        // Alto total = padding superior + icono + espacio + alto del texto + padding inferior
        int cardHeight = padding + iconSize + 4 + textHeight + padding;

        // --- 3. DIBUJAR FONDO ---
        int cardColor = (((int) (80 * rawProgress) << 24) | 0x222222);
        fillRoundedRect(graphics, x, y, x + width, y + cardHeight, cardColor);

        // --- 4. DIBUJAR ÍCONO ---
        int iconX = x + padding;
        int iconY = y + padding;
        int iconColor = (((int) (255 * rawProgress) << 24) | 0xAAAAAA);
        graphics.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, iconColor);

        // --- 5. DIBUJAR TÍTULO ---
        int textColor = (((int) (255 * rawProgress) << 24) | 0xFFFFFF);
        int titleX = iconX + iconSize + 4;
        int titleY = iconY + (iconSize / 2) - (font.lineHeight / 2);
        graphics.text(font, this.title, titleX, titleY, textColor, false);

        // --- 6. DIBUJAR FLECHA ---
        int arrowX = x + width - padding - arrowWidth;
        // Centramos la flecha verticalmente respecto a toda la tarjeta
        int arrowY = y + (cardHeight / 2) - (font.lineHeight / 2);
        graphics.text(font, Component.literal("->"), arrowX, arrowY, textColor, false);

        // --- 7. DIBUJAR DESCRIPCIÓN (Multi-línea) ---
        int descX = iconX;
        int currentDescY = iconY + iconSize + 4;
        int descColor = (((int) (180 * rawProgress) << 24) | 0xCCCCCC);

        for (FormattedCharSequence line : lines) {
            graphics.text(font, line, descX, currentDescY, descColor, false);
            currentDescY += font.lineHeight + 2;
        }

        return cardHeight;
    }

    // Copiamos el método aquí para que el componente sea 100% independiente
    private void fillRoundedRect(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int color) {
        graphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, color);
        graphics.fill(x1 + 2, y1, x2 - 2, y1 + 1, color);
        graphics.fill(x1 + 2, y2 - 1, x2 - 2, y2, color);
        graphics.fill(x1, y1 + 2, x1 + 1, y2 - 2, color);
        graphics.fill(x2 - 1, y1 + 2, x2, y2 - 2, color);
    }
}
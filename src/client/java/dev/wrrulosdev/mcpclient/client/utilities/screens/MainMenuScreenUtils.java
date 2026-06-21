package dev.wrrulosdev.mcpclient.client.utilities.screens;

import dev.wrrulosdev.mcpclient.client.screens.gui.SwitchOptionCard;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.List;

public class MainMenuScreenUtils {

    /**
     * Renders the generic grid content within a scrollable area in the main menu.
     *
     * @param graphics The graphics utility for rendering
     * @param x1 The start X-coordinate of the container
     * @param x2 The end X-coordinate of the container
     * @param y1 The start Y-coordinate of the container
     * @param y2 The end Y-coordinate of the container
     * @param mouseX The current X-coordinate of the mouse
     * @param mouseY The current Y-coordinate of the mouse
     * @param progress The animation or interpolation progress
     * @param font The font renderer to use
     * @param maxScrollOffset The maximum allowable scroll distance
     * @param scrollOffset The current scroll offset
     * @param cardHeight The height of an individual option card
     * @param sideMargin The horizontal margin inside the container
     * @param gap The spacing between grid elements
     * @param allCards The list of option cards to render
     * @return The updated maximum scroll offset
     */
    public static double renderWindowGenericContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress, Font font, double maxScrollOffset, double scrollOffset, int cardHeight, int sideMargin, int gap, List<SwitchOptionCard> allCards) {
        int listY1 = y1 + 55;
        int listY2 = y2 - 10;
        int columns = 3;
        int colWidth = calculateColWidth(x1, x2, columns, sideMargin, gap);
        int numRows = (int) Math.ceil(allCards.size() / (double) columns);
        maxScrollOffset = Math.max(
            0,
            (numRows * cardHeight)
                + ((numRows - 1) * gap)
                - (listY2 - listY1)
        );

        graphics.enableScissor(x1, listY1, x2, listY2);

        for (int i = 0; i < allCards.size(); i++) {
            int[] pos =
                calculateCardPosition(
                    i,
                    x1,
                    listY1,
                    colWidth,
                    columns,
                    cardHeight,
                    sideMargin,
                    gap,
                    scrollOffset
                );

            if (
                pos[1] + cardHeight >= listY1
                    && pos[1] <= listY2
            ) {
                allCards.get(i).render(
                    graphics,
                    font,
                    pos[0],
                    pos[1],
                    colWidth,
                    mouseX,
                    mouseY,
                    progress
                );
            }
        }

        graphics.disableScissor();
        return maxScrollOffset;
    }

    /**
     * Checks if the mouse cursor is hovering over any card within the grid.
     *
     * @param mouseX The mouse X-coordinate
     * @param mouseY The mouse Y-coordinate
     * @param screenWidth The total screen width
     * @param screenHeight The total screen height
     * @param maxWidth The maximum width of the window
     * @param maxHeight The maximum height of the window
     * @param cardHeight The height of a single card
     * @param totalCards Total number of cards
     * @param columns Number of columns in the grid
     * @param sideMargin Margin on the sides
     * @param gap The gap between elements
     * @param scrollOffset The current vertical scroll position
     * @return True if the mouse is hovering over a card, false otherwise
     */
    public static boolean isMouseOverGrid(
        double mouseX,
        double mouseY,
        int screenWidth,
        int screenHeight,
        int maxWidth,
        int maxHeight,
        int cardHeight,
        int totalCards,
        int columns,
        int sideMargin,
        int gap,
        int scrollOffset
    ) {
        int targetWidth = Math.min(screenWidth - 60, maxWidth);
        int targetHeight = Math.min(screenHeight - 60, maxHeight);
        int x1 = (screenWidth / 2) - (targetWidth / 2);
        int x2 = (screenWidth / 2) + (targetWidth / 2);
        int y1 = (screenHeight / 2) - (targetHeight / 2);

        int listY1 = y1 + 55;
        int listY2 = y1 + targetHeight - 10;
        int colWidth = calculateColWidth(x1, x2, columns, sideMargin, gap);

        if (mouseY >= listY1 && mouseY <= listY2) {
            for (int i = 0; i < totalCards; i++) {
                int[] pos = calculateCardPosition(i, x1, listY1, colWidth, columns, cardHeight, sideMargin, gap, scrollOffset);

                if (mouseX >= pos[0] && mouseX <= pos[0] + colWidth &&
                    mouseY >= pos[1] && mouseY <= pos[1] + cardHeight) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Handles mouse click events within the grid, delegating to the appropriate card.
     *
     * @param mouseX The mouse X-coordinate
     * @param mouseY The mouse Y-coordinate
     * @param mouseButton The button that was clicked
     * @param cards List of available cards
     * @param screenWidth The total screen width
     * @param screenHeight The total screen height
     * @param maxWidth The maximum window width
     * @param maxHeight The maximum window height
     * @param cardHeight The height of a single card
     * @param columns Number of columns
     * @param sideMargin Margin on the sides
     * @param gap The gap between elements
     * @param scrollOffset The current vertical scroll position
     * @return True if a card was clicked, false otherwise
     */
    public static boolean handleGridClick(
        double mouseX,
        double mouseY,
        int mouseButton,
        List<SwitchOptionCard> cards,
        int screenWidth,
        int screenHeight,
        int maxWidth,
        int maxHeight,
        int cardHeight,
        int columns,
        int sideMargin,
        int gap,
        int scrollOffset
    ) {
        int targetWidth = Math.min(screenWidth - 60, maxWidth);
        int targetHeight = Math.min(screenHeight - 60, maxHeight);
        int x1 = (screenWidth / 2) - (targetWidth / 2);
        int x2 = (screenWidth / 2) + (targetWidth / 2);
        int y1 = (screenHeight / 2) - (targetHeight / 2);

        int listY1 = y1 + 55;
        int listY2 = y1 + targetHeight - 10;
        int colWidth = calculateColWidth(x1, x2, columns, sideMargin, gap);

        if (mouseY >= listY1 && mouseY <= listY2) {
            for (int i = 0; i < cards.size(); i++) {
                int[] pos = calculateCardPosition(i, x1, listY1, colWidth, columns, cardHeight, sideMargin, gap, scrollOffset);

                if (cards.get(i).mouseClicked(mouseX, mouseY, mouseButton, pos[0], pos[1], colWidth)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Calculates the new scroll offset based on user input, clamped to valid boundaries.
     *
     * @param currentOffset The current scroll offset
     * @param scrollY The change in scroll (e.g., from a mouse wheel)
     * @param maxScrollOffset The maximum permitted scroll
     * @return The updated, clamped scroll offset
     */
    public static double calculateScrollOffset(double currentOffset, double scrollY, double maxScrollOffset) {
        return Math.clamp(
            currentOffset - scrollY * 20,
            0,
            maxScrollOffset
        );
    }

    /**
     * Calculates the width of a single column within the grid layout.
     *
     * @param x1 The start X-coordinate of the container
     * @param x2 The end X-coordinate of the container
     * @param columns The number of columns
     * @param sideMargin The horizontal margin
     * @param gap The spacing between columns
     * @return The calculated column width
     */
    private static int calculateColWidth(
        int x1,
        int x2,
        int columns,
        int sideMargin,
        int gap
    ) {
        return (
            (x2 - x1)
                - (sideMargin * 2)
                - ((columns - 1) * gap)
        ) / columns;
    }

    /**
     * Calculates the absolute screen position (X, Y) for a card at a given index.
     *
     * @param i The index of the card
     * @param x1 The start X-coordinate
     * @param listY1 The start Y-coordinate of the list area
     * @param colWidth The width of one column
     * @param columns The number of columns
     * @param cardHeight The height of a single card
     * @param sideMargin The side margin
     * @param gap The spacing between cards
     * @param scrollOffset The current scroll position
     * @return An array containing [X, Y] coordinates
     */
    private static int[] calculateCardPosition(
        int i,
        int x1,
        int listY1,
        int colWidth,
        int columns,
        int cardHeight,
        int sideMargin,
        int gap,
        double scrollOffset
    ) {
        int row = i / columns;
        int col = i % columns;
        int cardX = x1 + sideMargin + (col * (colWidth + gap));
        int cardY = listY1 + (row * (cardHeight + gap)) - (int) scrollOffset;
        return new int[]{cardX, cardY};
    }
}

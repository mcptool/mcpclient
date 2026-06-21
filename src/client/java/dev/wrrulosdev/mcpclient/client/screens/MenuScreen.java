package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.screens.gui.CategoryCard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends BaseAnimatedScreen {

    private final List<CategoryCard> allCards = new ArrayList<>();
    private static final int CARD_HEIGHT = 50;
    private static final int GAP = 3;
    private double scrollOffset = 0;
    private double maxScrollOffset = 0;

    /**
     * Creates the main menu screen instance.
     */
    public MenuScreen() {
        super(Component.empty());
    }

    /**
     * Initializes the screen and loads all available category cards.
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    /**
     * Populates the category list with all available menu entries and
     * their associated navigation actions.
     */
    private void loadCards() {
        this.allCards.clear();

        this.allCards.add(
            new CategoryCard(
                "Cheats",
                "Basic minecraft cheats.",
                TextureConstants.HACKER_ICON,
                () -> Minecraft.getInstance().setScreenAndShow(new CheatsScreen(this))
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Information",
                "Check server and player info.",
                TextureConstants.SEO_ICON,
                () -> System.out.println("Info")
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Exploits",
                "List of available exploits.",
                TextureConstants.BOMB_ICON,
                () -> Minecraft.getInstance().setScreenAndShow(new ExploitsScreen(this))
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Password Tracker",
                "View player passwords.",
                TextureConstants.USER_KEY_ICON,
                () -> System.out.println("Passwords")
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Griefing Utilities",
                "Automations for griefing.",
                TextureConstants.MALWARE1_ICON,
                () -> System.out.println("Utilities")
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Client Settings",
                "Configure client options.",
                TextureConstants.SIMPLE_RED_SETTINGS_ICON,
                () -> Minecraft.getInstance().setScreenAndShow(new ClientSettingsScreen(this))
            )
        );

        this.allCards.add(
            new CategoryCard(
                "Privacy",
                "User privacy settings.",
                TextureConstants.PADLOCK_ICON,
                () -> System.out.println("Blatant")
            )
        );

        this.allCards.add(
            new CategoryCard(
                "MCPTool Connection",
                "Connect your MCPTool.",
                TextureConstants.CONNECTION_ICON,
                () -> System.out.println("Blatant")
            )
        );
    }

    /**
     * Returns the title displayed in the window header.
     *
     * @return The screen title text.
     */
    @Override
    protected String getWindowTitle() {
        return "MCPClient";
    }

    /**
     * Renders the category card grid, handles clipping and calculates
     * scrolling limits based on the current content size.
     *
     * @param graphics The graphical rendering extraction context.
     * @param x1 The left window boundary.
     * @param x2 The right window boundary.
     * @param y1 The top window boundary.
     * @param y2 The bottom window boundary.
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     * @param progress The animation progress factor.
     */
    @Override
    protected void renderWindowContent(
        GuiGraphicsExtractor graphics,
        int x1,
        int x2,
        int y1,
        int y2,
        int mouseX,
        int mouseY,
        float progress
    ) {
        int listY1 = y1 + 50;
        int listY2 = y2 - 10;
        int viewableHeight = listY2 - listY1;
        int colWidth = ((x2 - x1) - 30) / 2;
        int numRows = (int) Math.ceil(allCards.size() / 2.0);
        int totalHeight = (numRows * CARD_HEIGHT) + ((numRows - 1) * GAP);
        this.maxScrollOffset = Math.max(0, totalHeight - viewableHeight);
        graphics.enableScissor(x1, listY1, x2, listY2);

        for (int i = 0; i < allCards.size(); i++) {
            int[] pos = calculateCardPosition(i, x1, listY1, colWidth);

            int cardX = pos[0];
            int cardY = pos[1];

            if (cardY + CARD_HEIGHT >= listY1 && cardY <= listY2) {
                allCards.get(i).render(
                    graphics,
                    this.font,
                    cardX,
                    cardY,
                    colWidth,
                    mouseX,
                    mouseY,
                    progress
                );
            }
        }

        graphics.disableScissor();
    }

    /**
     * Updates the cursor appearance when hovering interactive category cards.
     *
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     */
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        int targetWidth = Math.min(this.width - 60, this.maxWidth);
        int targetHeight = Math.min(this.height - 60, this.maxHeight);
        int x1 = (this.width / 2) - (targetWidth / 2);
        int x2 = (this.width / 2) + (targetWidth / 2);
        int y1 = (this.height / 2) - (targetHeight / 2);

        int listY1 = y1 + 50;
        int listY2 = y1 + targetHeight - 10;
        int colWidth = ((x2 - x1) - 30) / 2;

        boolean hovered = false;

        if (mouseY >= listY1 && mouseY <= listY2) {
            for (int i = 0; i < allCards.size(); i++) {
                int[] pos = calculateCardPosition(i, x1, listY1, colWidth);

                if (mouseX >= pos[0] && mouseX <= pos[0] + colWidth &&
                    mouseY >= pos[1] && mouseY <= pos[1] + CARD_HEIGHT) {
                    hovered = true;
                    break;
                }
            }
        }

        GLFW.glfwSetCursor(
            Minecraft.getInstance().getWindow().handle(),
            hovered ? HAND_CURSOR : ARROW_CURSOR
        );
    }

    /**
     * Processes mouse click interactions and forwards click events to the
     * corresponding category card under the cursor.
     *
     * @param event The mouse button event information.
     * @param doubleClick Indicates whether the click is part of a double click.
     * @return True if a category card consumed the click event.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int targetWidth = Math.min(this.width - 60, this.maxWidth);
        int targetHeight = Math.min(this.height - 60, this.maxHeight);
        int x1 = (this.width / 2) - (targetWidth / 2);
        int x2 = (this.width / 2) + (targetWidth / 2);
        int y1 = (this.height / 2) - (targetHeight / 2);

        int listY1 = y1 + 50;
        int listY2 = y1 + targetHeight - 10;
        int colWidth = ((x2 - x1) - 30) / 2;

        if (event.y() >= listY1 && event.y() <= listY2) {
            for (int i = 0; i < allCards.size(); i++) {
                int[] pos = calculateCardPosition(i, x1, listY1, colWidth);

                if (allCards.get(i).mouseClicked(event.x(), event.y(), event.button(), pos[0], pos[1], colWidth)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    /**
     * Calculates the on-screen position of a category card based on its
     * index, row placement and current scroll offset.
     *
     * @param i The card index within the collection.
     * @param x1 The left window boundary.
     * @param listY1 The top content boundary.
     * @param colWidth The width of a single card column.
     * @return An array containing the calculated X and Y coordinates.
     */
    private int[] calculateCardPosition(
        int i,
        int x1,
        int listY1,
        int colWidth
    ) {
        int row = i / 2;

        int cardX = (i % 2 != 0)
            ? (x1 + 10 + colWidth + 10)
            : (x1 + 10);

        int cardY = listY1
            + (row * (CARD_HEIGHT + GAP))
            - (int) scrollOffset;

        return new int[]{cardX, cardY};
    }

    /**
     * Handles mouse wheel scrolling and updates the content offset while
     * keeping the scroll position inside valid bounds.
     *
     * @param mouseX The current mouse X coordinate.
     * @param mouseY The current mouse Y coordinate.
     * @param scrollX The horizontal scroll amount.
     * @param scrollY The vertical scroll amount.
     * @return The result of the parent scroll handler.
     */
    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double scrollX,
        double scrollY
    ) {
        this.scrollOffset = Math.clamp(
            this.scrollOffset - scrollY * 20,
            0,
            this.maxScrollOffset
        );

        return super.mouseScrolled(
            mouseX,
            mouseY,
            scrollX,
            scrollY
        );
    }
}


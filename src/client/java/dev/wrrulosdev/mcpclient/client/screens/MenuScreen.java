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
     * Constructs the main menu screen
     */
    public MenuScreen() {
        super(Component.empty());
    }

    /**
     * Initializes the screen and loads all category cards
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    /**
     * Loads and initializes all category cards displayed in the menu
     * including navigation callbacks and visual configuration
     */
    private void loadCards() {
        this.allCards.clear();

        this.allCards.add(new CategoryCard(
            "Cheats",
            "Basic minecraft cheats.",
            TextureConstants.HACKER_ICON,
            () -> Minecraft.getInstance().setScreen(new CheatsScreen(this))
        ));

        this.allCards.add(new CategoryCard(
            "Information",
            "Check server and player info.",
            TextureConstants.SEO_ICON,
            () -> System.out.println("Info")
        ));

        this.allCards.add(new CategoryCard(
            "Exploits",
            "List of available exploits.",
            TextureConstants.BOMB_ICON,
            () -> Minecraft.getInstance().setScreen(new ExploitsScreen(this))
        ));

        this.allCards.add(new CategoryCard(
            "Password Tracker",
            "View player passwords.",
            TextureConstants.USER_KEY_ICON,
            () -> System.out.println("Passwords")
        ));

        this.allCards.add(new CategoryCard(
            "Griefing Utilities",
            "Automations for griefing.",
            TextureConstants.MALWARE1_ICON,
            () -> System.out.println("Utilities")
        ));

        this.allCards.add(new CategoryCard(
            "Client Settings",
            "Configure client options.",
            TextureConstants.SIMPLE_RED_SETTINGS_ICON,
            () -> System.out.println("Legit")
        ));

        this.allCards.add(new CategoryCard(
            "Privacy",
            "User privacy settings.",
            TextureConstants.PADLOCK_ICON,
            () -> System.out.println("Blatant")
        ));

        this.allCards.add(new CategoryCard(
            "MCPTool Connection",
            "Connect your MCPTool.",
            TextureConstants.CONNECTION_ICON,
            () -> System.out.println("Blatant")
        ));
    }

    /**
     * Returns the title displayed on the window header
     *
     * @return The screen title
     */
    @Override
    protected String getWindowTitle() {
        return "MCPClient";
    }

    /**
     * Renders the main content area of the screen including the scrollable card grid
     *
     * @param graphics Rendering context used to draw UI elements
     * @param x1 Left boundary of the content area
     * @param x2 Right boundary of the content area
     * @param y1 Top boundary of the content area
     * @param y2 Bottom boundary of the content area
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param progress Animation progress value for smooth rendering transitions
     */
    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        int listY1 = y1 + 50;
        int listY2 = y2 - 10;
        int viewableHeight = listY2 - listY1;

        int numRows = (int) Math.ceil(allCards.size() / 2.0);
        int totalHeight = (numRows * CARD_HEIGHT) + ((numRows - 1) * GAP);
        this.maxScrollOffset = Math.max(0, totalHeight - viewableHeight);

        graphics.enableScissor(x1, listY1, x2, listY2);
        int colWidth = ((x2 - x1) - 30) / 2;

        for (int i = 0; i < allCards.size(); i++) {
            int row = i / 2;
            int cardX = (i % 2 != 0) ? (x1 + 10 + colWidth + 10) : (x1 + 10);
            int cardY = listY1 + (row * (CARD_HEIGHT + GAP)) - (int) scrollOffset;

            if (cardY + CARD_HEIGHT >= listY1 && cardY <= listY2) {
                allCards.get(i).render(graphics, this.font, cardX, cardY, colWidth, progress, mouseX, mouseY);
            }
        }

        graphics.disableScissor();
    }

    /**
     * Updates cursor appearance based on whether the mouse is hovering interactive cards
     *
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     */
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        boolean hovered = allCards.stream().anyMatch(card -> card.isMouseOver((int) mouseX, (int) mouseY));

        GLFW.glfwSetCursor(
            Minecraft.getInstance().getWindow().handle(),
            hovered ? HAND_CURSOR : ARROW_CURSOR
        );
    }

    /**
     * Handles mouse click interactions for all category cards
     *
     * @param event Mouse button event containing position and button data
     * @param doubleClick Whether the click is a double click
     * @return true if a card handled the click, otherwise delegates to parent handling
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return allCards.stream()
            .anyMatch(card -> card.mouseClicked(event.x(), event.y(), event.button()))
            || super.mouseClicked(event, doubleClick);
    }

    /**
     * Handles mouse scroll input and updates the vertical scroll offset
     *
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     * @param scrollX Horizontal scroll delta
     * @param scrollY Vertical scroll delta
     * @return true if the event was processed by the screen
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scrollOffset = Math.clamp(
            this.scrollOffset - scrollY * 20,
            0,
            this.maxScrollOffset
        );

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.FakeCreative;
import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationManager;
import dev.wrrulosdev.mcpclient.client.notifications.NotificationType;
import dev.wrrulosdev.mcpclient.client.screens.gui.SwitchOptionCard;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CheatsScreen extends BaseAnimatedScreen {

    private static final int CARD_HEIGHT = 65;
    private static final int GAP = 15;
    private static final int SIDE_MARGIN = 20;
    private final Screen parentScreen;
    private final List<SwitchOptionCard> allCards = new ArrayList<>();
    private double scrollOffset = 0;
    private double maxScrollOffset = 0;

    /**
     * Constructs the cheats screen with a reference to the parent screen
     *
     * @param parentScreen Screen to return to when exiting this view
     */
    public CheatsScreen(Screen parentScreen) {
        super(Component.empty());
        this.parentScreen = parentScreen;
        this.maxWidth = 650;
        this.maxHeight = 400;
    }

    /**
     * Initializes the screen and loads all available cheat modules
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    /**
     * Creates and registers all cheat module cards displayed in the UI
     */
    private void loadCards() {
        this.allCards.clear();

        CheatsSettings settings = MCPClient.getSettingsManager().getCheatsSettings();
        addCard(
            "Fly",
            "Allows you to fly freely through the air.",
            settings::isFlyEnabled,
            newState -> {
                settings.setFlyEnabled(newState);
                Fly.INSTANCE.run();
            }
        );
        addCard(
            "Fake Gamemode",
            "Spoofs creative mode client-side.",
            settings::isFakeGmEnabled,
            newState -> {
                settings.setFakeGmEnabled(newState);
                FakeCreative.INSTANCE.run();
            }
        );
        addCard(
            "Jesus",
            "Allows you to walk on water and other liquids.",
            settings::isJesusEnabled,
            settings::setJesusEnabled
        );
        addCard(
            "Spider",
            "Climb walls as if you were a spider.",
            settings::isSpiderEnabled,
            settings::setSpiderEnabled
        );
        addCard(
            "WallHack",
            "Shows players through walls.",
            settings::isWallhackEnabled,
            settings::setWallhackEnabled
        );
        addCard(
            "AntiKB",
            "Reduces or prevents knockback from attacks.",
            settings::isAntikbEnabled,
            settings::setAntikbEnabled
        );
        addCard(
            "HClip",
            "Teleports you horizontally through blocks.",
            settings::isHClipEnabled,
            settings::setHClipEnabled
        );
        addCard(
            "VClip",
            "Teleports you vertically up or down.",
            settings::isVClipEnabled,
            settings::setVClipEnabled
        );
        addCard(
            "Fullbright",
            "Removes darkness and maximizes visibility.",
            settings::isFullBrightEnabled,
            settings::setFullBrightEnabled
        );
        addCard(
            "NoFall",
            "Prevents or reduces fall damage.",
            settings::isNoFallEnabled,
            settings::setNoFallEnabled
        );
        addCard(
            "Block tracker",
            "Shows blocks through walls.",
            settings::isBlockTrackerEnabled,
            settings::setBlockTrackerEnabled
        );

        /*addCard("AutoEat", "Automatically eats food when your hunger is low.", newState -> MCPClient.getSettingsManager().getCheatsSettings().setFlyEnabled(newState));*/
    }

    private void addCard(String title, String desc, Supplier<Boolean> stateSupplier, Consumer<Boolean> onToggle) {
        SwitchOptionCard card = new SwitchOptionCard(
            title,
            desc,
            stateSupplier,
            TextureConstants.SIMPLE_SWITCH_ON,
            TextureConstants.SIMPLE_SWITCH_OFF,
            TextureConstants.SIMPLE_SETTINGS_ICON,
            TextureConstants.SIMPLE_RED_SETTINGS_ICON,
            onToggle,
            () -> System.out.println(title + " settings")
        );

        card.setHeight(CARD_HEIGHT);
        this.allCards.add(card);
    }

    /**
     * Returns the title displayed in the window header
     *
     * @return Screen title string
     */
    @Override
    protected String getWindowTitle() {
        return "Cheats / Modules";
    }

    /**
     * Renders the scrollable grid of cheat modules
     *
     * @param graphics Rendering context used for UI drawing
     * @param x1 Left boundary of content area
     * @param x2 Right boundary of content area
     * @param y1 Top boundary of content area
     * @param y2 Bottom boundary of content area
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param progress Animation interpolation value
     */
    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        int listY1 = y1 + 55;
        int listY2 = y2 - 10;
        int viewableHeight = listY2 - listY1;
        int columns = 3;
        int numRows = (int) Math.ceil(allCards.size() / (double) columns);
        int totalHeight = (numRows * CARD_HEIGHT) + ((numRows - 1) * GAP);
        this.maxScrollOffset = Math.max(0, totalHeight - viewableHeight);

        graphics.enableScissor(x1, listY1, x2, listY2);

        int totalHorizontalSpace = (SIDE_MARGIN * 2) + ((columns - 1) * GAP);
        int colWidth = ((x2 - x1) - totalHorizontalSpace) / columns;

        for (int i = 0; i < allCards.size(); i++) {
            int row = i / columns;
            int col = i % columns;

            int cardX = x1 + SIDE_MARGIN + (col * (colWidth + GAP));
            int cardY = listY1 + (row * (CARD_HEIGHT + GAP)) - (int) scrollOffset;

            if (cardY + CARD_HEIGHT >= listY1 && cardY <= listY2) {
                allCards.get(i).render(graphics, this.font, cardX, cardY, colWidth, progress, mouseX, mouseY);
            }
        }

        graphics.disableScissor();
    }

    /**
     * Updates cursor state based on hover interaction with module cards
     *
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     */
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        boolean hovered = allCards.stream()
            .anyMatch(card -> card.isMouseOver((int) mouseX, (int) mouseY));

        GLFW.glfwSetCursor(
            Minecraft.getInstance().getWindow().handle(),
            hovered ? HAND_CURSOR : ARROW_CURSOR
        );
    }

    /**
     * Handles mouse clicks on module cards and triggers a test notification
     *
     * @param event Mouse input event containing position and button data
     * @param doubleClick Whether the click is a double click action
     * @return true if a card handled the click, otherwise delegates to parent handling
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        NotificationManager.show(
            "KillAura",
            "Module enabled successfully.",
            NotificationType.SUCCESS
        );

        return allCards.stream()
            .anyMatch(card -> card.mouseClicked(event.x(), event.y(), event.button()))
            || super.mouseClicked(event, doubleClick);
    }

    /**
     * Handles scroll input and updates vertical scroll offset
     *
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     * @param scrollX Horizontal scroll delta
     * @param scrollY Vertical scroll delta
     * @return true if scroll input was processed
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

    /**
     * Handles keyboard input events for navigation and screen control
     *
     * @param event Key input event containing key code information
     * @return true if the event was consumed, otherwise false
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.setScreen(this.parentScreen);
            return true;
        }

        return super.keyPressed(event);
    }
}
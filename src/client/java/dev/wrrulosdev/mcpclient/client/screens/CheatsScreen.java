package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.*;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.screens.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CheatsScreen extends BaseAnimatedScreen {

    private static final int CARD_HEIGHT = 65;
    private static final int GAP = 15;
    private static final int SIDE_MARGIN = 20;

    private final Screen parentScreen;
    private final List<SwitchOptionCard> allCards = new ArrayList<>();

    private double scrollOffset = 0;
    private double maxScrollOffset = 0;

    /**
     * Creates a new cheats screen instance.
     *
     * @param parentScreen The screen that should be restored when exiting.
     */
    public CheatsScreen(Screen parentScreen) {
        super(Component.empty());

        this.parentScreen = parentScreen;
        this.maxWidth = 650;
        this.maxHeight = 400;
    }

    /**
     * Initializes the screen and loads all available module cards.
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    private void loadCards() {
        this.allCards.clear();

        addCard(
            Fly.INSTANCE,
            "Fly",
            "Allows you to fly freely through the air.",
            () -> openModuleSettings("Fly", "Fly settings...", getFlySettings())
        );
        addCard(
            FakeCreative.INSTANCE,
            "Fake Creative",
            "Spoofs creative mode client-side.",
            () -> openModuleSettings("FakeCreative", "FakeCreative settings...", getFakeCreativeSettings())
        );
        addCard(
            Jesus.INSTANCE,
            "Jesus",
            "Allows you to walk on water.",
            () -> openModuleSettings("Jesus", "Jesus settings...", getJesusSettings())
        );
        addCard(
            Spider.INSTANCE,
            "Spider",
            "Climb walls as if you were a spider.",
            () -> openModuleSettings("Spider", "Spider settings...", getSpiderSettings())
        );
        addCard(NoFall.INSTANCE, "NoFall", "Prevents or reduces fall damage.", null);
    }

    /**
     * Builds the complete cheat module card collection and associates each
     * module with its toggle and settings actions.

    private void loadCards() {
        this.allCards.clear();

        CheatsSettings settings =
            MCPClient.getSettingsManager().getCheatsSettings();

        addCard(
            "Fly",
            "Allows you to fly freely through the air.",
            settings::isFlyEnabled,
            newState -> {
                settings.setFlyEnabled(newState);
                Fly.INSTANCE.run();
            },
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
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
            settings::setJesusEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "Spider",
            "Climb walls as if you were a spider.",
            settings::isSpiderEnabled,
            settings::setSpiderEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "WallHack",
            "Shows players through walls.",
            settings::isWallhackEnabled,
            settings::setWallhackEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "AntiKB",
            "Reduces or prevents knockback from attacks.",
            settings::isAntikbEnabled,
            settings::setAntikbEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "HClip",
            "Teleports you horizontally through blocks.",
            settings::isHClipEnabled,
            settings::setHClipEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "VClip",
            "Teleports you vertically up or down.",
            settings::isVClipEnabled,
            settings::setVClipEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "Fullbright",
            "Removes darkness and maximizes visibility.",
            settings::isFullBrightEnabled,
            settings::setFullBrightEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "NoFall",
            "Prevents or reduces fall damage.",
            settings::isNoFallEnabled,
            settings::setNoFallEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );

        addCard(
            "Block tracker",
            "Shows blocks through walls.",
            settings::isBlockTrackerEnabled,
            settings::setBlockTrackerEnabled,
            () -> openModuleSettings(
                "Fly",
                "Permite volar...",
                getFlySettings()
            )
        );
    }    */


    /**
     * Creates and registers a new cheat card linked to a cheat implementation.
     *
     * @param cheat The cheat instance controlled by this card.
     * @param title The display name shown in the card header.
     * @param desc The descriptive text displayed below the title.
     * @param onSettingsClick The action executed when the settings icon is clicked.
     */
    private void addCard(
        CheatBase cheat,
        String title,
        String desc,
        Runnable onSettingsClick
    ) {
        SwitchOptionCard card = new SwitchOptionCard(
            title,
            desc,
            cheat::isEnabled,
            TextureConstants.SIMPLE_SWITCH_ON,
            TextureConstants.SIMPLE_SWITCH_OFF,
            TextureConstants.SIMPLE_SETTINGS_ICON,
            TextureConstants.SIMPLE_RED_SETTINGS_ICON,
            (_) -> cheat.toggle(),
            onSettingsClick
        );

        card.setHeight(CARD_HEIGHT);
        this.allCards.add(card);
    }

    /**
     * Returns the title displayed in the window header.
     *
     * @return The cheats screen title.
     */
    @Override
    protected String getWindowTitle() {
        return "Cheats / Modules";
    }

    /**
     * Renders the module grid and manages clipping and scrolling boundaries.
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
        int listY1 = y1 + 55;
        int listY2 = y2 - 10;
        int columns = 3;
        int colWidth = calculateColWidth(x1, x2, columns);
        int numRows = (int) Math.ceil(allCards.size() / (double) columns);
        this.maxScrollOffset = Math.max(
            0,
            (numRows * CARD_HEIGHT)
                + ((numRows - 1) * GAP)
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
                    columns
                );

            if (
                pos[1] + CARD_HEIGHT >= listY1
                    && pos[1] <= listY2
            ) {
                allCards.get(i).render(
                    graphics,
                    this.font,
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
    }

    /**
     * Updates the mouse cursor when hovering interactive module cards.
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
        int listY1 = y1 + 55;
        int columns = 3;
        int colWidth = calculateColWidth(x1, x2, columns);
        boolean hovered = false;

        for (int i = 0; i < allCards.size(); i++) {
            int[] pos = calculateCardPosition(i, x1, listY1, colWidth, columns);

            if (mouseX >= pos[0] && mouseX <= pos[0] + colWidth &&
                mouseY >= pos[1] && mouseY <= pos[1] + CARD_HEIGHT) {
                hovered = true;
                break;
            }
        }

        GLFW.glfwSetCursor(
            Minecraft.getInstance().getWindow().handle(),
            hovered ? HAND_CURSOR : ARROW_CURSOR
        );
    }

    /**
     * Forwards mouse click events to the appropriate module card.
     *
     * @param event The mouse button event.
     * @param doubleClick Indicates whether the click is a double click.
     * @return True if a module card consumed the click event.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int targetWidth = Math.min(this.width - 60, this.maxWidth);
        int targetHeight = Math.min(this.height - 60, this.maxHeight);
        int x1 = (this.width / 2) - (targetWidth / 2);
        int x2 = (this.width / 2) + (targetWidth / 2);
        int y1 = (this.height / 2) - (targetHeight / 2);
        int listY1 = y1 + 55;
        int columns = 3;
        int colWidth = calculateColWidth(x1, x2, columns);

        for (int i = 0; i < allCards.size(); i++) {
            int[] pos = calculateCardPosition(i, x1, listY1, colWidth, columns);

            if (allCards.get(i).mouseClicked(event.x(), event.y(), event.button(), pos[0], pos[1], colWidth)) {
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    /**
     * Calculates the width available for each column based on the
     * current layout dimensions and spacing rules.
     *
     * @param x1 The left boundary.
     * @param x2 The right boundary.
     * @param columns The total column count.
     * @return The calculated column width.
     */
    private int calculateColWidth(
        int x1,
        int x2,
        int columns
    ) {
        return (
            (x2 - x1)
                - (SIDE_MARGIN * 2)
                - ((columns - 1) * GAP)
        ) / columns;
    }

    /**
     * Calculates the screen position of a module card based on its
     * index, row, column and current scroll offset.
     *
     * @param i The module index.
     * @param x1 The left boundary.
     * @param listY1 The content starting Y coordinate.
     * @param colWidth The width of each column.
     * @param columns The number of columns.
     * @return An array containing the calculated X and Y coordinates.
     */
    private int[] calculateCardPosition(
        int i,
        int x1,
        int listY1,
        int colWidth,
        int columns
    ) {
        int row = i / columns;
        int col = i % columns;
        int cardX = x1 + SIDE_MARGIN + (col * (colWidth + GAP));
        int cardY = listY1 + (row * (CARD_HEIGHT + GAP)) - (int) scrollOffset;
        return new int[]{cardX, cardY};
    }

    /**
     * Updates the vertical scroll position while keeping it inside
     * the valid content bounds.
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

    /**
     * Handles keyboard interactions and returns to the parent screen
     * when the escape key is pressed.
     *
     * @param event The keyboard input event.
     * @return True if the event was consumed.
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.setScreen(this.parentScreen);
            return true;
        }

        return super.keyPressed(event);
    }

    /**
     * Opens the module configuration screen for a specific cheat module.
     *
     * @param moduleName The module display name.
     * @param desc The module description.
     * @param settings The collection of configurable settings.
     */
    private void openModuleSettings(
        String moduleName,
        String desc,
        List<AbstractSettingComponent> settings
    ) {
        Minecraft.getInstance().setScreen(
            new ModuleSettingsScreen(
                this,
                moduleName,
                desc,
                settings
            )
        );
    }

    /**
     * Creates the settings collection used by the Fly module.
     *
     * @return A list containing all Fly configuration components.
     */
    private List<AbstractSettingComponent> getFlySettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Fly Speed",
                0.05f,
                1.0f,
                MCPClient.getSettingsManager().getCheatsSettings().getFlySpeed(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().setFlySpeed(val)
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the fly",
                MCPClient.getSettingsManager().getCheatsSettings().getKeyForKeyBind(Fly.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(Fly.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(Fly.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the Fake Creative module.
     *
     * @return A list containing all Fake Creative configuration components.
     */
    private List<AbstractSettingComponent> getFakeCreativeSettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the Fake Creative",
                MCPClient.getSettingsManager().getCheatsSettings().getKeyForKeyBind(FakeCreative.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(FakeCreative.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(FakeCreative.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the Jesus module.
     *
     * @return A list containing all Jesus configuration components.
     */
    private List<AbstractSettingComponent> getJesusSettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Jesus Speed",
                1.0f,
                1.50f,
                (float) MCPClient.getSettingsManager().getCheatsSettings().getJesusSpeed(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().setJesusSpeed(val)
            )
        );

        settings.add(
            new ToggleSetting(
                "Walking on water",
                MCPClient.getSettingsManager().getCheatsSettings().isJesusWaterEnabled(),
                MCPClient.getSettingsManager().getCheatsSettings()::setJesusWaterEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Walking on lava",
                MCPClient.getSettingsManager().getCheatsSettings().isJesusLavaEnabled(),
                MCPClient.getSettingsManager().getCheatsSettings()::setJesusLavaEnabled
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the Jesus",
                MCPClient.getSettingsManager().getCheatsSettings().getKeyForKeyBind(Jesus.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(Jesus.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(Jesus.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the Spider module.
     *
     * @return A list containing all Spider configuration components.
     */
    private List<AbstractSettingComponent> getSpiderSettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Spider Speed",
                0.2f,
                1.0f,
                (float) MCPClient.getSettingsManager().getCheatsSettings().getSpiderSpeed(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().setSpiderSpeed(val)
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the Spider",
                MCPClient.getSettingsManager().getCheatsSettings().getKeyForKeyBind(Spider.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(Spider.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(Spider.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }
}
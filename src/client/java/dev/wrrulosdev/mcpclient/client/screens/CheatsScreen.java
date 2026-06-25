package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.*;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.screens.gui.*;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import dev.wrrulosdev.mcpclient.client.utilities.screens.MainMenuScreenUtils;
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

    public CheatsScreen(Screen parentScreen) {
        super(Component.empty());

        this.parentScreen = parentScreen;
        this.maxWidth = 650;
        this.maxHeight = 400;
    }

    /**
     * Initializes the screen and triggers the loading of cheat configuration cards.
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    /**
     * Generates a generic description string for a given cheat module.
     *
     * @param cheat The cheat module to describe
     * @return A formatted string describing the module's settings
     */
    private String getGenericDescription(CheatBase cheat) {
        return cheat.getName() + " settings...";
    }

    /**
     * Registers all available cheat modules by creating and adding their respective
     * interface cards to the screen's internal collection.
     */
    private void loadCards() {
        this.allCards.clear();

        addCard(
            Fly.INSTANCE,
            Fly.INSTANCE.getName(),
            "Allows you to fly freely through the air.",
            () -> openModuleSettings(
                Fly.INSTANCE.getName(),
                getGenericDescription(Fly.INSTANCE),
                getFlySettings()
            )
        );
        addCard(
            FakeCreative.INSTANCE,
            FakeCreative.INSTANCE.getName(),
            "Spoofs creative mode client-side.",
            () -> openModuleSettings(
                FakeCreative.INSTANCE.getName(),
                getGenericDescription(FakeCreative.INSTANCE),
                getFakeCreativeSettings()
            )
        );
        addCard(
            Jesus.INSTANCE,
            Jesus.INSTANCE.getName(),
            "Allows you to walk on water.",
            () -> openModuleSettings(
                Jesus.INSTANCE.getName(),
                getGenericDescription(Jesus.INSTANCE),
                getJesusSettings()
            )
        );
        addCard(
            Spider.INSTANCE,
            Spider.INSTANCE.getName(),
            "Climb walls as if you were a spider.",
            () -> openModuleSettings(
                Spider.INSTANCE.getName(),
                getGenericDescription(Spider.INSTANCE),
                getSpiderSettings()
            )
        );
        addCard(
            NoFall.INSTANCE,
            NoFall.INSTANCE.getName(),
            "Prevents or reduces fall damage.",
            () -> openModuleSettings(
                NoFall.INSTANCE.getName(),
                getGenericDescription(NoFall.INSTANCE),
                getNoFallSettings()
            )
        );
        addCard(
            WallHack.INSTANCE,
            WallHack.INSTANCE.getName(),
            "Shows players through walls.",
            () -> openModuleSettings(
                WallHack.INSTANCE.getName(),
                getGenericDescription(WallHack.INSTANCE),
                getWallHackSettings()
            )
        );
        addCard(
            HClip.INSTANCE,
            HClip.INSTANCE.getName(),
            "Teleports you horizontally through blocks.",
            () -> openModuleSettings(
                HClip.INSTANCE.getName(),
                getGenericDescription(HClip.INSTANCE),
                getHClipSettings()
            )
        );
        addCard(
            VClip.INSTANCE,
            VClip.INSTANCE.getName(),
            "Teleports you vertically up or down.",
            () -> openModuleSettings(
                VClip.INSTANCE.getName(),
                getGenericDescription(VClip.INSTANCE),
                getVClipSettings()
            )
        );
        addCard(
            FullBright.INSTANCE,
            FullBright.INSTANCE.getName(),
            "Removes darkness and maximizes visibility.",
            () -> openModuleSettings(
                FullBright.INSTANCE.getName(),
                getGenericDescription(FullBright.INSTANCE),
                getFullBrightSettings()
            )
        );
        addCard(
            AntiKB.INSTANCE,
            AntiKB.INSTANCE.getName(),
            "Reduces or prevents knockback from attacks.",
            () -> openModuleSettings(
                AntiKB.INSTANCE.getName(),
                getGenericDescription(AntiKB.INSTANCE),
                getAntiKBSettings()
            )
        );
        addCard(
            BlockTracker.INSTANCE,
            BlockTracker.INSTANCE.getName(),
            "Shows blocks through walls.",
            () -> openModuleSettings(
                BlockTracker.INSTANCE.getName(),
                getGenericDescription(BlockTracker.INSTANCE),
                getBlockTrackerSettings()
            )
        );
    }

    /**
     * Configures and adds a {@link SwitchOptionCard} to the list of displayed cards.
     *
     * @param cheat             The associated cheat module
     * @param title             The title text for the card
     * @param desc              The description text for the card
     * @param onSettingsClick   Action to perform when the settings icon is clicked
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
     * Defines the title of the screen.
     *
     * @return The window title string
     */
    @Override
    protected String getWindowTitle() {
        return "Cheats / Modules";
    }

    /**
     * Renders the grid of cheat cards within the defined window area.
     *
     * @param graphics  The graphics extraction utility
     * @param x1        Start X coordinate
     * @param x2        End X coordinate
     * @param y1        Start Y coordinate
     * @param y2        End Y coordinate
     * @param mouseX    Current mouse X position
     * @param mouseY    Current mouse Y position
     * @param progress  Current animation progress
     */
    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        this.maxScrollOffset = MainMenuScreenUtils.renderWindowGenericContent(graphics, x1, x2, y1, y2, mouseX, mouseY, progress, this.font, this.maxScrollOffset, this.scrollOffset, CARD_HEIGHT, SIDE_MARGIN, GAP, allCards);
    }

    /**
     * Handles mouse movement to update cursor feedback when hovering over cards.
     *
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     */
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        boolean hovered = MainMenuScreenUtils.isMouseOverGrid(
            mouseX,
            mouseY,
            this.width,
            this.height,
            this.maxWidth,
            this.maxHeight,
            CARD_HEIGHT,
            this.allCards.size(),
            3,
            SIDE_MARGIN,
            GAP,
            (int) this.scrollOffset
        );
        GLFW.glfwSetCursor(
            Minecraft.getInstance().getWindow().handle(),
            hovered ? HAND_CURSOR : ARROW_CURSOR
        );
    }

    /**
     * Handles mouse click events within the card grid area.
     *
     * @param event       The mouse button event details
     * @param doubleClick Whether the click was a double click
     * @return True if the click was handled by the grid, false otherwise
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean handled = MainMenuScreenUtils.handleGridClick(
            event.x(),
            event.y(),
            event.button(),
            this.allCards,
            this.width,
            this.height,
            this.maxWidth,
            this.maxHeight,
            CARD_HEIGHT,
            3,
            SIDE_MARGIN,
            GAP,
            (int) this.scrollOffset
        );

        if (handled) {
            return true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    /**
     * Handles mouse scrolling to update the vertical offset of the cards.
     *
     * @param mouseX  Mouse X position
     * @param mouseY  Mouse Y position
     * @param scrollX Horizontal scroll delta
     * @param scrollY Vertical scroll delta
     * @return True if the event was processed
     */
    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double scrollX,
        double scrollY
    ) {
        this.scrollOffset = MainMenuScreenUtils.calculateScrollOffset(
            this.scrollOffset,
            scrollY,
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
            this.minecraft.setScreenAndShow(this.parentScreen);
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
        Minecraft.getInstance().setScreenAndShow(
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
        List<AbstractSettingComponent> settings = new ArrayList<>();

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
                MCPClient.getKeyBindManager().getCurrentKeyCode(Fly.INSTANCE.getIdentifier()),
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
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the Fake Creative",
                MCPClient.getKeyBindManager().getCurrentKeyCode(FakeCreative.INSTANCE.getIdentifier()),
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
        List<AbstractSettingComponent> settings = new ArrayList<>();

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
                MCPClient.getKeyBindManager().getCurrentKeyCode(Jesus.INSTANCE.getIdentifier()),
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
        List<AbstractSettingComponent> settings = new ArrayList<>();

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
                MCPClient.getKeyBindManager().getCurrentKeyCode(Spider.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(Spider.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(Spider.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the NoFall module.
     *
     * @return A list containing all NoFall configuration components.
     */
    private List<AbstractSettingComponent> getNoFallSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the NoFall",
                MCPClient.getKeyBindManager().getCurrentKeyCode(NoFall.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(NoFall.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(NoFall.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the WallHack module.
     *
     * @return A list containing all WallHack configuration components.
     */
    private List<AbstractSettingComponent> getWallHackSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Showing borders through the players",
                MCPClient.getSettingsManager().getCheatsSettings().isWallHackBoxesEnabled(),
                MCPClient.getSettingsManager().getCheatsSettings()::setWallHackBoxesEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Show stick mans of the players",
                MCPClient.getSettingsManager().getCheatsSettings().isWallHackStickManEnabled(),
                MCPClient.getSettingsManager().getCheatsSettings()::setWallHackStickManEnabled
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the WallHack",
                MCPClient.getKeyBindManager().getCurrentKeyCode(WallHack.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(WallHack.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(WallHack.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the HClip module.
     *
     * @return A list containing all HClip configuration components.
     */
    private List<AbstractSettingComponent> getHClipSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Teleportation distance",
                1.0f,
                100.0f,
                (float) MCPClient.getSettingsManager().getCheatsSettings().gethClipDistance(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().sethClipDistance(val)
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the HClip",
                MCPClient.getKeyBindManager().getCurrentKeyCode(HClip.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(HClip.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(HClip.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the VClip module.
     *
     * @return A list containing all VClip configuration components.
     */
    private List<AbstractSettingComponent> getVClipSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Teleportation distance",
                1.0f,
                100.0f,
                (float) MCPClient.getSettingsManager().getCheatsSettings().getvClipDistance(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().setvClipDistance(val)
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the VClip",
                MCPClient.getKeyBindManager().getCurrentKeyCode(VClip.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(VClip.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(VClip.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the FullBright module.
     *
     * @return A list containing all FullBright configuration components.
     */
    private List<AbstractSettingComponent> getFullBrightSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Amount of brightness when activated",
                2.0f,
                100.0f,
                (float) MCPClient.getSettingsManager().getCheatsSettings().getFullBrightAmount(),
                "x",
                val -> MCPClient.getSettingsManager().getCheatsSettings().setFullBrightAmount(val)
            )
        );

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the FullBright",
                MCPClient.getKeyBindManager().getCurrentKeyCode(FullBright.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(FullBright.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(FullBright.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the AntiKB module.
     *
     * @return A list containing all AntiKB configuration components.
     */
    private List<AbstractSettingComponent> getAntiKBSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new KeybindSetting(
                "Assign a key bind to the AntiKB",
                MCPClient.getKeyBindManager().getCurrentKeyCode(AntiKB.INSTANCE.getIdentifier()),
                val -> {
                    MCPClient.getSettingsManager().getCheatsSettings().setKeyForKeyBind(AntiKB.INSTANCE.getIdentifier(), val);
                    MCPClient.getKeyBindManager().updateKey(AntiKB.INSTANCE.getIdentifier(), val);
                }
            )
        );

        return settings;
    }

    /**
     * Creates the settings collection used by the BlockTracker module.
     *
     * @return A list containing all BlockTracker configuration components.
     */
    private List<AbstractSettingComponent> getBlockTrackerSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        CheatsSettings cs = MCPClient.getSettingsManager().getCheatsSettings();

        settings.add(
            new SliderSetting(
                "Block scanner radius",
                5.0f,
                128.0f,
                cs.getBlockTrackerScanRadius(),
                "blocks",
                cs::setBlockTrackerScanRadius
            )
        );

        settings.add(
            new SliderSetting(
                "Block scanner delay",
                0.1f,
                10.0f,
                cs.getBlockTrackerScanDelay(),
                "seconds",
                cs::setBlockTrackerScanDelay
            )
        );

        settings.add(new ToggleSetting(
            "View coal ores",
            cs.isBlockTrackerCoalOresEnabled(),
            cs::setBlockTrackerCoalOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View iron ores",
            cs.isBlockTrackerIronOresEnabled(),
            cs::setBlockTrackerIronOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View copper ores",
            cs.isBlockTrackerCopperOresEnabled(),
            cs::setBlockTrackerCopperOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View gold ores",
            cs.isBlockTrackerGoldOresEnabled(),
            cs::setBlockTrackerGoldOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View redstone ores",
            cs.isBlockTrackerRedstoneOresEnabled(),
            cs::setBlockTrackerRedstoneOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View lapis ores",
            cs.isBlockTrackerLapisOresEnabled(),
            cs::setBlockTrackerLapisOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View diamond ores",
            cs.isBlockTrackerDiamondOresEnabled(),
            cs::setBlockTrackerDiamondOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View emerald ores",
            cs.isBlockTrackerEmeraldOresEnabled(),
            cs::setBlockTrackerEmeraldOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View nether ores",
            cs.isBlockTrackerNetherOresEnabled(),
            cs::setBlockTrackerNetherOresEnabled
        ));

        settings.add(new ToggleSetting(
            "View ancient debris",
            cs.isBlockTrackerAncientDebrisEnabled(),
            cs::setBlockTrackerAncientDebrisEnabled
        ));

        settings.add(new ToggleSetting(
            "View mineral blocks",
            cs.isBlockTrackerMineralBlocksEnabled(),
            cs::setBlockTrackerMineralBlocksEnabled
        ));

        settings.add(new ToggleSetting(
            "View storage",
            cs.isBlockTrackerStorageEnabled(),
            cs::setBlockTrackerStorageEnabled
        ));

        settings.add(new ToggleSetting(
            "View utility",
            cs.isBlockTrackerUtilityEnabled(),
            cs::setBlockTrackerUtilityEnabled
        ));

        settings.add(new ToggleSetting(
            "View redstone",
            cs.isBlockTrackerRedstoneEnabled(),
            cs::setBlockTrackerRedstoneEnabled
        ));

        settings.add(new KeybindSetting(
            "Assign a key bind to the BlockTracker",
            MCPClient.getKeyBindManager().getCurrentKeyCode(BlockTracker.INSTANCE.getIdentifier()),
            val -> {
                cs.setKeyForKeyBind(BlockTracker.INSTANCE.getIdentifier(), val);
                MCPClient.getKeyBindManager().updateKey(BlockTracker.INSTANCE.getIdentifier(), val);
            }
        ));

        return settings;
    }
}

package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.options.*;
import dev.wrrulosdev.mcpclient.client.screens.gui.*;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
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
import java.util.concurrent.atomic.AtomicReference;

public class ClientSettingsScreen extends BaseAnimatedScreen {

    private static final int CARD_HEIGHT = 65;
    private static final int GAP = 15;
    private static final int SIDE_MARGIN = 20;
    private final Screen parentScreen;
    private final List<SwitchOptionCard> allCards = new ArrayList<>();
    private double scrollOffset = 0;
    private double maxScrollOffset = 0;
    private ClientSettings clientSettings;

    /**
     * Constructs a new {@code ClientSettingsScreen}.
     * Initializes the screen with predefined dimensions, caches the parent screen
     * for navigation, and retrieves the current client settings from the manager.
     *
     * @param parentScreen The screen to return to when closing this settings menu
     */
    public ClientSettingsScreen(Screen parentScreen) {
        super(Component.empty());
        this.parentScreen = parentScreen;
        this.maxWidth = 650;
        this.maxHeight = 400;
        this.clientSettings = MCPClient.getSettingsManager().getClientSettings();
    }

    /**
     * Called by the Minecraft GUI system when the screen is initialized or resized.
     * Triggers the UI layout logic by populating the card list.
     */
    @Override
    protected void init() {
        super.init();
        loadCards();
    }

    /**
     * Populates the screen with toggleable option cards for each client module.
     */
    private void loadCards() {
        this.allCards.clear();

        addCard(
            ClientHud.INSTANCE,
            ClientHud.INSTANCE.getName(),
            ClientHud.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                ClientHud.INSTANCE.getName(),
                ClientHud.INSTANCE.getLongDescription(),
                getClientHudSettings()
            )
        );
        addCard(
            Anonymous.INSTANCE,
            Anonymous.INSTANCE.getName(),
            Anonymous.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                Anonymous.INSTANCE.getName(),
                Anonymous.INSTANCE.getLongDescription(),
                getAnonymousSettings()
            )
        );
        addCard(
            NameTag.INSTANCE,
            NameTag.INSTANCE.getName(),
            NameTag.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                NameTag.INSTANCE.getName(),
                NameTag.INSTANCE.getLongDescription(),
                getNameTagSettings()
            )
        );
        addCard(
            ChatAnimation.INSTANCE,
            ChatAnimation.INSTANCE.getName(),
            ChatAnimation.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                ChatAnimation.INSTANCE.getName(),
                ChatAnimation.INSTANCE.getLongDescription(),
                getChatAnimationSettings()
            )
        );
        addCard(
            Notifications.INSTANCE,
            Notifications.INSTANCE.getName(),
            Notifications.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                Notifications.INSTANCE.getName(),
                Notifications.INSTANCE.getLongDescription(),
                getNotificationsSettings()
            )
        );
        addCard(
            PlayerModel.INSTANCE,
            PlayerModel.INSTANCE.getName(),
            PlayerModel.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                PlayerModel.INSTANCE.getName(),
                PlayerModel.INSTANCE.getLongDescription(),
                getPlayerModelSettings()
            )
        );
        addCard(
            AnvilButtons.INSTANCE,
            AnvilButtons.INSTANCE.getName(),
            AnvilButtons.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                AnvilButtons.INSTANCE.getName(),
                AnvilButtons.INSTANCE.getLongDescription(),
                getAnvilButtonsSettings()
            )
        );
        addCard(
            CustomPrefix.INSTANCE,
            CustomPrefix.INSTANCE.getName(),
            CustomPrefix.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                CustomPrefix.INSTANCE.getName(),
                CustomPrefix.INSTANCE.getLongDescription(),
                getCustomPrefixSettings()
            )
        );
    }

    /**
     * Creates and registers a new option card to the UI list.
     *
     * @param option          The option object instance
     * @param title           The display title of the option
     * @param desc            The description of the option
     * @param onSettingsClick Callback to execute when the settings button is clicked
     */
    private void addCard(OptionsBase option, String title, String desc, Runnable onSettingsClick) {
        SwitchOptionCard card = new SwitchOptionCard(
            title,
            desc,
            option::isEnabled,
            TextureConstants.SIMPLE_SWITCH_ON,
            TextureConstants.SIMPLE_SWITCH_OFF,
            TextureConstants.SIMPLE_SETTINGS_ICON,
            TextureConstants.SIMPLE_RED_SETTINGS_ICON,
            (_) -> option.toggle(),
            onSettingsClick
        );

        card.setHeight(CARD_HEIGHT);
        this.allCards.add(card);
    }

    /**
     * Renders the main content of the settings window, including the grid of option cards.
     * Updates the maximum scroll offset based on the content size.
     *
     * @param graphics The graphics extractor for drawing UI elements
     * @param x1 The start X coordinate of the window area
     * @param x2 The end X coordinate of the window area
     * @param y1 The start Y coordinate of the window area
     * @param y2 The end Y coordinate of the window area
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param progress The current animation progress
     */
    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        this.maxScrollOffset = MainMenuScreenUtils.renderWindowGenericContent(graphics, x1, x2, y1, y2, mouseX, mouseY, progress, this.font, this.maxScrollOffset, this.scrollOffset, CARD_HEIGHT, SIDE_MARGIN, GAP, allCards);
    }

    /**
     * Handles mouse movement to update the cursor appearance when hovering over interactive cards.
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
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
     * Handles mouse click events within the grid area.
     * Delegates click handling to {@link MainMenuScreenUtils}.
     *
     * @param event The mouse button event
     * @param doubleClick Whether the click was a double click
     * @return True if the click was handled by a card in the grid, false otherwise
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
     * Handles mouse scroll events to update the vertical scroll position.
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param scrollX The horizontal scroll delta
     * @param scrollY The vertical scroll delta
     * @return Always returns true to indicate the event was handled
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
     * Navigates to a sub-screen for configuring individual module settings.
     *
     * @param moduleName The name of the module to display in the sub-screen header
     * @param desc       A short description or sub-header for the module
     * @param settings   The list of setting components to render in the sub-screen
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
     * Generates a list of settings for the Anonymous module.
     *
     * @return A list of {@link AbstractSettingComponent} for anonymity toggles and text input
     */
    private List<AbstractSettingComponent> getAnonymousSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Hide name in chat",
                this.clientSettings.isAnonymousChatEnabled(),
                this.clientSettings::setAnonymousChatEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name in nametags",
                this.clientSettings.isAnonymousNameTagsEnabled(),
                this.clientSettings::setAnonymousNameTagsEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name on scoreboard",
                this.clientSettings.isAnonymousScoreboardEnabled(),
                this.clientSettings::setAnonymousScoreboardEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name in tab",
                this.clientSettings.isAnonymousTabListEnabled(),
                this.clientSettings::setAnonymousTabListEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name in holograms",
                this.clientSettings.isAnonymousHologramsEnabled(),
                this.clientSettings::setAnonymousHologramsEnabled
            )
        );

        settings.add(
            new TextSetting(
                "New Anonymous username",
                this.clientSettings.getNewAnonymousName(),
                this.clientSettings::setNewAnonymousName
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the Client HUD module.
     *
     * @return A list of {@link AbstractSettingComponent} for HUD toggles
     */
    private List<AbstractSettingComponent> getClientHudSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Show FPS",
                this.clientSettings.isClientHudFpsEnabled(),
                this.clientSettings::setClientHudFpsEnabled
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the NameTag module.
     *
     * @return A list of {@link AbstractSettingComponent} for name tag customization
     */
    private List<AbstractSettingComponent> getNameTagSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();
        int currentColor = this.clientSettings.getNameTagColor();

        settings.add(
            new ToggleSetting(
                "Custom color enabled",
                this.clientSettings.isNameTagColorEnabled(),
                this.clientSettings::setNameTagColorEnabled
            )
        );

        settings.add(
            new ColorSetting(
                "Custom color",
                String.format("#%06X", currentColor),
                this.clientSettings::setNameTagColor
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the Chat Animation module.
     *
     * @return A list containing the {@link SliderSetting} for animation duration
     */
    private List<AbstractSettingComponent> getChatAnimationSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Chat animation speed",
                (float) 0.005,
                (float) 0.030,
                (float) this.clientSettings.getChatAnimationDuration(),
                "ms",
                this.clientSettings::setChatAnimationDuration
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the Notifications module.
     *
     * @return A list containing the {@link SliderSetting} for notification duration
     */
    private List<AbstractSettingComponent> getNotificationsSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Notification duration",
                1,
                5,
                (float) this.clientSettings.getNotificationDuration(),
                "ms",
                this.clientSettings::setNotificationDuration
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the Player Model preview module.
     *
     * @return A list of {@link SliderSetting} components for model positioning and scaling
     */
    private List<AbstractSettingComponent> getPlayerModelSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new SliderSetting(
                "Model X margin",
                (float) 1,
                (float) 100,
                (float) this.clientSettings.getPlayerModelMarginX(),
                "x",
                this.clientSettings::setPlayerModelMarginX
            )
        );

        settings.add(
            new SliderSetting(
                "Model Y margin",
                (float) 1,
                (float) 100,
                (float) this.clientSettings.getPlayerModelMarginY(),
                "x",
                this.clientSettings::setPlayerModelMarginY
            )
        );

        settings.add(
            new SliderSetting(
                "Model size",
                (float) 1,
                (float) 100,
                (float) this.clientSettings.getPlayerModelSize(),
                "x",
                this.clientSettings::setPlayerModelSize
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the Anvil Buttons preview module.
     *
     * @return A list of {@link SliderSetting} components for model positioning and scaling
     */
    private List<AbstractSettingComponent> getAnvilButtonsSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new MapSetting(
                "Anvil Buttons",
                this.clientSettings.getAnvilButtons(),
                "Button name...",
                "Content...",
                (newMap) -> {
                    this.clientSettings.setAnvilButtons(newMap);
                }
            )
        );

        return settings;
    }

    /**
     * Generates a list of settings for the CustomPrefix preview module.
     *
     * @return A list of {@link SliderSetting} components for model positioning and scaling
     */
    private List<AbstractSettingComponent> getCustomPrefixSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new TextSetting(
                "Custom Prefix",
                this.clientSettings.getCustomPrefix(),
                this.clientSettings::setCustomPrefix
            )
        );

        settings.add(
            new StringListSetting(
                "Usernames",
                this.clientSettings.getCustomPrefixUsernames(),
                this.clientSettings::setCustomPrefixUsernames
            )
        );

        return settings;
    }


    /**
     * Returns the title for this screen to be displayed in the UI.
     *
     * @return The window title string
     */
    @Override
    protected String getWindowTitle() {
        return "Client Settings / Modules";
    }
}

package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.Fly;
import dev.wrrulosdev.mcpclient.client.cheats.Jesus;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.options.*;
import dev.wrrulosdev.mcpclient.client.screens.gui.*;
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

public class ClientSettingsScreen extends BaseAnimatedScreen {

    private static final int CARD_HEIGHT = 65;
    private static final int GAP = 15;
    private static final int SIDE_MARGIN = 20;
    private final Screen parentScreen;
    private final List<SwitchOptionCard> allCards = new ArrayList<>();
    private double scrollOffset = 0;
    private double maxScrollOffset = 0;

    public ClientSettingsScreen(Screen parentScreen) {
        super(Component.empty());
        this.parentScreen = parentScreen;
        this.maxWidth = 650;
        this.maxHeight = 400;
    }

    @Override
    protected void init() {
        super.init();
        loadCards();
    }

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
                getChatAnimationSettings()
            )
        );
        addCard(
            PlayerModel.INSTANCE,
            PlayerModel.INSTANCE.getName(),
            PlayerModel.INSTANCE.getShortDescription(),
            () -> openModuleSettings(
                PlayerModel.INSTANCE.getName(),
                PlayerModel.INSTANCE.getLongDescription(),
                getChatAnimationSettings()
            )
        );
        System.out.println(ChatAnimation.INSTANCE.isEnabled());
    }

    /**
     * Creates and registers a new exploit card.
     *
     * @param title The exploit name.
     * @param desc The exploit description.
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

    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        this.maxScrollOffset = MainMenuScreenUtils.renderWindowGenericContent(graphics, x1, x2, y1, y2, mouseX, mouseY, progress, this.font, this.maxScrollOffset, this.scrollOffset, CARD_HEIGHT, SIDE_MARGIN, GAP, allCards);
    }

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

    private List<AbstractSettingComponent> getAnonymousSettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Hide name in chat",
                MCPClient.getSettingsManager().getClientSettings().isAnonymousChatEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setAnonymousChatEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name on scoreboard",
                MCPClient.getSettingsManager().getClientSettings().isAnonymousScoreboardEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setAnonymousScoreboardEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name in tab",
                MCPClient.getSettingsManager().getClientSettings().isAnonymousTabListEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setAnonymousTabListEnabled
            )
        );

        settings.add(
            new ToggleSetting(
                "Hide name in holograms",
                MCPClient.getSettingsManager().getClientSettings().isAnonymousHologramsEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setAnonymousHologramsEnabled
            )
        );

        settings.add(
            new TextSetting(
                "New Anonymous username",
                MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName(),
                MCPClient.getSettingsManager().getClientSettings()::setNewAnonymousName
            )
        );

        return settings;
    }

    private List<AbstractSettingComponent> getClientHudSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Show FPS",
                MCPClient.getSettingsManager().getClientSettings().isClientHudFpsEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setClientHudFpsEnabled
            )
        );

        return settings;
    }

    private List<AbstractSettingComponent> getNameTagSettings() {
        List<AbstractSettingComponent> settings = new ArrayList<>();
        int currentColor = MCPClient.getSettingsManager().getClientSettings().getNameTagColor();

        settings.add(
            new ToggleSetting(
                "Custom color enabled",
                MCPClient.getSettingsManager().getClientSettings().isNameTagColorEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setNameTagColorEnabled
            )
        );

        settings.add(
            new ColorSetting(
                "Custom color",
                String.format("#%06X", currentColor),
                MCPClient.getSettingsManager().getClientSettings()::setNameTagColor
            )
        );

        return settings;
    }

    private List<AbstractSettingComponent> getChatAnimationSettings() {
        List<AbstractSettingComponent> settings =
            new ArrayList<>();

        settings.add(
            new ToggleSetting(
                "Example",
                MCPClient.getSettingsManager().getClientSettings().isClientHudFpsEnabled(),
                MCPClient.getSettingsManager().getClientSettings()::setClientHudFpsEnabled
            )
        );

        return settings;
    }

    @Override
    protected String getWindowTitle() {
        return "Client Settings / Modules";
    }
}

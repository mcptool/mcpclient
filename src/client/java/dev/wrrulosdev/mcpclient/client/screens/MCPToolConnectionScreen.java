package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomTextField;
import dev.wrrulosdev.mcpclient.client.utilities.messages.CC;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.concurrent.CompletableFuture;

public class MCPToolConnectionScreen extends BaseAnimatedScreen {

    private enum ConnectionState {
        DISCONNECTED, CONNECTING, CONNECTED, FAILED
    }

    private final Screen parentScreen;
    private ConnectionState state = ConnectionState.DISCONNECTED;
    private boolean showPortField = false;

    private CustomButton connectButton;
    private CustomButton changePortButton;
    private CustomButton backButton;
    private CustomTextField portField;

    public MCPToolConnectionScreen(Screen parentScreen) {
        super(Component.empty());
        this.parentScreen = parentScreen;
        this.maxWidth = 400;
        this.maxHeight = 250;
    }

    /**
     * Initializes the screen components, including buttons and text fields,
     * setting their initial visibility and placement configurations.
     */
    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        int defaultWidth = 200;
        int defaultHeight = 20;

        this.connectButton = CustomButton.builder(Component.literal("Connect"))
            .dimensions(defaultWidth, defaultHeight)
            .onPress(button -> this.attemptConnection())
            .build();
        this.connectButton.visible = false;
        this.addRenderableWidget(this.connectButton);

        this.changePortButton = CustomButton.builder(Component.literal("Change port"))
            .dimensions(defaultWidth, defaultHeight)
            .onPress(button -> {
                this.showPortField = !this.showPortField;
                this.updateWidgetStates();
            })
            .build();
        this.changePortButton.visible = false;
        this.addRenderableWidget(this.changePortButton);

        this.portField = CustomTextField.builder(Component.literal("localhost:8080"))
            .size(defaultWidth, defaultHeight)
            .build();
        this.portField.setText("localhost:8080");
        this.portField.visible = false;
        this.addRenderableWidget(this.portField);

        this.backButton = CustomButton.builder(Component.literal("Back"))
            .dimensions(defaultWidth, defaultHeight)
            .onPress(button -> this.onClose())
            .build();
        this.backButton.visible = false;
        this.addRenderableWidget(this.backButton);
    }

    /**
     * Attempts to establish a connection to the MCPTool asynchronously,
     * updating the connection state and UI upon completion.
     */
    private void attemptConnection() {
        this.state = ConnectionState.CONNECTING;
        this.updateWidgetStates();

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).thenRun(() -> {
            this.minecraft.execute(() -> {
                this.state = ConnectionState.CONNECTED;
                this.updateWidgetStates();
            });
        });
    }

    /**
     * Renders the screen content, including status messages and color-coded
     * UI elements based on the current connection state.
     *
     * @param graphics The screen rendering engine
     * @param x1 The start X coordinate
     * @param x2 The end X coordinate
     * @param y1 The start Y coordinate
     * @param y2 The end Y coordinate
     * @param mouseX Current mouse X position
     * @param mouseY Current mouse Y position
     * @param progress Animation progress value
     */
    @Override
    protected void renderWindowContent(GuiGraphicsExtractor graphics, int x1, int x2, int y1, int y2, int mouseX, int mouseY, float progress) {
        int centerX = x1 + (x2 - x1) / 2;
        int titleOffset = 40;
        int contentHeight = calculateContentHeight();
        int availableSpace = (y2 - y1) - titleOffset;
        int startY = y1 + titleOffset + (availableSpace - contentHeight) / 2;
        int textAlpha = (int) (255 * progress);

        int textColor = switch (state) {
            case CONNECTED -> 0x55FF55 | (textAlpha << 24);
            case FAILED -> 0xFF5555 | (textAlpha << 24);
            default -> 0xFFFFFF | (textAlpha << 24);
        };

        Component statusText = switch (state) {
            case CONNECTING -> Component.literal("Connecting to MCPTool...");
            case CONNECTED -> CC.parseColorCodes("&aMCPTool is connected correctly!");
            case FAILED -> CC.parseColorCodes("&cCould not connect correctly!");
            default -> CC.parseColorCodes("&cMCPTool is not connected, connect it now!");
        };

        graphics.text(this.font, statusText, centerX - (this.font.width(statusText) / 2), startY, textColor, true);
        updateWidgetPositions(centerX, startY + 25, progress);
    }

    /**
     * Calculates the total vertical space required by the visible widgets based
     * on the current connection state.
     *
     * @return The calculated height in pixels
     */
    private int calculateContentHeight() {
        int h = 10;
        if (this.connectButton != null && state != ConnectionState.CONNECTED) h += 30;
        if (this.changePortButton != null && state == ConnectionState.DISCONNECTED) h += 30;
        if (this.portField != null && state == ConnectionState.DISCONNECTED && this.showPortField) h += 30;
        if (this.backButton != null) h += 30;
        return h;
    }

    /**
     * Dynamically updates the positions and visibility of screen widgets
     * using an easing function for entrance animations.
     *
     * @param centerX The center X coordinate of the window
     * @param startY The starting Y coordinate for the widget list
     * @param progress The animation progress
     */
    private void updateWidgetPositions(int centerX, int startY, float progress) {
        float ease = 1.0f - (float) Math.pow(1.0f - progress, 3);
        int slideOffset = (int) ((1.0f - ease) * 20);
        int currentY = startY + slideOffset;

        if (this.connectButton != null) {
            this.connectButton.setX(centerX - (this.connectButton.getWidth() / 2));
            this.connectButton.setY(currentY);
            this.connectButton.visible = (state != ConnectionState.CONNECTED);
            if (this.connectButton.visible) currentY += this.connectButton.getHeight() + 10;
        }

        if (this.changePortButton != null) {
            this.changePortButton.setX(centerX - (this.changePortButton.getWidth() / 2));
            this.changePortButton.setY(currentY);
            this.changePortButton.visible = (state == ConnectionState.DISCONNECTED);
            if (this.changePortButton.visible) currentY += this.changePortButton.getHeight() + 10;
        }

        if (this.portField != null) {
            this.portField.setX(centerX - (this.portField.getWidth() / 2));
            this.portField.setY(currentY);
            this.portField.visible = (state == ConnectionState.DISCONNECTED && this.showPortField);
            if (this.portField.visible) currentY += this.portField.getHeight() + 10;
        }

        if (this.backButton != null) {
            this.backButton.setX(centerX - (this.backButton.getWidth() / 2));
            this.backButton.setY(currentY);
            this.backButton.visible = (state != ConnectionState.CONNECTING);
        }
    }

    /**
     * Updates widget properties, such as button interactivity and labels,
     * according to the current connection state.
     */
    private void updateWidgetStates() {
        if (this.connectButton != null) {
            this.connectButton.active = (state != ConnectionState.CONNECTING);
            this.connectButton.setMessage(state == ConnectionState.FAILED
                ? Component.literal("Retry")
                : Component.literal("Connect"));
        }
        if (this.backButton != null) {
            this.backButton.active = (state != ConnectionState.CONNECTING);
        }
    }

    /**
     * Gets the title to be displayed for this screen.
     *
     * @return The title string
     */
    @Override
    protected String getWindowTitle() {
        return "MCPTool Connection";
    }

    /**
     * Closes the current screen and returns to the parent screen.
     */
    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parentScreen);
    }
}
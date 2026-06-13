package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.constants.ButtonConstants;
import dev.wrrulosdev.mcpclient.client.screens.gui.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.List;

public class ModuleSettingsScreen extends BaseAnimatedScreen {

    private final Screen parentScreen;
    private final String moduleName;
    private final String moduleDescription;
    private final List<AbstractSettingComponent> settings;
    private CustomButton backButton;
    private double scrollOffset = 0;
    private double maxScrollOffset = 0;

    /**
     * Creates a new module settings screen.
     *
     * @param parentScreen The screen that should be restored when exiting.
     * @param moduleName The module display name.
     * @param moduleDescription The module description text.
     * @param settings The list of setting components to display.
     */
    public ModuleSettingsScreen(
        Screen parentScreen,
        String moduleName,
        String moduleDescription,
        List<AbstractSettingComponent> settings
    ) {
        super(Component.empty());

        this.parentScreen = parentScreen;
        this.moduleName = moduleName;
        this.moduleDescription = moduleDescription;
        this.settings = settings;

        this.maxWidth = 480;
        this.maxHeight = 320;
    }

    /**
     * Initializes the screen and creates the navigation button.
     */
    @Override
    protected void init() {
        super.init();

        int btnWidth = 100;
        int btnHeight = 22;

        int btnX = this.width / 2 - btnWidth / 2;
        int btnY = (this.height / 2)
            + (this.maxHeight / 2)
            - btnHeight
            - 15;

        this.backButton = CustomButton.builder(
                Component.literal("Back")
            )
            .position(btnX, btnY)
            .size(btnWidth, btnHeight)
            .onPress(button ->
                this.minecraft.setScreen(this.parentScreen)
            )
            .style(style -> style
                .backgroundColors(
                    ButtonConstants.DEFAULT_BACKGROUND_COLOR,
                    ButtonConstants.DEFAULT_BACKGROUND_HOVER_COLOR,
                    ButtonConstants.DEFAULT_BACKGROUND_DISABLED_COLOR
                )
                .textColors(
                    ButtonConstants.DEFAULT_TEXT_COLOR,
                    ButtonConstants.DEFAULT_TEXT_HOVER_COLOR,
                    ButtonConstants.DEFAULT_TEXT_DISABLED_COLOR
                )
                .borderColors(
                    ButtonConstants.DEFAULT_BORDER_COLOR,
                    ButtonConstants.DEFAULT_BORDER_DISABLED_COLOR
                )
            )
            .build();
    }

    /**
     * Returns the title displayed in the window header.
     *
     * @return The screen title.
     */
    @Override
    protected String getWindowTitle() {
        return "Settings";
    }

    /**
     * Renders the module title, description, setting components,
     * scrollable content area and navigation controls.
     *
     * @param graphics Rendering context.
     * @param x1 Left window boundary.
     * @param x2 Right window boundary.
     * @param y1 Top window boundary.
     * @param y2 Bottom window boundary.
     * @param mouseX Current mouse X position.
     * @param mouseY Current mouse Y position.
     * @param progress Animation progress factor.
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
        var font = this.minecraft.font;
        int contentX = x1 + 25;
        int contentWidth = (x2 - x1) - 50;
        int titleY = y1 + 55;
        graphics.text(
            font,
            Component.literal(this.moduleName.toUpperCase()),
            contentX,
            titleY,
            getAlphaColor(0xB71C1C, progress),
            false
        );

        int descY = titleY + 16;
        var pose = graphics.pose();
        pose.pushMatrix();

        float descScale = 0.9f;
        pose.scale(descScale, descScale);
        graphics.text(
            font,
            Component.literal(this.moduleDescription),
            (int) (contentX / descScale),
            (int) (descY / descScale),
            getAlphaColor(0xAAAAAA, progress),
            false
        );
        pose.popMatrix();

        int sepY = descY + 15;
        graphics.fill(
            contentX,
            sepY,
            contentX + contentWidth,
            sepY + 1,
            getAlphaColor(0x333333, progress)
        );

        int startY = sepY + 10;
        int endY = y2 - 45;
        int visibleHeight = endY - startY;
        int currentY = (int) (startY - this.scrollOffset);
        boolean anyComponentHovered = false;

        for (AbstractSettingComponent setting : this.settings) {
            if (currentY + setting.getHeight() > startY
                && currentY < endY) {

                setting.render(
                    graphics,
                    font,
                    contentX,
                    currentY,
                    contentWidth,
                    mouseX,
                    mouseY,
                    progress
                );

                if (setting.isMouseOver(
                    mouseX,
                    mouseY,
                    contentX,
                    currentY,
                    contentWidth
                )) {
                    anyComponentHovered = true;
                }
            }

            currentY += setting.getHeight() + 10;
        }

        boolean isButtonHovered = false;

        if (this.backButton != null) {
            int btnX = this.width / 2
                - this.backButton.getWidth() / 2;

            int btnY = y2 - 32;
            this.backButton.setX(btnX);
            this.backButton.setY(btnY);

            isButtonHovered =
                mouseX >= btnX
                    && mouseX <= btnX + this.backButton.getWidth()
                    && mouseY >= btnY
                    && mouseY <= btnY + this.backButton.getHeight();

            this.backButton.render(
                graphics,
                mouseX,
                mouseY,
                0.0f
            );
        }

        GLFW.glfwSetCursor(
            this.minecraft.getWindow().handle(),
            (anyComponentHovered || isButtonHovered)
                ? HAND_CURSOR
                : ARROW_CURSOR
        );

        int totalContentHeight =
            this.settings.stream()
                .mapToInt(setting -> setting.getHeight() + 10)
                .sum()
                - 10;

        this.maxScrollOffset = Math.max(
            0,
            totalContentHeight - visibleHeight
        );
    }

    /**
     * Handles mouse click interactions for the back button
     * and all visible setting components.
     *
     * @param event Mouse button event.
     * @param doubleClick Whether the click belongs to a double-click sequence.
     * @return True if the event was consumed.
     */
    @Override
    public boolean mouseClicked(
        MouseButtonEvent event,
        boolean doubleClick
    ) {
        int targetWidth = Math.min(this.width - 60, this.maxWidth);
        int targetHeight = Math.min(this.height - 60, this.maxHeight);
        int x1 = (this.width / 2) - (targetWidth / 2);
        int x2 = (this.width / 2) + (targetWidth / 2);
        int y1 = (this.height / 2) - (targetHeight / 2);
        int y2 = (this.height / 2) + (targetHeight / 2);
        int contentX = x1 + 25;
        int contentWidth = (x2 - x1) - 50;
        int startY = y1 + 106;
        int endY = y2 - 45;

        if (this.backButton != null) {
            int bX = this.backButton.getX();
            int bY = this.backButton.getY();
            int bW = this.backButton.getWidth();
            int bH = this.backButton.getHeight();

            if (event.x() >= bX
                && event.x() <= bX + bW
                && event.y() >= bY
                && event.y() <= bY + bH) {

                this.backButton.mouseClicked(
                    event,
                    doubleClick
                );

                return true;
            }
        }

        if (event.y() < startY || event.y() > endY) {
            return super.mouseClicked(event, doubleClick);
        }

        int currentY = (int) (startY - this.scrollOffset);

        for (AbstractSettingComponent setting : this.settings) {
            if (currentY + setting.getHeight() > startY
                && currentY < endY) {

                if (setting.mouseClicked(
                    event.x(),
                    event.y(),
                    event.button(),
                    contentX,
                    currentY,
                    contentWidth
                )) {
                    return true;
                }
            }

            currentY += setting.getHeight() + 10;
        }

        return super.mouseClicked(event, doubleClick);
    }

    /**
     * Handles vertical scrolling inside the settings list.
     *
     * @param mouseX Mouse X position.
     * @param mouseY Mouse Y position.
     * @param scrollX Horizontal scroll amount.
     * @param scrollY Vertical scroll amount.
     * @return True if the event was processed.
     */
    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double scrollX,
        double scrollY
    ) {
        this.scrollOffset = Math.clamp(
            this.scrollOffset - scrollY * 18,
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
     * Dispatches keyboard input to setting components and
     * handles Escape-based navigation.
     *
     * @param event Keyboard event.
     * @return True if the event was consumed.
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        for (AbstractSettingComponent setting : this.settings) {
            if (setting.keyPressed(event)) {
                return true;
            }
        }

        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.setScreen(this.parentScreen);
            return true;
        }

        return super.keyPressed(event);
    }

    /**
     * Applies an alpha channel value to a RGB color.
     *
     * @param rgb Base RGB color.
     * @param alpha Alpha value between 0.0 and 1.0.
     * @return Combined ARGB color.
     */
    private int getAlphaColor(
        int rgb,
        float alpha
    ) {
        return ((int) (alpha * 255) << 24) | rgb;
    }
}
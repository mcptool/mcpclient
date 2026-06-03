package dev.wrrulosdev.mcpclient.client.screens;

import dev.wrrulosdev.mcpclient.client.constants.ButtonConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class CustomButton extends AbstractWidget {

    private final PressAction pressAction;
    private final ButtonStyle style;
    private Tooltip tooltip;

    private static Object lastScreen = null;
    private static CustomButton hoveredButton = null;
    private static long handCursor = 0;

    /**
     * Private constructor to initialize the custom button.
     * @param x           The X position on the screen.
     * @param y           The Y position on the screen.
     * @param width       The width of the button.
     * @param height      The height of the button.
     * @param message     The text component displayable on the button.
     * @param style       The structural and visual configuration of the button.
     * @param pressAction The action execution callback when the button is clicked.
     */
    private CustomButton(int x, int y, int width, int height, Component message,
                         ButtonStyle style, PressAction pressAction) {
        super(x, y, width, height, message);
        this.style = style;
        this.pressAction = pressAction;
    }

    /**
     * Extracts and processes the visual widget state to render background, borders, images, and texts.
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param mouseX   The current X coordinate of the cursor.
     * @param mouseY   The current Y coordinate of the cursor.
     * @param delta    The partial ticks time delta elapsed since last tick frame.
     */
    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        updateMouseCursor();

        if (this.tooltip != null) {
            this.setTooltip(this.tooltip);
        }

        if (!style.transparent) {
            int bgColor = getBackgroundColor();
            renderRoundedBackground(graphics, getX(), getY(), width, height, bgColor);
        }

        if (style.borderEnabled) {
            renderBorder(graphics);
        }

        if (style.texture != null || style.hoverTexture != null) {
            renderButtonImage(graphics);
        }

        if (!getMessage().getString().isEmpty()) {
            renderButtonText(graphics);
        }
    }

    /**
     * Generates a procedurally constructed background matrix layer applying a localized 2-pixel corner indentation sequence.
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param x        The relative horizontal screen coordinate offset.
     * @param y        The relative vertical screen coordinate offset.
     * @param width    The complete pixel scale length bounds metric of the component.
     * @param height   The complete pixel scale vertical height bounds metric of the component.
     * @param color    The designated integer hexadecimal ARGB standard profile color payload value.
     */
    private void renderRoundedBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x + 2, y, x + width - 2, y + 1, color);

        graphics.fill(x + 1, y + 1, x + width - 1, y + 2, color);

        graphics.fill(x, y + 2, x + width, y + height - 2, color);

        graphics.fill(x + 1, y + height - 2, x + width - 1, y + height - 1, color);

        graphics.fill(x + 2, y + height - 1, x + width - 2, y + height, color);
    }

    /**
     * Evaluates the hover state to dynamically swap the native window cursor to an interactive hand configuration.
     */
    private void updateMouseCursor() {
        var currentScreen = Minecraft.getInstance().screen;
        long windowHandle = Minecraft.getInstance().getWindow().handle();

        if (currentScreen != lastScreen) {
            lastScreen = currentScreen;
            if (hoveredButton != null) {
                hoveredButton = null;
                GLFW.glfwSetCursor(windowHandle, 0);
            }
        }

        if (this.isHovered() && this.active) {
            if (hoveredButton != this) {
                hoveredButton = this;
                if (handCursor == 0) {
                    handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
                }
                GLFW.glfwSetCursor(windowHandle, handCursor);
            }
        } else {
            if (hoveredButton == this) {
                hoveredButton = null;
                GLFW.glfwSetCursor(windowHandle, 0);
            }
        }
    }

    /**
     * Calculates the correct background color variant based on active and hover states.
     * @return The dynamic standard integer ARGB representation hex color.
     */
    private int getBackgroundColor() {
        if (!this.active) return style.disabledBackgroundColor;
        return this.isHovered() ? style.hoverBackgroundColor : style.normalBackgroundColor;
    }

    /**
     * Draws segment vectors mapping an identical customized rounded profile structure boundary across target coordinate scales.
     * @param graphics The screen graphics rendering pipeline extractor context.
     */
    private void renderBorder(GuiGraphicsExtractor graphics) {
        int borderColor = this.active ? style.borderColor : style.disabledBorderColor;
        int x = getX();
        int y = getY();

        graphics.fill(x + 2, y, x + width - 2, y + 1, borderColor);
        graphics.fill(x + 2, y + height - 1, x + width - 2, y + height, borderColor);

        graphics.fill(x, y + 2, x + 1, y + height - 2, borderColor);
        graphics.fill(x + width - 1, y + 2, x + width, y + height - 2, borderColor);

        graphics.fill(x + 1, y + 1, x + 2, y + 2, borderColor);
        graphics.fill(x + width - 2, y + 1, x + width - 1, y + 2, borderColor);
        graphics.fill(x + 1, y + height - 2, x + 2, y + height - 1, borderColor);
        graphics.fill(x + width - 2, y + height - 2, x + width - 1, y + height - 1, borderColor);
    }

    /**
     * Binds and draws a specified graphical asset frame within the center boundary.
     * @param graphics The screen graphics rendering pipeline extractor context.
     */
    private void renderButtonImage(GuiGraphicsExtractor graphics) {
        int imageX = getX() + (width - style.imageWidth) / 2;
        int imageY = getY() + (height - style.imageHeight) / 2;

        Identifier currentTexture = (this.isHovered() && style.hoverTexture != null) ? style.hoverTexture : style.texture;

        if (currentTexture != null) {
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                currentTexture,
                imageX,
                imageY,
                style.imageU,
                style.imageV,
                style.imageWidth,
                style.imageHeight,
                style.imageWidth,
                style.imageHeight,
                style.textureWidth,
                style.textureHeight
            );
        }
    }

    /**
     * Resolves the font properties to position text perfectly centralized.
     * @param graphics The screen graphics rendering pipeline extractor context.
     */
    private void renderButtonText(GuiGraphicsExtractor graphics) {
        var font = Minecraft.getInstance().font;
        int textColor = getTextColor();
        Component buttonText = getMessage();

        int textWidth = font.width(buttonText);
        int xPosition = getX() + (width - textWidth) / 2;
        int yPosition = getY() + (height - font.lineHeight) / 2;

        graphics.text(font, buttonText, xPosition, yPosition, textColor, true);
    }

    /**
     * Resolves the label text format color evaluating active condition parameters.
     * @return The active, standard, or disabled hex color value.
     */
    private int getTextColor() {
        if (!this.active) return style.disabledTextColor;
        return this.isHovered() ? style.hoverTextColor : style.normalTextColor;
    }

    /**
     * Triggers the functional action logic handler when a pointer clicks inside boundaries.
     * @param event       The encapsulation mouse cursor signal arguments.
     * @param doubleClick True if action was registered sequentially under rapid response time frame.
     */
    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (this.pressAction != null) {
            this.pressAction.onPress(this);
        }
    }

    /**
     * Handles accessibility tools and reader narratives updating screen changes.
     * @param builder The targeted container handling output feedback context structure information.
     */
    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput builder) {
    }

    /**
     * Creates a new instance of the modular builder configuration stack helper.
     * @param message The label text component applied directly on button instantiation.
     * @return An incremental chainable Builder utility object instance.
     */
    public static Builder builder(Component message) {
        return new Builder(message);
    }

    /**
     * Helper factory function generating simplified standard buttons layout setups quickly.
     * @param message     The label string text wrapper logic.
     * @param pressAction The execution listener trigger block.
     * @param x           The structural horizontally aligned position coordinate.
     * @param y           The structural vertically aligned position coordinate.
     * @param width       The targeted total thickness horizontal bounding frame box dimension.
     * @param height      The targeted total vertical layout frame dimension.
     * @return A tailored built CustomButton sequence configuration setup.
     */
    public static CustomButton create(Component message, PressAction pressAction, int x, int y, int width, int height) {
        return create(message, pressAction, x, y, width, height, null, true);
    }

    /**
     * Complete customized factory generator to structure widgets detailing hover details directly.
     * @param message     The descriptive textual payload component wrapper.
     * @param pressAction The runtime execution method trigger target.
     * @param x           Horizontal screen coordinates point placement.
     * @param y           Vertical screen coordinates point placement.
     * @param width       Horizontal boundary length size metrics.
     * @param height      Vertical boundary elevation height layout metrics.
     * @param tooltip     Optional contextual floating metadata hovering summary block box display elements.
     * @param active      The starting interaction state value flag condition indicator logic.
     * @return A ready-for-use active structural CustomButton entity block setup.
     */
    public static CustomButton create(Component message, PressAction pressAction, int x, int y, int width, int height, @Nullable Tooltip tooltip, boolean active) {
        CustomButton button = new CustomButton(x, y, width, height, message, new ButtonStyle(), pressAction);
        button.active = active;
        if (tooltip != null) {
            button.tooltip = tooltip;
            button.setTooltip(tooltip);
        }
        return button;
    }

    public static class Builder {
        private final Component message;
        private int x;
        private int y;
        private int width = 200;
        private int height = 20;
        private final ButtonStyle style = new ButtonStyle();
        private PressAction pressAction;
        private Tooltip tooltip;
        private boolean active = true;

        /**
         * Builder constructor assigning mandatory core localized descriptive values.
         * @param message The label component target.
         */
        public Builder(Component message) {
            this.message = message;
        }

        /**
         * Sets relative screen coordinate assignments inside modern frame logic layouts.
         * @param x Horizontal pixel position coordinate offset.
         * @param y Vertical pixel position coordinate offset.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Configures absolute rectangular dimension specifications framework definitions.
         * @param width  Horizontal pixel boundary layout length metrics target.
         * @param height Vertical frame box thickness dimension length metrics.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Alternative wrapper utility referencing layout dimensions assignment mapping values directly.
         * @param width  Horizontal design layout bounds space size configuration.
         * @param height Vertical spacing elevation boundary height coordinate size.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder size(int width, int height) {
            return dimensions(width, height);
        }

        /**
         * Modifies visual attributes dynamically exposing modular background styles variables context hooks.
         * @param styleConsumer The block handler function setting contextual color definitions configuration variables.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder style(Consumer<ButtonStyle> styleConsumer) {
            styleConsumer.accept(this.style);
            return this;
        }

        /**
         * Hooks standard click actions directly into final initialization pipelines.
         * @param pressAction The executable operation implementation logic callback block.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder onPress(PressAction pressAction) {
            this.pressAction = pressAction;
            return this;
        }

        /**
         * Appends explicit localized floating tip contexts text elements on target mouse hover occurrences.
         * @param tooltip The targeted floating overlay context component.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        /**
         * Toggles native user interaction validation behaviors globally during layout frames creation lifecycle.
         * @param active True if interactions must respond properly, false if skipped entirely.
         * @return The updated Builder execution context reference pipeline.
         */
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        /**
         * Collects structured build configurations pipeline arguments data formatting and return operations object targets.
         * @return A newly initialized CustomButton matching context options properties data.
         */
        public CustomButton build() {
            CustomButton button = new CustomButton(x, y, width, height, message, style, pressAction);
            button.active = active;
            if (tooltip != null) {
                button.tooltip = tooltip;
                button.setTooltip(tooltip);
            }
            return button;
        }
    }

    public static class ButtonStyle {
        private boolean transparent = false;
        private boolean borderEnabled = true;

        private int normalBackgroundColor = ButtonConstants.DEFAULT_BACKGROUND_COLOR;
        private int hoverBackgroundColor = ButtonConstants.DEFAULT_BACKGROUND_HOVER_COLOR;
        private int disabledBackgroundColor = ButtonConstants.DEFAULT_BACKGROUND_DISABLED_COLOR;

        private int normalTextColor = ButtonConstants.DEFAULT_TEXT_COLOR;
        private int hoverTextColor = ButtonConstants.DEFAULT_TEXT_HOVER_COLOR;
        private int disabledTextColor = ButtonConstants.DEFAULT_TEXT_DISABLED_COLOR;

        private int borderColor = ButtonConstants.DEFAULT_BORDER_COLOR;
        private int disabledBorderColor = ButtonConstants.DEFAULT_BORDER_DISABLED_COLOR;

        private Identifier texture = null;
        private Identifier hoverTexture = null;
        private int imageU = 0;
        private int imageV = 0;
        private int imageWidth = 16;
        private int imageHeight = 16;
        private int textureWidth = 256;
        private int textureHeight = 256;

        /**
         * Updates standard widget background render state profiles hiding backgrounds safely.
         * @param transparent Toggles transparency visibility mode.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle transparent(boolean transparent) {
            this.transparent = transparent;
            return this;
        }

        /**
         * Sets visibility boundaries criteria over borders outline layer components layouts.
         * @param enabled Toggles the borders rendering sequence layer flags visibility status.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle border(boolean enabled) {
            this.borderEnabled = enabled;
            return this;
        }

        /**
         * Sets standard color profiles arrays matching explicit interaction behaviors state context values.
         * @param normal   Standard idle condition state hex background configuration profile colors context code.
         * @param hover    Active visual focus hover hex colors code background configurations profile context metrics.
         * @param disabled Unresponsive state inactive background transparency color layouts values hex profiles.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle backgroundColors(int normal, int hover, int disabled) {
            this.normalBackgroundColor = normal;
            this.hoverBackgroundColor = hover;
            this.disabledBackgroundColor = disabled;
            return this;
        }

        /**
         * Changes text components color configuration profiles metrics dynamically during execution phases.
         * @param normal   Standard text color hex value representation format context variables layout.
         * @param hover    Active focus cursor text tracking color hex parameters metric representation settings.
         * @param disabled Inactive muted font rendering label colors configuration parameters hex values metrics.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle textColors(int normal, int hover, int disabled) {
            this.normalTextColor = normal;
            this.hoverTextColor = hover;
            this.disabledTextColor = disabled;
            return this;
        }

        /**
         * Adjusts lines border frame colors settings profiles structures directly.
         * @param normal   Standard bounding frame profile border layout line colors hex value notation format configurations.
         * @param disabled Disrupted component structure border layer display line values colors notation profile parameters.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle borderColors(int normal, int disabled) {
            this.borderColor = normal;
            this.disabledBorderColor = disabled;
            return this;
        }

        /**
         * Binds specific asset package identifiers paths coordinates directly inside canvas renderer matrices layers.
         * @param texture The asset pointer storage resource Identifier path path string mapping.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle image(Identifier texture) {
            this.texture = texture;
            return this;
        }

        /**
         * Binds a designated focus overlay texture layout to use exclusively when user interaction triggers pointer targeting variables.
         * @param hoverTexture The graphical target texture asset configuration wrapper structure.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle hoverImage(Identifier hoverTexture) {
            this.hoverTexture = hoverTexture;
            return this;
        }

        /**
         * Overlays advanced resource sheet configurations tracking default canvas size constraints layouts boundaries profiles parameters.
         * @param texture      The target resource sheet structural location model metadata identifier component.
         * @param u            Horizontal texture coordinate frame layout reading position pixel metrics tracker index.
         * @param v            Vertical texture pixel mapping frame reading baseline offset coordinates tracker index.
         * @param regionWidth  Horizontal surface dimensions length size matching layout pixels properties selection profiles.
         * @param regionHeight Vertical frame section measurement dimensions size specification pixels mapping values configurations bounds.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle image(Identifier texture, int u, int v, int regionWidth, int regionHeight) {
            return image(texture, u, v, regionWidth, regionHeight, 256, 256);
        }

        /**
         * Complete graphical asset grid tracking assignment configuration parameters for customized resource sprite mapping sizes logic.
         * @param texture       The target asset resource identity identifier model source package path structure pointer.
         * @param u             Resource UV mapping coordinates horizontal layout origin location indices parameters.
         * @param v             Resource UV texture coordinates vertical offset tracking origin baseline selection index parameters.
         * @param regionWidth   Targeted individual source image section dimensions layout pixels space size selection lengths.
         * @param regionHeight  Targeted section frame measurements vertical segment boundary elevation space measurement units.
         * @param textureWidth  Total graphical texture image template horizontal resolution pixel canvas metrics length layout scales.
         * @param textureHeight Total asset texture grid canvas graphic source vertical frame pixel density bounds scales.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle image(Identifier texture, int u, int v, int regionWidth, int regionHeight,
                                 int textureWidth, int textureHeight) {
            this.texture = texture;
            this.imageU = u;
            this.imageV = v;
            this.imageWidth = regionWidth;
            this.imageHeight = regionHeight;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            return this;
        }

        /**
         * Resizes rendered image metrics on user components directly on screen frames boundaries structures layout spaces.
         * @param width  Horizontal resolution element pixel boundary scale dimensions metric specs.
         * @param height Vertical layout segment thickness canvas display resolution frame units properties values.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle imageSize(int width, int height) {
            this.imageWidth = width;
            this.imageHeight = height;
            return this;
        }

        /**
         * Modifies reading indexes offsets tracking locations internally across texture canvas grids sheets mapping properties.
         * @param u Horizontal pixel texture sheet atlas map grid reading coordinate location value parameter alignment.
         * @param v Vertical texturing coordinate selection matrix frame reading axis value location position definitions.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle imagePosition(int u, int v) {
            this.imageU = u;
            this.imageV = v;
            return this;
        }

        /**
         * Overrides canvas boundaries metrics checking data mappings resolutions on custom template properties directly.
         * @param width  Horizontal file format matrix baseline canvas scaling layout dimension pixels space specifications.
         * @param height Vertical resolution configuration files source template sheet pixel parameters layout mapping measurements.
         * @return The current configuration instance state reference frame context.
         */
        public ButtonStyle textureDimensions(int width, int height) {
            this.textureWidth = width;
            this.textureHeight = height;
            return this;
        }
    }

    @FunctionalInterface
    public interface PressAction {
        /**
         * Execution method triggered synchronously during valid button interact event actions operations contexts pipeline frames loops.
         * @param button The interactive CustomButton context origin generating the caller signal.
         */
        void onPress(CustomButton button);
    }
}
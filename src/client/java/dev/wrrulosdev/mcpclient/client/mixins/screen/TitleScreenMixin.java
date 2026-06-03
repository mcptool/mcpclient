package dev.wrrulosdev.mcpclient.client.mixins.screen;

import dev.wrrulosdev.mcpclient.client.constants.ButtonConstants;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.screens.CustomButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    /**
     * Protected fallback constructor assigning an empty descriptive literal screen name context.
     */
    protected TitleScreenMixin() {
        super(Component.literal(""));
    }

    /**
     * Intercepts the screen initialization setup entry point to inject custom main menu interaction buttons.
     * @param ci The mixin callback info handle controlling invocation properties.
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        // Singleplayer
        this.addRenderableWidget(
            CustomButton.builder(Component.literal("Singleplayer"))
                .position(this.width / 2 - 80, this.height / 4 + 60)
                .size(159, 20)
                .tooltip(
                    Tooltip.create(
                        Component.literal("Play on your own worlds")
                    )
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
                .onPress(button -> {
                    Minecraft.getInstance().setScreen(new SelectWorldScreen(this));
                })
                .build()
        );

        // Multiplayer
        this.addRenderableWidget(
            CustomButton.builder(Component.literal("Multiplayer"))
                .position(this.width / 2 - 80, this.height / 4 + 85)
                .size(159, 20)
                .tooltip(
                    Tooltip.create(
                        Component.literal("Join multiplayer servers")
                    )
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
                .onPress(button -> {
                    Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(this));
                })
                .build()
        );

        // MCPTool Functions
        this.addRenderableWidget(
            CustomButton.builder(Component.literal("MCPTool Functions"))
                .position(this.width / 2 - 80, this.height / 4 + 110) // Toma el lugar de +110
                .size(159, 20)
                .tooltip(
                    Tooltip.create(
                        Component.literal("Open MCPTool features")
                    )
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
                .onPress(button -> {
                    System.out.println("MCPTool Functions button clicked!");
                })
                .build()
        );

        // Settings
        this.addRenderableWidget(
            CustomButton.builder(Component.literal("Settings"))
                .position(this.width / 2 - 80, this.height / 4 + 135) // Bajó de +110 a +135
                .size(76, 20)
                .tooltip(
                    Tooltip.create(Component.literal("Open settings"))
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
                .onPress(button -> {
                    Minecraft.getInstance().setScreen(new OptionsScreen(this, Minecraft.getInstance().options, false));
                })
                .build()
        );

        // Quit Game
        this.addRenderableWidget(
            CustomButton.builder(Component.literal("Quit Game"))
                .position(this.width / 2 + 3, this.height / 4 + 135) // Bajó de +110 a +135
                .size(76, 20)
                .tooltip(
                    Tooltip.create(Component.literal("Exit the game"))
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
                .onPress(button -> {
                    Minecraft.getInstance().stop();
                })
                .build()
        );

        // Discord
        this.addRenderableWidget(
            CustomButton.builder(Component.empty())
                .position(this.width - 30, this.height - 27)
                .size(22, 21)
                .tooltip(
                    Tooltip.create(
                        Component.literal("Join our Discord server")
                    )
                )
                .style(style -> style
                    .border(false)
                    .transparent(true)
                    .image(
                        TextureConstants.DISCORD_ICON,
                        0,
                        0,
                        36,
                        36,
                        36,
                        36
                    )
                )
                .onPress(button -> {
                    Minecraft.getInstance().keyboardHandler.setClipboard(
                        "https://discord.mcptool.net"
                    );
                })
                .build()
        );

        // Github
        this.addRenderableWidget(
            CustomButton.builder(Component.empty())
                .position(this.width - 55, this.height - 25)
                .size(18, 18)
                .tooltip(
                    Tooltip.create(
                        Component.literal("View Project Source Code")
                    )
                )
                .style(style -> style
                    .border(false)
                    .transparent(true)
                    .image(
                        TextureConstants.GITHUB_ICON,
                        0,
                        0,
                        30,
                        30,
                        30,
                        30
                    )
                )
                .onPress(button -> {
                    Minecraft.getInstance().keyboardHandler.setClipboard(
                        "https://github.com/wrrulosdev/mcpclient"
                    );
                })
                .build()
        );

        // Spoofing
        this.addRenderableWidget(
            CustomButton.builder(Component.empty())
                .position(this.width - 35, 10)
                .size(26, 26)
                .tooltip(
                    Tooltip.create(
                        Component.literal("MCPTool Website!")
                    )
                )
                .style(style -> style
                    .border(false)
                    .transparent(true)
                    .image(
                        TextureConstants.URL_ICON,
                        0,
                        0,
                        30,
                        30,
                        30,
                        30
                    )
                    .hoverImage(TextureConstants.URL_HOVER_ICON)
                )
                .onPress(button -> {
                    Minecraft.getInstance().keyboardHandler.setClipboard(
                        "https://discord.mcptool.net"
                    );
                })
                .build()
        );

        ci.cancel();
    }

    /**
     * Intercepts the screen rendering pipeline storage loop to modify the underlying system engine version signature string.
     * @param originalVersion The original version string captured inside local variable slots allocations index.
     * @return The updated custom string formatting applied into current stack assignments.
     */
    @ModifyVariable(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "STORE"),
        ordinal = 0
    )
    private String changeVersionString(String originalVersion) {
        return "MCPClient v" + ClientConstants.VERSION;
    }

    /**
     * Cancels standard game logotype processing to blit a custom logo.
     * @param graphics    The screen graphics rendering pipeline extractor context.
     * @param mouseX      The current X coordinate of the cursor pointer position tracking index.
     * @param mouseY      The current Y coordinate of the cursor pointer position tracking index.
     * @param partialTick The runtime delta timing variable metric scales.
     * @param ci          The mixin callback info handle controlling invocation properties.
     */
    @Inject(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/LogoRenderer;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IF)V"
        ),
        cancellable = true
    )
    private void renderCustomLogo(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        TitleScreen screen = (TitleScreen)(Object)this;
        int desiredWidth = 50 + (int)(screen.width * 0.12);
        int drawWidth = net.minecraft.util.Mth.clamp(desiredWidth, 50, 120);
        int drawHeight = (int) (drawWidth / 1.2);
        int logoX = (screen.width / 2) - (drawWidth / 2);
        int logoY = screen.height / 8;
        int color = ARGB.white(1.0F);
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            TextureConstants.LOGO,
            logoX, logoY,
            0.0F, 0.0F,
            drawWidth, drawHeight,
            drawWidth, drawHeight,
            color
        );
    }
}

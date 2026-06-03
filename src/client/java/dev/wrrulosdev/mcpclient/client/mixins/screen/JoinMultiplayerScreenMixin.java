package dev.wrrulosdev.mcpclient.client.mixins.screen;

import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.JoinMultiplayerScreenAccessor;
import dev.wrrulosdev.mcpclient.client.screens.CustomButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen {

    protected JoinMultiplayerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void afterInit(CallbackInfo ci) {
        JoinMultiplayerScreenAccessor acc = (JoinMultiplayerScreenAccessor) this;

        Button selectButton = acc.getSelectButton();
        selectButton.visible = false;
        selectButton.active = false;

        Button editButton = acc.getEditButton();
        editButton.visible = false;
        editButton.active = false;

        Button deleteButton = acc.getDeleteButton();
        deleteButton.visible = false;
        deleteButton.active = false;

        CustomButton discordButton = CustomButton.builder(Component.empty())
            .position(5, 5)
            .size(22, 21)
            .tooltip(Tooltip.create(Component.literal("Return to Menu")))
            .style(style -> style
                .border(false)
                .transparent(true)
                .image(
                    TextureConstants.HOME_ICON,
                    0, 0, 25, 25, 25, 25
                )
                .hoverImage(TextureConstants.HOME_HOVER_ICON)
            )
            .onPress(button -> {
                Minecraft.getInstance().setScreen(new TitleScreen());
            })
            .build();

        this.addRenderableWidget(discordButton);
    }
}
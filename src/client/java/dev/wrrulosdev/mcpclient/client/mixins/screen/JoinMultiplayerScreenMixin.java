package dev.wrrulosdev.mcpclient.client.mixins.screen;

import dev.wrrulosdev.mcpclient.client.mixins.accessor.JoinMultiplayerScreenAccessor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreenMixin {

    /**
     * Intercepts the tail end of the multiplayer screen initialization to hide and disable standard interaction buttons.
     * In addition to adding custom icons.
     * @param ci The mixin callback info handle controlling invocation properties.
     */
    @Inject(method = "init", at = @At("TAIL"))
    private void afterInit(CallbackInfo ci) {

        JoinMultiplayerScreen self =
            (JoinMultiplayerScreen)(Object)this;

        JoinMultiplayerScreenAccessor acc =
            (JoinMultiplayerScreenAccessor) self;

        Button selectButton = acc.getSelectButton();
        selectButton.visible = false;
        selectButton.active = false;

        Button editButton = acc.getEditButton();
        editButton.visible = false;
        editButton.active = false;

        Button deleteButton = acc.getDeleteButton();
        deleteButton.visible = false;
        deleteButton.active = false;
    }
}
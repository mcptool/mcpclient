package dev.wrrulosdev.mcpclient.client.mixins.gui;

import com.llamalad7.mixinextras.sugar.Local;
import dev.wrrulosdev.mcpclient.client.hud.ClientHud;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;applyCursor(Lcom/mojang/blaze3d/platform/Window;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void onExtractRenderState(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci,
                                      @Local(ordinal = 0) GuiGraphicsExtractor graphics) {
        ClientHud.render(graphics, deltaTracker.getGameTimeDeltaPartialTick(false));
    }
}
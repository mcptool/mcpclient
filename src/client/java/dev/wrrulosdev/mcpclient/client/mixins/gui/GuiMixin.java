package dev.wrrulosdev.mcpclient.client.mixins.gui;

import com.llamalad7.mixinextras.sugar.Local;
import dev.wrrulosdev.mcpclient.client.hud.ClientHud;
import dev.wrrulosdev.mcpclient.client.hud.PlayerModel;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    /**
     * Injects rendering logic into the {@code extractRenderState} method.
     * This ensures the Client HUD and Player Model are drawn correctly before the
     * cursor is applied to the screen state.
     *
     * @param deltaTracker      The tracker for game delta time
     * @param shouldRenderLevel Whether the level should be rendered
     * @param resourcesLoaded   Whether game resources are currently loaded
     * @param ci                Callback info
     * @param graphics          The graphics extractor local variable
     */
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
        PlayerModel.renderPlayerModel(graphics);
    }
}
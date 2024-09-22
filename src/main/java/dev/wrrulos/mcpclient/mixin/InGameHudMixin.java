package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    /**
     * Cancel rendering of the scoreboard sidebar
     * @param context Draw context
     * @param renderTickCounter Render tick counter
     * @param ci Callback info
     */
    @Inject(method = "renderScoreboardSidebar*", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboardSidebar(DrawContext context, RenderTickCounter renderTickCounter, CallbackInfo ci) {
        MCPToolSettings settings = Mcpclient.getSettings();

        if (settings.isStealthMode()) {
            ci.cancel();
        }
    }
}

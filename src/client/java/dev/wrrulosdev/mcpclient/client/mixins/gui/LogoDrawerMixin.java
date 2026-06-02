package dev.wrrulosdev.mcpclient.client.mixins.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogoRenderer.class)
public class LogoDrawerMixin {

    /**
     * Intercepts the head of the logotype render state extractor to cancel default vanilla branding drawing routines.
     * @param graphics The screen graphics rendering pipeline extractor context.
     * @param width    The structural horizontal window layout alignment boundary calculation metrics.
     * @param alpha    The floating visibility scaling value opacity factor parameter.
     * @param ci       The mixin callback info handle controlling invocation properties.
     */
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void extractRenderState(
        GuiGraphicsExtractor graphics,
        int width,
        float alpha,
        CallbackInfo ci
    ) {
        ci.cancel();
    }
}
package dev.wrrulosdev.mcpclient.client.mixins.screen;

import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Panorama;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.PanoramaRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Panorama.class)
public class PanoramaMixin {

    /**
     * Intercepts the panorama background render state extraction to substitute the vanilla rotating background with a static texture constant definition.
     * @param graphics   The screen graphics rendering pipeline extractor context.
     * @param width      The current width boundary dimension of the application screen window viewport.
     * @param height     The current height boundary dimension of the application screen window viewport.
     * @param shouldSpin Flag determining if the structural background environment rotation sequence applies.
     * @param ci         The mixin callback info handle controlling invocation properties.
     */
    @Inject(
        method = "extractRenderState",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onExtractRenderState(
        GuiGraphicsExtractor graphics,
        int width,
        int height,
        boolean shouldSpin,
        CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.getGameRenderState().guiRenderState.panoramaRenderState = new PanoramaRenderState(0.0F);
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            TextureConstants.MENU_BACKGROUND,
            0, 0,
            0.0F, 0.0F,
            width, height,
            16, 128,
            16, 128
        );

        ci.cancel();
    }
}
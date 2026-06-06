package dev.wrrulosdev.mcpclient.client.mixins.render;

import dev.wrrulosdev.mcpclient.client.cheats.esp.BlockScanner;
import dev.wrrulosdev.mcpclient.client.cheats.esp.EspRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    /**
     * Updates block scanning data and renders ESP overlays
     * after the world rendering pass has completed.
     *
     * @param ci Callback information
     */
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void onRenderLevel(CallbackInfo ci) {
        BlockScanner.update(Minecraft.getInstance().player.blockPosition());
        EspRenderer.render();
    }
}
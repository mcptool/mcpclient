package dev.wrrulosdev.mcpclient.client.mixins;

import dev.wrrulosdev.mcpclient.client.esp.BlockScanner;
import dev.wrrulosdev.mcpclient.client.esp.EspRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void onRenderLevel(CallbackInfo ci) {
        BlockScanner.update(Minecraft.getInstance().player.blockPosition());
        EspRenderer.render();
    }
}
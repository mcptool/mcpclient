package dev.wrrulosdev.mcpclient.client.mixins.gui;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class WindowTitleMixin {
    @Inject(method = "createTitle", at = @At(value = "HEAD"), cancellable = true)
    private void changeMinecraftTitle(CallbackInfoReturnable<String> callback) {
        callback.setReturnValue("MCPClient " + ClientConstants.VERSION + " - " + SharedConstants.getCurrentVersion().name());
    }
}

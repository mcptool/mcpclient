package dev.wrrulosdev.mcpclient.client.mixins.world;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Display.TextDisplay.class)
public class TextDisplayMixin {

    @Inject(method = "getText", at = @At("RETURN"), cancellable = true)
    private void anonymizeTextDisplay(CallbackInfoReturnable<Component> cir) {
        if (!MCPClient.getSettingsManager().getClientSettings().isAnonymousHologramsEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        Component originalComponent = cir.getReturnValue();
        if (originalComponent == null) return;

        String oldName = minecraft.player.getName().getString();

        if (!originalComponent.getString().contains(oldName)) {
            return;
        }

        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();
        cir.setReturnValue(AnonymizerUtils.replaceTextWithRedName(originalComponent, oldName, newName));
    }
}

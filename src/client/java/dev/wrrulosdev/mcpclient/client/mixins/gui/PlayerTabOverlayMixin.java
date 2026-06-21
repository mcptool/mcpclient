package dev.wrrulosdev.mcpclient.client.mixins.gui;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    /**
     * Injects into the player tab list display name retrieval to anonymize the
     * local player's entry if the feature is enabled.
     *
     * @param info The player information for the entry being rendered
     * @param cir Callback information used to modify the returned display name component
     */
    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void modifyTabName(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        if (!MCPClient.getSettingsManager().getClientSettings().isAnonymousScoreboardEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !info.getProfile().id().equals(minecraft.player.getUUID())) {
            return;
        }

        Component originalComponent = cir.getReturnValue();
        if (originalComponent == null) {
            return;
        }

        String oldName = minecraft.player.getName().getString();
        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();
        cir.setReturnValue(AnonymizerUtils.replaceTextWithRedName(originalComponent, oldName, newName));
    }
}
package dev.wrrulosdev.mcpclient.client.mixins.world;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.options.Anonymous;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    /**
     * Injects into the entity custom name retrieval to anonymize the local player's
     * name if it appears in entity holograms (e.g., name tags or ArmorStand text).
     *
     * @param cir Callback information used to modify the returned component
     */
    @Inject(method = "getCustomName", at = @At("RETURN"), cancellable = true)
    private void anonymizeEntityHolograms(CallbackInfoReturnable<Component> cir) {
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();

        if (!Anonymous.INSTANCE.isEnabled() || !clientSettings.isAnonymousHologramsEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        Component originalComponent = cir.getReturnValue();

        if (originalComponent == null) {
            return;
        }

        String oldName = minecraft.player.getName().getString();

        if (!originalComponent.getString().contains(oldName)) {
            return;
        }

        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();
        cir.setReturnValue(AnonymizerUtils.replaceTextWithRedName(originalComponent, oldName, newName));
    }
}
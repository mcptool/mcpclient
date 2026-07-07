package dev.wrrulosdev.mcpclient.client.mixins.render;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.options.Anonymous;
import dev.wrrulosdev.mcpclient.client.options.NameTag;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.CC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.entity.EntityAttachment.NAME_TAG;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    /**
     * Injects custom logic at the end of the render state extraction process.
     * This allows modification of the final computed nametag before it is rendered.
     *
     * @param entity The entity being processed for rendering
     * @param state The render state generated for the entity
     * @param partialTicks The interpolation tick value used for smooth rendering
     * @param ci Callback information for the injection point
     */
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void appendCustomPrefix(T entity, S state, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();

        // Inventory
        if (mc.gui.screen() != null) {
            return;
        }

        // Player NameTag
        if (mc.player != null && entity == mc.player) {
            boolean playerNameTagEnabled = NameTag.INSTANCE.isEnabled();
            boolean anonymous = Anonymous.INSTANCE.isEnabled();
            boolean anonymousNameTag = clientSettings.isAnonymousNameTagsEnabled();

            if (!playerNameTagEnabled || mc.player.isCrouching()) {
                state.nameTag = null;
                return;
            }
            String name = (anonymous && anonymousNameTag)
                ? clientSettings.getNewAnonymousName()
                : entity.getName().getString();

            state.nameTag = Component.literal(name).setStyle(
                clientSettings.isNameTagColorEnabled()
                    ? Style.EMPTY.withColor(clientSettings.getNameTagColor())
                    : Style.EMPTY
            );

            state.nameTagAttachment = mc.player.getAttachments()
                .getNullable(NAME_TAG, 0, mc.player.getYRot(partialTicks));
        }

        // Custom Prefix
        if (entity instanceof Player && state.nameTag != null) {
            if (!clientSettings.getCustomPrefixUsernames().contains(entity.getName().getString()) || !clientSettings.isCustomPrefixEnabled()) return;
            state.nameTag = Component.empty()
                .append(CC.parseColorCodes(clientSettings.getCustomPrefix()))
                .append(state.nameTag);
        }
    }
}
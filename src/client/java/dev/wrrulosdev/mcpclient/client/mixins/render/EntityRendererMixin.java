package dev.wrrulosdev.mcpclient.client.mixins.render;

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

        /*
         * Ensures the local player always has a visible nametag.
         */
        if (mc.player != null && entity == mc.player) {
            state.nameTag = mc.player.getDisplayName();

            state.nameTagAttachment = mc.player.getAttachments()
                .getNullable(net.minecraft.world.entity.EntityAttachment.NAME_TAG, 0, mc.player.getYRot(partialTicks));
        }

        /*
         * Applies a custom prefix to player nametags.
         */
        if (entity instanceof Player && state.nameTag != null) {
            MutableComponent prefix = Component.literal("[MCP] ")
                .setStyle(Style.EMPTY
                    .withColor(0xFF0000)
                    .withBold(true));

            state.nameTag = Component.empty()
                .append(prefix)
                .append(state.nameTag);
        }
    }
}
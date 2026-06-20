package dev.wrrulosdev.mcpclient.client.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    private static final float DEFAULT_Y = 2.6F;
    private static final float SELF_Y = 2.6F; // 2.2
    private static final float ICON_SIZE = 0.60F;
    private static final int FULL_BRIGHT_LIGHT = 0x00F000F0;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * Injects custom rendering logic into the name display rendering stage
     *
     * @param state The render state of the avatar being rendered
     * @param poseStack The transformation stack used for rendering
     * @param collector The render submission collector used to queue geometry
     * @param camera The current camera render state
     * @param ci Callback information for the injection point
     */
    @Inject(method = "submitNameDisplay", at = @At("TAIL"))
    private void renderBadge(
        AvatarRenderState state,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        CameraRenderState camera,
        CallbackInfo ci
    ) {
        Minecraft mc = Minecraft.getInstance();
        boolean isSelf = mc.player != null && state.id == mc.player.getId();
        float yOffset = isSelf ? SELF_Y : DEFAULT_Y;

        poseStack.pushPose();
        applyTransform(poseStack, camera, yOffset);
        renderIcon(poseStack, collector);
        poseStack.popPose();
    }

    /**
     * Applies transformation to align the icon with the camera and position it above the avatar
     *
     * @param poseStack The transformation stack used for rendering
     * @param camera The current camera render state
     * @param yOffset Vertical offset applied to the icon position
     */
    private void applyTransform(PoseStack poseStack, CameraRenderState camera, float yOffset) {
        poseStack.translate(0.0F, yOffset, 0.0F);
        poseStack.mulPose(new Quaternionf(camera.orientation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-180.0F));
    }

    /**
     * Renders the icon quad using custom geometry submission
     *
     * @param poseStack The transformation stack used for rendering
     * @param collector The render submission collector used to queue geometry
     */
    private void renderIcon(PoseStack poseStack, SubmitNodeCollector collector) {
        collector.submitCustomGeometry(
            poseStack,
            RenderTypes.entityTranslucent(TextureConstants.LOGO),
            (pose, buffer) -> {
                buffer.addVertex(pose, -ICON_SIZE / 2, ICON_SIZE / 2, 0)
                    .setColor(WHITE)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(FULL_BRIGHT_LIGHT)
                    .setNormal(pose, 0, 0, 1);

                buffer.addVertex(pose, ICON_SIZE / 2, ICON_SIZE / 2, 0)
                    .setColor(WHITE)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(FULL_BRIGHT_LIGHT)
                    .setNormal(pose, 0, 0, 1);

                buffer.addVertex(pose, ICON_SIZE / 2, -ICON_SIZE / 2, 0)
                    .setColor(WHITE)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(FULL_BRIGHT_LIGHT)
                    .setNormal(pose, 0, 0, 1);

                buffer.addVertex(pose, -ICON_SIZE / 2, -ICON_SIZE / 2, 0)
                    .setColor(WHITE)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(FULL_BRIGHT_LIGHT)
                    .setNormal(pose, 0, 0, 1);
            }
        );
    }
}
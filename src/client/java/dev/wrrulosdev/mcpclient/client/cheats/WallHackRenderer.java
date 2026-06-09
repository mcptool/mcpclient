package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.world.phys.Vec3;

public class WallHackRenderer {

    private static final GizmoStyle BOX_STROKE = GizmoStyle.stroke(0xFFFF0000, 2.0F);
    private static final GizmoStyle BOX_FILL = GizmoStyle.fill(0x50FF0000);
    private static final int STICK_COLOR = 0xFFFFFFFF;
    private static final float STICK_WIDTH = 2.0F;
    private static final GizmoStyle STICK_STYLE = GizmoStyle.stroke(STICK_COLOR, STICK_WIDTH);

    /**
     * Renders ESP boxes around all visible player entities in the world,
     * excluding the local player and spectators.
     * Draws both an outlined and semi-transparent filled bounding box
     * that remains visible through walls.
     */
    public static void renderBoxes() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        for (Player player : mc.level.players()) {
            if (player == mc.player || mc.player.getName().contains(Component.nullToEmpty(" ")) || player.isSpectator()) continue;

            if (player.isSpectator()) {
                continue;
            }

            Gizmos.cuboid(player.getBoundingBox(), BOX_STROKE).setAlwaysOnTop();
            Gizmos.cuboid(player.getBoundingBox(), BOX_FILL).setAlwaysOnTop();
        }
    }

    /**
     * Renders animated stickman overlays for all player entities in the world,
     * excluding the local player and spectators.
     * Stickman positions are updated using player movement, attack animations,
     * body rotation, and crouching state to better match the current pose.
     */
    public static void renderStickMan() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);

        for (Player player : mc.level.players()) {
            if (player == mc.player || mc.player.getName().contains(Component.nullToEmpty(" ")) || player.isSpectator()) continue;
            drawAnimatedStickMan(player, partialTick);
        }
    }

    /**
     * Calculates and renders a skeletal StickMan representation for a player.
     * The skeleton is dynamically animated using walking, attacking, crouching,
     * and body rotation data to approximate the player's current pose.
     *
     * @param player Target player entity
     * @param partialTick Current frame interpolation value
     */
    private static void drawAnimatedStickMan(Player player, float partialTick) {
        float height = player.getBbHeight();
        float width = player.getBbWidth();
        Vec3 pos = player.position();

        Vec3 look = Vec3.directionFromRotation(0, player.yBodyRot);
        Vec3 right = new Vec3(-look.z, 0, look.x).normalize().scale(width * 0.7);
        Vec3 narrowRight = right.scale(0.5);

        float limbSwing = player.walkAnimation.position();
        float limbSwingAmount = player.walkAnimation.speed();
        if (limbSwingAmount > 1.0F) limbSwingAmount = 1.0F;

        float armSwing = Mth.sin(limbSwing * 0.6662F) * 2.0F * limbSwingAmount;
        float legSwing = Mth.sin(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        float attackProgress = player.getAttackAnim(partialTick);
        float sneakOffset = player.isCrouching() ? 0.3F : 0.0F;

        Vec3 headCenter = player.getEyePosition().add(0, 0.1, 0).subtract(0, sneakOffset, 0);
        Vec3 neck = player.getEyePosition().subtract(0, 0.2, 0).subtract(0, sneakOffset, 0);
        Vec3 pelvis = pos.add(0, height * 0.45, 0).subtract(0, sneakOffset * 0.5, 0); // La pelvis baja menos que la cabeza
        Vec3 leftShoulder = neck.subtract(right);
        Vec3 rightShoulder = neck.add(right);
        Vec3 leftHip = pelvis.subtract(narrowRight);
        Vec3 rightHip = pelvis.add(narrowRight);
        Vec3 leftHand = leftShoulder.subtract(0, height * 0.45, 0);
        Vec3 rightHand = rightShoulder.subtract(0, height * 0.45, 0);
        Vec3 leftFoot = pos.subtract(narrowRight);
        Vec3 rightFoot = pos.add(narrowRight);

        leftHand = leftHand.add(look.scale(-armSwing * 0.3));
        rightHand = rightHand.add(look.scale(armSwing * 0.3));
        leftFoot = leftFoot.add(look.scale(legSwing * 0.3));
        rightFoot = rightFoot.add(look.scale(-legSwing * 0.3));

        if (attackProgress > 0.0F) {
            float attackUp = Mth.sin(attackProgress * (float)Math.PI) * 0.5F;
            float attackForward = Mth.sin(attackProgress * (float)Math.PI) * 0.8F;
            rightHand = rightHand.add(look.scale(attackForward)).add(0, attackUp, 0);
        }

        Gizmos.circle(headCenter, 0.25F, STICK_STYLE).setAlwaysOnTop();
        Gizmos.line(neck, pelvis, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(leftShoulder, rightShoulder, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(leftShoulder, leftHand, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(rightShoulder, rightHand, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(leftHip, rightHip, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(leftHip, leftFoot, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
        Gizmos.line(rightHip, rightFoot, STICK_COLOR, STICK_WIDTH).setAlwaysOnTop();
    }
}

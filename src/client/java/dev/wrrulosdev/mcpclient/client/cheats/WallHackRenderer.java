package dev.wrrulosdev.mcpclient.client.cheats;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.GizmoStyle;

public class WallHackRenderer {

    private static final GizmoStyle STROKE = GizmoStyle.stroke(0xFFFF0000, 2.0F);
    private static final GizmoStyle FILL = GizmoStyle.fill(0x50FF0000);

    /**
     * Renders ESP boxes around all visible player entities in the world,
     * excluding the local player and spectators.
     * Draws both an outlined and semi-transparent filled bounding box
     * that remains visible through walls.
     */
    public static void render() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        for (Player player : mc.level.players()) {
            if (player == mc.player) {
                continue;
            }

            if (player.isSpectator()) {
                continue;
            }

            Gizmos.cuboid(player.getBoundingBox(), STROKE).setAlwaysOnTop();
            Gizmos.cuboid(player.getBoundingBox(), FILL).setAlwaysOnTop();
        }
    }
}
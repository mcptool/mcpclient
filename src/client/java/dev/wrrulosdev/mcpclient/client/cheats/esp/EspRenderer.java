package dev.wrrulosdev.mcpclient.client.cheats.esp;

import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.GizmoStyle;

import java.util.HashMap;
import java.util.Map;

public class EspRenderer {
    private static final Map<Integer, BlockStyles> styleCache = new HashMap<>();

    /**
     * Renders all currently scanned blocks using cached fill and outline styles.
     */
    public static void render() {
        if (BlockScanner.scannedBlocks.isEmpty()) {
            return;
        }

        for (BlockScanner.ScannedBlock scanned : BlockScanner.scannedBlocks) {
            BlockStyles styles = getOrCreateStyles(scanned.color());

            Gizmos.cuboid(scanned.pos(), 0.0F, styles.fillStyle).setAlwaysOnTop();
            Gizmos.cuboid(scanned.pos(), 0.0F, styles.strokeStyle).setAlwaysOnTop();
        }
    }

    /**
     * Retrieves cached rendering styles for the specified color or creates
     * them if they do not already exist.
     *
     * @param baseColorARGB Base ARGB color assigned to the scanned block
     * @return Cached or newly created style pair
     */
    private static BlockStyles getOrCreateStyles(int baseColorARGB) {
        return styleCache.computeIfAbsent(baseColorARGB, color -> {

            int fillARGB = (color & 0x00FFFFFF) | 0x80000000;

            GizmoStyle stroke = GizmoStyle.stroke(color, 2.0F);
            GizmoStyle fill = GizmoStyle.fill(fillARGB);

            return new BlockStyles(stroke, fill);
        });
    }

    /**
     * Groups outline and fill styles associated with a specific ESP color.
     *
     * @param strokeStyle Outline rendering style
     * @param fillStyle Fill rendering style
     */
    private record BlockStyles(GizmoStyle strokeStyle, GizmoStyle fillStyle) { }
}
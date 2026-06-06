package dev.wrrulosdev.mcpclient.client.esp;

import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.GizmoStyle;

public class EspRenderer {
    public static void render() {
        // Recorremos la lista de cofres que ya tienes
        for (BlockPos pos : ChestScanner.chestPositions) {

            // Usamos 0xFFFFA500 (Naranja, opacidad 255)
            // 'stroke' es el método que reemplaza a 'lines'
            GizmoStyle style = GizmoStyle.stroke(0xFFFFA500, 2.0F);

            // Dibujamos el cuboid
            // El segundo parámetro (padding) lo dejamos en 0.0F
            // para que el cubo encaje perfecto en el bloque
            Gizmos.cuboid(pos, 0.0F, style);
        }
    }
}
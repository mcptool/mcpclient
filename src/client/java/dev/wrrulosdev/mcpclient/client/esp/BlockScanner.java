package dev.wrrulosdev.mcpclient.client.esp;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockScanner {
    // Lista segura para multihilo que guardará el bloque y su color
    public static final List<ScannedBlock> scannedBlocks = new CopyOnWriteArrayList<>();

    // Registro de bloques a buscar y su color base (Formato ARGB: 0xAARRGGBB)
    private static final Map<Block, Integer> TARGET_BLOCKS = new HashMap<>();

    // Controladores de tiempo e hilos
    private static long lastScanTime = 0;
    private static boolean isScanning = false;

    static {
        // --- CONFIGURACIÓN DE BLOQUES Y COLORES ---
        // Cofres (Naranja)
        TARGET_BLOCKS.put(Blocks.CHEST, 0xFFFFA500);
        TARGET_BLOCKS.put(Blocks.TRAPPED_CHEST, 0xFFFF5500); // Naranja rojizo

        // Minerales Valiosos (Cian / Aqua)
        TARGET_BLOCKS.put(Blocks.DIAMOND_ORE, 0xFF00FFFF);
        TARGET_BLOCKS.put(Blocks.DEEPSLATE_DIAMOND_ORE, 0xFF00FFFF);

        // Oro (Amarillo/Dorado)
        TARGET_BLOCKS.put(Blocks.GOLD_ORE, 0xFFFFD700);
        TARGET_BLOCKS.put(Blocks.DEEPSLATE_GOLD_ORE, 0xFFFFD700);

        // Puedes agregar más aquí fácilmente...
    }

    public static void update(BlockPos playerPos) {
        long currentTime = System.currentTimeMillis();

        // Evitar escanear si no han pasado 2 seg o si el escaneo anterior aún no termina
        if (currentTime - lastScanTime < 2000 || isScanning) return;

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        lastScanTime = currentTime;
        isScanning = true;

        // 🚀 Ejecutar la búsqueda en un hilo de fondo (Asíncrono) para EVITAR EL LAG
        CompletableFuture.runAsync(() -> {
            try {
                List<ScannedBlock> tempFoundBlocks = new ArrayList<>();
                int radius = 64;

                // Escaneo de la zona
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            BlockPos currentPos = playerPos.offset(x, y, z);
                            BlockState state = level.getBlockState(currentPos);
                            Block block = state.getBlock();

                            // Búsqueda instantánea usando el HashMap (O(1))
                            Integer color = TARGET_BLOCKS.get(block);
                            if (color != null) {
                                tempFoundBlocks.add(new ScannedBlock(currentPos, color));
                            }
                        }
                    }
                }

                // Actualizar la lista que lee el renderer de una sola vez
                scannedBlocks.clear();
                scannedBlocks.addAll(tempFoundBlocks);
            } finally {
                // Liberar el candado para permitir el próximo escaneo
                isScanning = false;
            }
        });
    }

    // Un "Record" es una forma limpia de hacer una clase de solo lectura en Java
    public record ScannedBlock(BlockPos pos, int color) {}
}
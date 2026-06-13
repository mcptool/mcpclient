package dev.wrrulosdev.mcpclient.client.cheats.esp;

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
    public static final List<ScannedBlock> scannedBlocks = new CopyOnWriteArrayList<>();
    private static final Map<Block, Integer> TARGET_BLOCKS = new HashMap<>();
    private static long lastScanTime = 0;
    private static boolean isScanning = false;

    static {
        // Containers / valuable blocks
        addTarget(Blocks.CHEST, 0xFFFFA500);
        addTarget(Blocks.TRAPPED_CHEST, 0xFFFF5500);
        addTarget(Blocks.ENDER_CHEST, 0xFF8A2BE2);
        addTarget(Blocks.BARREL, 0xFFCD853F);
        addTarget(Blocks.SHULKER_BOX, 0xFFB266FF);
        addTarget(Blocks.WHITE_SHULKER_BOX, 0xFFB266FF);
        addTarget(Blocks.ORANGE_SHULKER_BOX, 0xFFFFA500);
        addTarget(Blocks.MAGENTA_SHULKER_BOX, 0xFFFF00FF);
        addTarget(Blocks.LIGHT_BLUE_SHULKER_BOX, 0xFF87CEFA);
        addTarget(Blocks.YELLOW_SHULKER_BOX, 0xFFFFFF00);
        addTarget(Blocks.LIME_SHULKER_BOX, 0xFF00FF00);
        addTarget(Blocks.PINK_SHULKER_BOX, 0xFFFF69B4);
        addTarget(Blocks.GRAY_SHULKER_BOX, 0xFF808080);
        addTarget(Blocks.LIGHT_GRAY_SHULKER_BOX, 0xFFD3D3D3);
        addTarget(Blocks.CYAN_SHULKER_BOX, 0xFF00FFFF);
        addTarget(Blocks.PURPLE_SHULKER_BOX, 0xFFAA00FF);
        addTarget(Blocks.BLUE_SHULKER_BOX, 0xFF0000FF);
        addTarget(Blocks.BROWN_SHULKER_BOX, 0xFF8B4513);
        addTarget(Blocks.GREEN_SHULKER_BOX, 0xFF00AA00);
        addTarget(Blocks.RED_SHULKER_BOX, 0xFFFF3333);
        addTarget(Blocks.BLACK_SHULKER_BOX, 0xFF222222);

        // Spawners / structures
        addTarget(Blocks.SPAWNER, 0xFFAA00FF);
        addTarget(Blocks.BEACON, 0xFF00FFFF);
        addTarget(Blocks.ENCHANTING_TABLE, 0xFF7F00FF);
        addTarget(Blocks.ANCIENT_DEBRIS, 0xFF6E4B3A);

        // Ores
        addTarget(Blocks.COAL_ORE, 0xFF4D4D4D);
        addTarget(Blocks.DEEPSLATE_COAL_ORE, 0xFF3A3A3A);

        addTarget(Blocks.IRON_ORE, 0xFFD8D8D8);
        addTarget(Blocks.DEEPSLATE_IRON_ORE, 0xFFB0B0B0);

        addTarget(Blocks.COPPER_ORE, 0xFFB87333);
        addTarget(Blocks.DEEPSLATE_COPPER_ORE, 0xFFA85E2E);

        addTarget(Blocks.GOLD_ORE, 0xFFFFD700);
        addTarget(Blocks.DEEPSLATE_GOLD_ORE, 0xFFE6C200);

        addTarget(Blocks.REDSTONE_ORE, 0xFFFF3030);
        addTarget(Blocks.DEEPSLATE_REDSTONE_ORE, 0xFFE02020);

        addTarget(Blocks.LAPIS_ORE, 0xFF3366FF);
        addTarget(Blocks.DEEPSLATE_LAPIS_ORE, 0xFF264DCC);

        addTarget(Blocks.DIAMOND_ORE, 0xFF00FFFF);
        addTarget(Blocks.DEEPSLATE_DIAMOND_ORE, 0xFF00D9FF);

        addTarget(Blocks.EMERALD_ORE, 0xFF00FF66);
        addTarget(Blocks.DEEPSLATE_EMERALD_ORE, 0xFF00CC55);

        addTarget(Blocks.NETHER_GOLD_ORE, 0xFFFFC000);
        addTarget(Blocks.NETHER_QUARTZ_ORE, 0xFFFFFFFF);

        // Redstone / useful utility blocks
        addTarget(Blocks.REDSTONE_BLOCK, 0xFFFF0000);
        addTarget(Blocks.REDSTONE_TORCH, 0xFFFF4444);
        addTarget(Blocks.REPEATER, 0xFFAA0000);
        addTarget(Blocks.COMPARATOR, 0xFFAA2222);
        addTarget(Blocks.OBSERVER, 0xFF666666);
        addTarget(Blocks.PISTON, 0xFF8B8B8B);
        addTarget(Blocks.STICKY_PISTON, 0xFF6E8B6E);
        addTarget(Blocks.DISPENSER, 0xFF777777);
        addTarget(Blocks.DROPPER, 0xFF666666);
        addTarget(Blocks.HOPPER, 0xFF3F3F3F);
        addTarget(Blocks.DAYLIGHT_DETECTOR, 0xFFFFCC66);
        addTarget(Blocks.LEVER, 0xFFCCAA55);
        addTarget(Blocks.TARGET, 0xFFFF6666);

        // Stations / crafting / storage
        addTarget(Blocks.FURNACE, 0xFF888888);
        addTarget(Blocks.BLAST_FURNACE, 0xFFFF8800);
        addTarget(Blocks.SMOKER, 0xFFFFAA55);
        addTarget(Blocks.BREWING_STAND, 0xFFCC99FF);
        addTarget(Blocks.CAULDRON, 0xFF6666FF);
        addTarget(Blocks.COMPOSTER, 0xFF996633);
        addTarget(Blocks.LOOM, 0xFFDDDDDD);
        addTarget(Blocks.STONECUTTER, 0xFFAAAAAA);
        addTarget(Blocks.GRINDSTONE, 0xFFBBBBBB);
        addTarget(Blocks.SMITHING_TABLE, 0xFF444444);
        addTarget(Blocks.CARTOGRAPHY_TABLE, 0xFF55AA55);
        addTarget(Blocks.FLETCHING_TABLE, 0xFFAA8855);
        addTarget(Blocks.LECTERN, 0xFFFFFFFF);
        addTarget(Blocks.ANVIL, 0xFF444444);
        addTarget(Blocks.CHIPPED_ANVIL, 0xFF555555);
        addTarget(Blocks.DAMAGED_ANVIL, 0xFF666666);
        addTarget(Blocks.JUKEBOX, 0xFFAA5500);
        addTarget(Blocks.BELL, 0xFFFFD966);
        addTarget(Blocks.CAMPFIRE, 0xFFFFAA55);
        addTarget(Blocks.SOUL_CAMPFIRE, 0xFF66CCFF);

        // Special useful blocks
        addTarget(Blocks.BEE_NEST, 0xFFFFCC66);
        addTarget(Blocks.BEEHIVE, 0xFFFFAA33);
        addTarget(Blocks.AMETHYST_BLOCK, 0xFFC77DFF);
        addTarget(Blocks.BUDDING_AMETHYST, 0xFFB266FF);
    }

    /**
     * Registers a block as a scan target and assigns its ESP color.
     *
     * @param block Block to detect during scans
     * @param color ARGB color used for rendering
     */
    private static void addTarget(Block block, int color) {
        TARGET_BLOCKS.put(block, color);
    }

    /**
     * Starts an asynchronous scan around the specified player position.
     * Scans are rate-limited and only one scan can run at a time.
     *
     * @param playerPos Center position used as the scan origin
     */
    public static void update(BlockPos playerPos) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastScanTime < 2000 || isScanning) {
            return;
        }

        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        lastScanTime = currentTime;
        isScanning = true;

        CompletableFuture.runAsync(() -> {
            try {
                List<ScannedBlock> tempFoundBlocks = new ArrayList<>();
                int radius = 64;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            BlockPos currentPos = playerPos.offset(x, y, z);
                            BlockState state = level.getBlockState(currentPos);
                            Block block = state.getBlock();

                            Integer color = TARGET_BLOCKS.get(block);

                            if (color != null) {
                                tempFoundBlocks.add(new ScannedBlock(currentPos, color));
                            }
                        }
                    }
                }

                scannedBlocks.clear();
                scannedBlocks.addAll(tempFoundBlocks);
            } finally {
                isScanning = false;
            }
        });
    }

    /**
     * Represents a block detected during scanning together with
     * its assigned ESP render color.
     *
     * @param pos Block position
     * @param color ARGB render color
     */
    public record ScannedBlock(BlockPos pos, int color) { }
}
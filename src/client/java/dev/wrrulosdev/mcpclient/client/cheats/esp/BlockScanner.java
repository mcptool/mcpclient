package dev.wrrulosdev.mcpclient.client.cheats.esp;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockScanner {

    public enum TargetCategory {
        COAL_ORES,
        IRON_ORES,
        COPPER_ORES,
        GOLD_ORES,
        REDSTONE_ORES,
        LAPIS_ORES,
        DIAMOND_ORES,
        EMERALD_ORES,
        NETHER_ORES,
        ANCIENT_DEBRIS,
        MINERAL_BLOCKS,
        STORAGE,
        UTILITY,
        REDSTONE
    }

    private record Target(Block block, int color, TargetCategory category) { }

    public static final List<ScannedBlock> scannedBlocks = new CopyOnWriteArrayList<>();
    private static final List<Target> TARGETS = new ArrayList<>();
    private static final EnumSet<TargetCategory> ENABLED_CATEGORIES =
        EnumSet.allOf(TargetCategory.class);

    private static long lastScanTime = 0;
    private static boolean isScanning = false;

    static {
        // COAL_ORES
        addTarget(TargetCategory.COAL_ORES, Blocks.COAL_ORE, 0xFF4D4D4D);
        addTarget(TargetCategory.COAL_ORES, Blocks.DEEPSLATE_COAL_ORE, 0xFF3A3A3A);

        // IRON_ORES
        addTarget(TargetCategory.IRON_ORES, Blocks.IRON_ORE, 0xFFD8D8D8);
        addTarget(TargetCategory.IRON_ORES, Blocks.DEEPSLATE_IRON_ORE, 0xFFB0B0B0);

        // COPPER_ORES
        addTarget(TargetCategory.COPPER_ORES, Blocks.COPPER_ORE, 0xFFB87333);
        addTarget(TargetCategory.COPPER_ORES, Blocks.DEEPSLATE_COPPER_ORE, 0xFFA85E2E);

        // GOLD_ORES
        addTarget(TargetCategory.GOLD_ORES, Blocks.GOLD_ORE, 0xFFFFD700);
        addTarget(TargetCategory.GOLD_ORES, Blocks.DEEPSLATE_GOLD_ORE, 0xFFE6C200);
        addTarget(TargetCategory.GOLD_ORES, Blocks.NETHER_GOLD_ORE, 0xFFFFC000);

        // REDSTONE_ORES
        addTarget(TargetCategory.REDSTONE_ORES, Blocks.REDSTONE_ORE, 0xFFFF3030);
        addTarget(TargetCategory.REDSTONE_ORES, Blocks.DEEPSLATE_REDSTONE_ORE, 0xFFE02020);

        // LAPIS_ORES
        addTarget(TargetCategory.LAPIS_ORES, Blocks.LAPIS_ORE, 0xFF3366FF);
        addTarget(TargetCategory.LAPIS_ORES, Blocks.DEEPSLATE_LAPIS_ORE, 0xFF264DCC);

        // DIAMOND_ORES
        addTarget(TargetCategory.DIAMOND_ORES, Blocks.DIAMOND_ORE, 0xFF00FFFF);
        addTarget(TargetCategory.DIAMOND_ORES, Blocks.DEEPSLATE_DIAMOND_ORE, 0xFF00D9FF);

        // EMERALD_ORES
        addTarget(TargetCategory.EMERALD_ORES, Blocks.EMERALD_ORE, 0xFF00FF66);
        addTarget(TargetCategory.EMERALD_ORES, Blocks.DEEPSLATE_EMERALD_ORE, 0xFF00CC55);

        // NETHER_ORES
        addTarget(TargetCategory.NETHER_ORES, Blocks.NETHER_QUARTZ_ORE, 0xFFFFFFFF);

        // ANCIENT_DEBRIS
        addTarget(TargetCategory.ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS, 0xFF6E4B3A);

        // MINERAL_BLOCKS
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.COAL_BLOCK, 0xFF4D4D4D);

        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.IRON_BLOCK, 0xFFD8D8D8);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.RAW_IRON_BLOCK, 0xFFB0B0B0);

        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.COPPER_BLOCK, 0xFFB87333);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.RAW_COPPER_BLOCK, 0xFFA85E2E);

        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.GOLD_BLOCK, 0xFFFFD700);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.RAW_GOLD_BLOCK, 0xFFE6C200);

        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.REDSTONE_BLOCK, 0xFFFF0000);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.LAPIS_BLOCK, 0xFF3366FF);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.DIAMOND_BLOCK, 0xFF00FFFF);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.EMERALD_BLOCK, 0xFF00FF66);
        addTarget(TargetCategory.MINERAL_BLOCKS, Blocks.NETHERITE_BLOCK, 0xFF3B2F2F);

        // STORAGE
        addTarget(TargetCategory.STORAGE, Blocks.CHEST, 0xFFFFA500);
        addTarget(TargetCategory.STORAGE, Blocks.TRAPPED_CHEST, 0xFFFF5500);
        addTarget(TargetCategory.STORAGE, Blocks.ENDER_CHEST, 0xFF8A2BE2);
        addTarget(TargetCategory.STORAGE, Blocks.BARREL, 0xFFCD853F);

        addTarget(TargetCategory.STORAGE, Blocks.SHULKER_BOX, 0xFFB266FF);
        addTarget(TargetCategory.STORAGE, Blocks.WHITE_SHULKER_BOX, 0xFFB266FF);
        addTarget(TargetCategory.STORAGE, Blocks.ORANGE_SHULKER_BOX, 0xFFFFA500);
        addTarget(TargetCategory.STORAGE, Blocks.MAGENTA_SHULKER_BOX, 0xFFFF00FF);
        addTarget(TargetCategory.STORAGE, Blocks.LIGHT_BLUE_SHULKER_BOX, 0xFF87CEFA);
        addTarget(TargetCategory.STORAGE, Blocks.YELLOW_SHULKER_BOX, 0xFFFFFF00);
        addTarget(TargetCategory.STORAGE, Blocks.LIME_SHULKER_BOX, 0xFF00FF00);
        addTarget(TargetCategory.STORAGE, Blocks.PINK_SHULKER_BOX, 0xFFFF69B4);
        addTarget(TargetCategory.STORAGE, Blocks.GRAY_SHULKER_BOX, 0xFF808080);
        addTarget(TargetCategory.STORAGE, Blocks.LIGHT_GRAY_SHULKER_BOX, 0xFFD3D3D3);
        addTarget(TargetCategory.STORAGE, Blocks.CYAN_SHULKER_BOX, 0xFF00FFFF);
        addTarget(TargetCategory.STORAGE, Blocks.PURPLE_SHULKER_BOX, 0xFFAA00FF);
        addTarget(TargetCategory.STORAGE, Blocks.BLUE_SHULKER_BOX, 0xFF0000FF);
        addTarget(TargetCategory.STORAGE, Blocks.BROWN_SHULKER_BOX, 0xFF8B4513);
        addTarget(TargetCategory.STORAGE, Blocks.GREEN_SHULKER_BOX, 0xFF00AA00);
        addTarget(TargetCategory.STORAGE, Blocks.RED_SHULKER_BOX, 0xFFFF3333);
        addTarget(TargetCategory.STORAGE, Blocks.BLACK_SHULKER_BOX, 0xFF222222);

        // UTILITY
        addTarget(TargetCategory.UTILITY, Blocks.SPAWNER, 0xFFAA00FF);
        addTarget(TargetCategory.UTILITY, Blocks.BEACON, 0xFF00FFFF);
        addTarget(TargetCategory.UTILITY, Blocks.ENCHANTING_TABLE, 0xFF7F00FF);

        addTarget(TargetCategory.UTILITY, Blocks.FURNACE, 0xFF888888);
        addTarget(TargetCategory.UTILITY, Blocks.BLAST_FURNACE, 0xFFFF8800);
        addTarget(TargetCategory.UTILITY, Blocks.SMOKER, 0xFFFFAA55);

        addTarget(TargetCategory.UTILITY, Blocks.BREWING_STAND, 0xFFCC99FF);
        addTarget(TargetCategory.UTILITY, Blocks.CAULDRON, 0xFF6666FF);
        addTarget(TargetCategory.UTILITY, Blocks.COMPOSTER, 0xFF996633);

        addTarget(TargetCategory.UTILITY, Blocks.LOOM, 0xFFDDDDDD);
        addTarget(TargetCategory.UTILITY, Blocks.STONECUTTER, 0xFFAAAAAA);
        addTarget(TargetCategory.UTILITY, Blocks.GRINDSTONE, 0xFFBBBBBB);
        addTarget(TargetCategory.UTILITY, Blocks.SMITHING_TABLE, 0xFF444444);
        addTarget(TargetCategory.UTILITY, Blocks.CARTOGRAPHY_TABLE, 0xFF55AA55);
        addTarget(TargetCategory.UTILITY, Blocks.FLETCHING_TABLE, 0xFFAA8855);
        addTarget(TargetCategory.UTILITY, Blocks.LECTERN, 0xFFFFFFFF);

        addTarget(TargetCategory.UTILITY, Blocks.ANVIL, 0xFF444444);
        addTarget(TargetCategory.UTILITY, Blocks.CHIPPED_ANVIL, 0xFF555555);
        addTarget(TargetCategory.UTILITY, Blocks.DAMAGED_ANVIL, 0xFF666666);

        addTarget(TargetCategory.UTILITY, Blocks.JUKEBOX, 0xFFAA5500);
        addTarget(TargetCategory.UTILITY, Blocks.BELL, 0xFFFFD966);

        addTarget(TargetCategory.UTILITY, Blocks.CAMPFIRE, 0xFFFFAA55);
        addTarget(TargetCategory.UTILITY, Blocks.SOUL_CAMPFIRE, 0xFF66CCFF);

        addTarget(TargetCategory.UTILITY, Blocks.BEE_NEST, 0xFFFFCC66);
        addTarget(TargetCategory.UTILITY, Blocks.BEEHIVE, 0xFFFFAA33);

        addTarget(TargetCategory.UTILITY, Blocks.AMETHYST_BLOCK, 0xFFC77DFF);
        addTarget(TargetCategory.UTILITY, Blocks.BUDDING_AMETHYST, 0xFFB266FF);

        // REDSTONE
        addTarget(TargetCategory.REDSTONE, Blocks.REDSTONE_BLOCK, 0xFFFF0000);
        addTarget(TargetCategory.REDSTONE, Blocks.REDSTONE_TORCH, 0xFFFF4444);
        addTarget(TargetCategory.REDSTONE, Blocks.REPEATER, 0xFFAA0000);
        addTarget(TargetCategory.REDSTONE, Blocks.COMPARATOR, 0xFFAA2222);
        addTarget(TargetCategory.REDSTONE, Blocks.OBSERVER, 0xFF666666);
        addTarget(TargetCategory.REDSTONE, Blocks.PISTON, 0xFF8B8B8B);
        addTarget(TargetCategory.REDSTONE, Blocks.STICKY_PISTON, 0xFF6E8B6E);
        addTarget(TargetCategory.REDSTONE, Blocks.DISPENSER, 0xFF777777);
        addTarget(TargetCategory.REDSTONE, Blocks.DROPPER, 0xFF666666);
        addTarget(TargetCategory.REDSTONE, Blocks.HOPPER, 0xFF3F3F3F);
        addTarget(TargetCategory.REDSTONE, Blocks.DAYLIGHT_DETECTOR, 0xFFFFCC66);
        addTarget(TargetCategory.REDSTONE, Blocks.LEVER, 0xFFCCAA55);
        addTarget(TargetCategory.REDSTONE, Blocks.TARGET, 0xFFFF6666);
    }

    private static void addTarget(TargetCategory category, Block block, int color) {
        TARGETS.add(new Target(block, color, category));
    }

    public static void setCategoryEnabled(TargetCategory category, boolean enabled) {
        if (enabled) {
            ENABLED_CATEGORIES.add(category);
        } else {
            ENABLED_CATEGORIES.remove(category);
        }
    }

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

                            for (Target target : TARGETS) {
                                if (target.block() == block && ENABLED_CATEGORIES.contains(target.category())) {
                                    tempFoundBlocks.add(new ScannedBlock(currentPos, target.color()));
                                    break;
                                }
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

    public record ScannedBlock(BlockPos pos, int color) { }
}
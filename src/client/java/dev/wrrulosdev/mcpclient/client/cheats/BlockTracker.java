package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.esp.BlockScanner;
import dev.wrrulosdev.mcpclient.client.cheats.esp.EspRenderer;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class BlockTracker extends CheatBase {

    public static final BlockTracker INSTANCE = new BlockTracker();

    /**
     * Retrieves the cheat settings instance used by this module.
     *
     * @return The current cheats settings configuration.
     */
    private CheatsSettings getSettings() {
        return MCPClient.getSettingsManager().getCheatsSettings();
    }

    /**
     * Returns the unique identifier used to reference this cheat.
     *
     * @return The cheat identifier.
     */
    @Override
    public String getIdentifier() {
        return "blocktracker";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "BlockTracker";
    }

    /**
     * Returns the default keyboard key assigned to this module.
     *
     * @return The GLFW key code used as the default keybind.
     */
    @Override
    public int getDefaultKey() {
        return ClientConstants.DEFAULT_INVALID_KEYBIND;
    }

    /**
     * Determines whether the block tracker module is currently enabled.
     *
     * @return True if block tracker is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isBlockTrackerEnabled();
    }

    /**
     * Updates the enabled state of the block tracker module.
     *
     * @param enabled The new block tracker state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setBlockTrackerEnabled(enabled);
    }

    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        CheatsSettings cs = getSettings();

        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.COAL_ORES, cs.isBlockTrackerCoalOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.IRON_ORES, cs.isBlockTrackerIronOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.COPPER_ORES, cs.isBlockTrackerCopperOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.GOLD_ORES, cs.isBlockTrackerGoldOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.REDSTONE_ORES, cs.isBlockTrackerRedstoneOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.LAPIS_ORES, cs.isBlockTrackerLapisOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.DIAMOND_ORES, cs.isBlockTrackerDiamondOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.EMERALD_ORES, cs.isBlockTrackerEmeraldOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.NETHER_ORES, cs.isBlockTrackerNetherOresEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.ANCIENT_DEBRIS, cs.isBlockTrackerAncientDebrisEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.MINERAL_BLOCKS, cs.isBlockTrackerMineralBlocksEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.STORAGE, cs.isBlockTrackerStorageEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.UTILITY, cs.isBlockTrackerUtilityEnabled());
        BlockScanner.setCategoryEnabled(BlockScanner.TargetCategory.REDSTONE, cs.isBlockTrackerRedstoneEnabled());
        BlockScanner.update(Minecraft.getInstance().player.blockPosition());
    }
}
package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;

public class Jesus extends CheatBase {

    public static final Jesus INSTANCE = new Jesus();

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
        return "jesus";
    }

    /**
     * Returns the default keyboard key assigned to this module.
     *
     * @return The default keybind identifier.
     */
    @Override
    public int getDefaultKey() {
        return ClientConstants.DEFAULT_INVALID_KEYBIND;
    }

    /**
     * Determines whether the Jesus module is currently enabled.
     *
     * @return True if the module is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isJesusEnabled();
    }

    /**
     * Updates the enabled state of the Jesus module.
     *
     * @param enabled The new module state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setJesusEnabled(enabled);
    }

    /**
     * Allows the player to stand and walk on water or lava by forcing a
     * grounded state whenever a fluid block is detected directly below.
     * Normal movement is preserved while jumping.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        if (!isEnabled()) {
            return;
        }

        BlockPos pos = player.blockPosition().below();
        FluidState fluidState = player.level().getFluidState(pos);

        boolean canWalkOnFluid =
            (getSettings().isJesusWaterEnabled() && fluidState.is(FluidTags.WATER))
                || (getSettings().isJesusLavaEnabled() && fluidState.is(FluidTags.LAVA));

        if (!canWalkOnFluid || player.isJumping()) {
            return;
        }

        player.setOnGround(true);
        player.setDeltaMovement(
            player.getDeltaMovement().x,
            0.0D,
            player.getDeltaMovement().z
        );
    }
}
package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.AABB;

public class NoFall extends CheatBase {

    public static final NoFall INSTANCE = new NoFall();

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
        return "nofall";
    }

    /**
     * Returns the display name of this cheat.
     */
    @Override
    public String getName() {
        return "NoFall";
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
     * Determines whether the NoFall module is currently enabled.
     *
     * @return True if the module is enabled.
     */
    @Override
    public boolean isEnabled() {
        return getSettings().isNoFallEnabled();
    }

    /**
     * Updates the enabled state of the NoFall module.
     *
     * @param enabled The new module state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        getSettings().setNoFallEnabled(enabled);
    }

    /**
     * Prevents fall damage by detecting imminent ground collisions and
     * resetting the player's accumulated fall distance before impact.
     * A brief flight state toggle is used to assist the damage bypass.
     *
     * @param player The local player instance.
     * @param args Optional execution arguments. Not used by this cheat.
     */
    @Override
    protected void onExecute(LocalPlayer player, Object... args) {
        if (!isEnabled()) {
            return;
        }

        boolean isFalling = player.getDeltaMovement().y < -0.1D;
        boolean canTakeFallDamage = player.fallDistance > 3.0F;

        if (!isFalling || !canTakeFallDamage) {
            return;
        }

        double nextY = player.getDeltaMovement().y;
        AABB predictedBox = player.getBoundingBox().move(
            0.0D,
            nextY - 0.1D,
            0.0D
        );

        boolean willHitGround =
            !player.level().noCollision(player, predictedBox);

        if (!willHitGround) {
            return;
        }

        player.getAbilities().flying = true;

        player.setDeltaMovement(
            player.getDeltaMovement().x,
            0.1D,
            player.getDeltaMovement().z
        );

        player.getAbilities().flying = false;
        player.fallDistance = 0.0F;
    }
}
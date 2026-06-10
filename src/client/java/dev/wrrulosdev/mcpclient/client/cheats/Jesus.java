package dev.wrrulosdev.mcpclient.client.cheats;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;

public class Jesus extends CheatBase {

    public static final Jesus INSTANCE = new Jesus();

    /**
     * Simulates water and lava walking behavior by forcing the local player
     * to remain grounded while standing above fluid blocks.
     * Vertical movement is neutralized to prevent sinking into the liquid.
     *
     * @param player The local player instance being processed by the cheat system.
     */
    @Override
    protected void onExecute(LocalPlayer player) {
        BlockPos pos = player.blockPosition().below();
        boolean isFluidBelow = player.level().getFluidState(pos).is(FluidTags.WATER)
            || player.level().getFluidState(pos).is(FluidTags.LAVA);

        if (!isFluidBelow || player.isJumping()) {
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
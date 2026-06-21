package dev.wrrulosdev.mcpclient.client.mixins.world;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTeam.class)
public abstract class PlayerTeamMixin {

    @Shadow
    private Component playerPrefix;

    @Shadow
    private Component playerSuffix;

    /**
     * Injects into the scoreboard team name formatting process to replace the local player's
     * real username with an anonymous alias if the feature is enabled.
     *
     * @param teamMemberName The original name component of the team member
     * @param cir Callback information used to return a custom modified component
     */
    @Inject(method = "getFormattedName", at = @At("HEAD"), cancellable = true)
    private void replaceName(Component teamMemberName, CallbackInfoReturnable<MutableComponent> cir) {
        if (!MCPClient.getSettingsManager().getClientSettings().isAnonymousScoreboardEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        String oldName = minecraft.player.getName().getString();
        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();
        MutableComponent prefix = AnonymizerUtils.replaceTextWithRedName(playerPrefix, oldName, newName);
        MutableComponent suffix = AnonymizerUtils.replaceTextWithRedName(playerSuffix, oldName, newName);
        MutableComponent result = Component.empty()
            .append(prefix)
            .append(teamMemberName)
            .append(suffix);
        cir.setReturnValue(result);
    }
}
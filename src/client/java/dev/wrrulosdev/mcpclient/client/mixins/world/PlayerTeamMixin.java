package dev.wrrulosdev.mcpclient.client.mixins.world;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.options.Anonymous;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTeam.class)
public abstract class PlayerTeamMixin {

    /**
     * Intercepts the formatting of team names to anonymize the player's name
     * on the scoreboard if the anonymity feature is enabled in the client settings.
     *
     * @param team The team associated with the name
     * @param name The component representing the player's name
     * @param cir  The callback information for the injection point
     */
    @Inject(method = "formatNameForTeam", at = @At("RETURN"), cancellable = true)
    private static void replaceNameOnScoreboard(Team team, Component name, CallbackInfoReturnable<MutableComponent> cir) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();

        if (!Anonymous.INSTANCE.isEnabled() || !clientSettings.isAnonymousScoreboardEnabled()) {
            return;
        }

        String oldName = minecraft.player.getName().getString();
        String newName = clientSettings.getNewAnonymousName();
        MutableComponent originalComponent = cir.getReturnValue();
        MutableComponent modifiedComponent = AnonymizerUtils.replaceTextWithRedName(originalComponent, oldName, newName);
        cir.setReturnValue(modifiedComponent);
    }
}
package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    /**
     * Mixin method for rendering the player list.
     * <p>
     * @param context The draw context.
     * @param scaledWindowWidth The scaled window width.
     * @param scoreboard The scoreboard.
     * @param objective The scoreboard objective.
     * @param ci Callback information for mixin injection handling.
     */

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        MCPToolSettings settings = Mcpclient.getSettings();

        if (!settings.isStealthMode()) {
            return;
        }

        Collection<PlayerListEntry> playerListEntries = client.getNetworkHandler().getPlayerList();

        for (PlayerListEntry entry : playerListEntries) {
            if (entry.getProfile().getName().contains("MCPTool")) {
                entry.setDisplayName(Text.of("***"));
            }
        }
    }
}


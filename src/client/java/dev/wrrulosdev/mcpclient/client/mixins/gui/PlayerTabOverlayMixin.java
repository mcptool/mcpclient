package dev.wrrulosdev.mcpclient.client.mixins.gui;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.options.Anonymous;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import dev.wrrulosdev.mcpclient.client.utilities.messages.AnonymizerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Shadow @Nullable private Component header;
    @Shadow @Nullable private Component footer;

    /**
     * Intercepts the tab list overlay rendering to anonymize the player's name
     * within the header and footer components if the anonymous tab list setting is enabled.
     *
     * @param graphics         The graphics extraction utility
     * @param screenWidth      The width of the screen
     * @param scoreboard       The active scoreboard
     * @param displayObjective The currently displayed scoreboard objective
     * @param ci               The callback information for the injection point
     */
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void injectAnonymousHeaderFooter(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, @Nullable Objective displayObjective, CallbackInfo ci) {
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();

        if (!Anonymous.INSTANCE.isEnabled() || !clientSettings.isAnonymousTabListEnabled()) {
            return;
        }

        String oldName = Minecraft.getInstance().player.getName().getString();
        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();

        if (this.header != null && this.header.getString().contains(oldName)) {
            this.header = AnonymizerUtils.replaceTextWithRedName(this.header, oldName, newName);
        }

        if (this.footer != null && this.footer.getString().contains(oldName)) {
            this.footer = AnonymizerUtils.replaceTextWithRedName(this.footer, oldName, newName);
        }
    }

    /**
     * Injects into the player tab list display name retrieval to anonymize the
     * local player's entry if the feature is enabled.
     *
     * @param info The player information for the entry being rendered
     * @param cir Callback information used to modify the returned display name component
     */
    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void modifyTabName(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();

        if (!Anonymous.INSTANCE.isEnabled() || !clientSettings.isAnonymousTabListEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || !info.getProfile().id().equals(minecraft.player.getUUID())) {
            return;
        }

        Component originalComponent = cir.getReturnValue();

        if (originalComponent == null) {
            return;
        }

        String oldName = minecraft.player.getName().getString();
        String newName = MCPClient.getSettingsManager().getClientSettings().getNewAnonymousName();
        cir.setReturnValue(AnonymizerUtils.replaceTextWithRedName(originalComponent, oldName, newName));
    }
}
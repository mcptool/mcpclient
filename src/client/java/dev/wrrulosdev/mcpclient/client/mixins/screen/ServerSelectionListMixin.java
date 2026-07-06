package dev.wrrulosdev.mcpclient.client.mixins.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class ServerSelectionListMixin {

    @Unique
    private final Minecraft client = Minecraft.getInstance();

    /**
     * Injects custom rendering logic to display server version and protocol information
     * in the server selection list entries.
     *
     * @param graphics The graphics extractor used to render text on the screen.
     * @param mouseX   The current X-coordinate of the mouse cursor.
     * @param mouseY   The current Y-coordinate of the mouse cursor.
     * @param hovered  Whether the entry is currently being hovered by the mouse.
     * @param a        The alpha or animation progress value.
     * @param ci       The callback information to control the injection flow.
     */
    @Inject(method = "extractContent", at = @At("TAIL"))
    private void onExtractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a, CallbackInfo ci) {
        ServerData server = ((ServerSelectionList.OnlineServerEntry) (Object) this).getServerData();
        int rightX = ((ServerSelectionList.OnlineServerEntry) (Object) this).getContentRight();
        int y = ((ServerSelectionList.OnlineServerEntry) (Object) this).getContentY();
        int drawX = rightX + 10;
        int textY = y + 5;
        String version = server.version.getString();
        String protocol = "Protocol: " + server.protocol;
        graphics.text(this.client.font, version, drawX, textY, 0xFFFFAAAA, true);
        graphics.text(this.client.font, protocol, drawX, textY + 10, 0xFFFFAAAA, true);
    }
}
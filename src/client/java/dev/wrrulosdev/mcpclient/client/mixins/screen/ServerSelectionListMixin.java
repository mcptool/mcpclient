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
    private static final int LABEL_COLOR = 0xFFFF5555;
    private static final int VALUE_COLOR = 0xFF55FF55;
    private static final String VERSION_LABEL = "Version: ";
    private static final String PROTOCOL_LABEL = "Protocol: ";
    private static final String LAST_CONNECTION_LABEL = "Last connection: ";

    /**
     * Injects custom rendering logic to display server version protocol and last connection
     * information in the server selection list entries.
     *
     * @param graphics The graphics extractor used to render text on the screen.
     * @param mouseX   The current X-coordinate of the mouse cursor.
     * @param mouseY   The current Y-coordinate of the mouse cursor.
     * @param hovered  Whether the entry is currently being hovered by the mouse.
     * @param a        The alpha or animation progress value.
     * @param ci       The callback information to control the injection flow.
     */
    @Inject(method = "extractContent", at = @At("TAIL"))
    private void onExtractContent(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        boolean hovered,
        float a,
        CallbackInfo ci
    ) {
        ServerData server = ((ServerSelectionList.OnlineServerEntry) (Object) this).getServerData();
        int drawX = ((ServerSelectionList.OnlineServerEntry) (Object) this).getContentRight() + 10;
        int textY = ((ServerSelectionList.OnlineServerEntry) (Object) this).getContentY() + 5;
        String version = server.version.getString();
        String protocol = String.valueOf(server.protocol);
        String lastConnection = "Unknown";
        drawEntry(graphics, VERSION_LABEL, version, drawX, textY);
        drawEntry(graphics, PROTOCOL_LABEL, protocol, drawX, textY + 10);
        drawEntry(graphics, LAST_CONNECTION_LABEL, lastConnection, drawX, textY + 20);
    }

    /**
     * Renders a key-value pair entry to the screen using the provided graphics engine.
     * The label is drawn with a specific color, followed by the value at an offset
     * determined by the width of the label.
     *
     * @param graphics The screen rendering engine
     * @param label The descriptive text (key)
     * @param value The value text to be displayed
     * @param x The starting X coordinate
     * @param y The Y coordinate
     */
    private void drawEntry(
        GuiGraphicsExtractor graphics,
        String label,
        String value,
        int x,
        int y
    ) {
        graphics.text(this.client.font, label, x, y, LABEL_COLOR, true);
        graphics.text(
            this.client.font,
            value,
            x + this.client.font.width(label),
            y,
            VALUE_COLOR,
            true
        );
    }
}
package dev.wrrulos.mcpclient.mixin;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.session.SessionController;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(method = "onGameMessage(Lnet/minecraft/text/Text;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V"))
    private void onGameMessage(ChatHud instance, Text text) {
        MCPToolSettings settings = Mcpclient.getSettings();
        SessionController sessionController = Mcpclient.getSessionController();
        String username = sessionController.getUsername();
        Text newMessage = text;

        if (text.getString().contains(username) && settings.isStealthMode()) {
            newMessage = Text.of(text.getString().replace(username, "****"));
        }

        instance.addMessage(newMessage);
    }
}
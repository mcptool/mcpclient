package dev.wrrulosdev.mcpclient.client.mixins.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LinearLayout.class)
public class LinearLayoutMixin {

    /**
     * Intercepts the return of child element addition to linear layouts to intercept, filter, and disable specific vanilla server selection menu control buttons.
     * @param child    The target layout element instance being appended to the container.
     * @param settings The structural layout configuration properties associated with the child widget.
     * @param cir      The mixin callback info handle controlling return invocation properties.
     * @param <T>      The structural widget type extending standard interface elements.
     */
    @Inject(method = "addChild", at = @At("RETURN"))
    private <T extends LayoutElement> void onAddChild(
        T child,
        LayoutSettings settings,
        CallbackInfoReturnable<T> cir
    ) {

        if (child instanceof Button button) {
            Component msg = button.getMessage();

            if (msg.getContents() instanceof TranslatableContents translatable) {
                String key = translatable.getKey();

                if (key.equals("selectServer.select") ||
                    key.equals("selectServer.direct") ||
                    key.equals("selectServer.add") ||
                    key.equals("selectServer.refresh") ||
                    key.equals("gui.back")) {

                    button.visible = false;
                    button.active = false;
                }
            }
        }
    }
}
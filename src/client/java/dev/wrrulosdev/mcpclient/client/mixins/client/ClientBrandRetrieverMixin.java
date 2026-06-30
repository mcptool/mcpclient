package dev.wrrulosdev.mcpclient.client.mixins.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {

    /**
     * Injects logic at the start of {@code getClientModName} to force the return value to "vanilla".
     *
     * @param cir The callback info used to set the method's return value and cancel original execution
     */
    @Inject(
        method = "getClientModName",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getClientModName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("vanilla");
    }
}
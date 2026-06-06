package dev.wrrulosdev.mcpclient.client.mixins.options;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Options.class)
public abstract class FullbrightMixin {

    /**
     * Shadow reference to the game's gamma option instance.
     * This field is used to directly modify the underlying gamma configuration value.
     */
    @Final
    @Shadow
    private OptionInstance<Double> gamma;

    /**
     * Intercepts the gamma option getter and forces the gamma value to maximum brightness.
     * This ensures that every time the gamma option is requested, it is set to {@code 100.0D}.
     *
     * @param info Callback return handler for the gamma option retrieval method
     */
    @Inject(
        method = "gamma",
        at = @At("RETURN"),
        cancellable = true
    )
    private void forceFullbright(CallbackInfoReturnable<OptionInstance<Double>> info) {
        gamma.set(100.0D);
    }
}
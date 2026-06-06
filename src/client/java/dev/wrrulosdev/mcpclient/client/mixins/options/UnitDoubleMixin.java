package dev.wrrulosdev.mcpclient.client.mixins.options;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(OptionInstance.UnitDouble.class)
public abstract class UnitDoubleMixin {

    /**
     * Intercepts the value validation process and enforces a maximum limit.
     * If the provided value is {@code null}, an empty {@link Optional} is returned.
     * Otherwise, the value is clamped to a maximum of {@code 100.0D}.
     *
     * @param value The value being validated
     * @param cir Callback return handler used to override the original method result
     */
    @Inject(
        method = "validateValue(Ljava/lang/Double;)Ljava/util/Optional;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void clampMax(Double value, CallbackInfoReturnable<Optional<Double>> cir) {
        if (value == null) {
            cir.setReturnValue(Optional.empty());
            return;
        }

        double clamped = Math.min(value, 100.0D);
        cir.setReturnValue(Optional.of(clamped));
    }
}
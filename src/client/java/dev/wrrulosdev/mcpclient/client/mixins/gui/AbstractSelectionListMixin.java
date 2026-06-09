package dev.wrrulosdev.mcpclient.client.mixins.gui;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {

    /**
     * Replaces the original selection color argument with a fixed value.
     *
     * @param originalColor Original computed color value
     * @return Forced replacement color (red)
     */
    @ModifyVariable(
        method = "extractSelection",
        at = @At("HEAD"),
        argsOnly = true
    )
    private int changeBorderColor(int originalColor) {
        return -65536; // Red
    }
}
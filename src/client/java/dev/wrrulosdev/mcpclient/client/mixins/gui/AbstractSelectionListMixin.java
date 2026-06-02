package dev.wrrulosdev.mcpclient.client.mixins.gui;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {

    @ModifyVariable(
        method = "extractSelection",
        at = @At("HEAD"),
        argsOnly = true
    )
    private int changeBorderColor(int originalColor) {
        return -65536; // Red
    }
}
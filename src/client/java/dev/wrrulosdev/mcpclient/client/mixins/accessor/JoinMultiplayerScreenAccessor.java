package dev.wrrulosdev.mcpclient.client.mixins.accessor;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JoinMultiplayerScreen.class)
public interface JoinMultiplayerScreenAccessor {

    @Accessor("selectButton")
    Button getSelectButton();

    @Accessor("editButton")
    Button getEditButton();

    @Accessor("deleteButton")
    Button getDeleteButton();
}

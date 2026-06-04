package dev.wrrulosdev.mcpclient.client.mixins.accessor;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(JoinMultiplayerScreen.class)
public interface JoinMultiplayerScreenAccessor {

    @Accessor("selectButton")
    Button getSelectButton();

    @Accessor("editButton")
    Button getEditButton();

    @Accessor("deleteButton")
    Button getDeleteButton();

    @Accessor("editingServer")
    ServerData getEditingServer();

    @Accessor("editingServer") void setEditingServer(ServerData data);

    @Accessor("serverSelectionList")
    ServerSelectionList getServeSelectionList();

    @Invoker("refreshServerList")
    void invokeRefreshServerList();

    @Invoker("deleteCallback")
    void invokeDeleteCallback(boolean result);

    @Invoker("addServerCallback")
    void invokeAddServerCallback(boolean result);

    @Invoker("directJoinCallback")
    void invokeDirectJoinCallback(boolean result);
}

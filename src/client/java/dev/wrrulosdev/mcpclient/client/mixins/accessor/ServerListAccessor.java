package dev.wrrulosdev.mcpclient.client.mixins.accessor;

import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ServerList.class)
public interface ServerListAccessor {

    @Accessor("serverList")
    List<ServerData> getServerList();
}
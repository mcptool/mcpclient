package dev.wrrulos.mcpclient.mixin;

import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface SessionMixin {
    @Mutable
    @Accessor("username")
    void setUsername(String username);

    @Accessor("username")
    String getUsername();
}
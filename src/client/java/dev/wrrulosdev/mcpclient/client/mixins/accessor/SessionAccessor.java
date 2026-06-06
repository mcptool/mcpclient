package dev.wrrulosdev.mcpclient.client.mixins.accessor;

import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(User.class)
public interface SessionAccessor {

    @Mutable
    @Accessor("name")
    void setUsername(String username);

    @Accessor("name")
    String getUsername();
}
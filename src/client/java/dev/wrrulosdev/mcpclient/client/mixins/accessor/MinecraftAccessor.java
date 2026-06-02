package dev.wrrulosdev.mcpclient.client.mixins.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor("playerSkinRenderCache")
    PlayerSkinRenderCache getPlayerSkinRenderCache();
}
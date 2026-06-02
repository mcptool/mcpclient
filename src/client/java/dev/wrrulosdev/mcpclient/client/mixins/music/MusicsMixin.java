package dev.wrrulosdev.mcpclient.client.mixins.music;

import dev.wrrulosdev.mcpclient.client.sounds.SoundEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Musics.class)
public class MusicsMixin {

    @Shadow @Final @Mutable
    public static Music MENU;

    /**
     * Intercepts the static class initializer block tail event to overwrite the default main menu music configuration reference.
     * @param ci The mixin callback info handle controlling invocation properties.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceMenuMusic(CallbackInfo ci) {
        MENU = new Music(
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(
                SoundEvents.MENU_MUSIC
            ),
            20,
            600,
            true
        );
    }
}
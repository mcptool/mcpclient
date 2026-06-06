package dev.wrrulosdev.mcpclient.client.sounds;

import dev.wrrulosdev.mcpclient.client.constants.ClientConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class SoundEvents {

    public static final Identifier ID =
        Identifier.fromNamespaceAndPath(ClientConstants.IDENTIFIER, "menu_music");

    public static final SoundEvent MENU_MUSIC =
        SoundEvent.createVariableRangeEvent(ID);

    /**
     * Registers custom runtime sound instances directly into the registry lifecycle map storage framework.
     */
    public static void register() {
        Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            ID,
            MENU_MUSIC
        );
    }
}
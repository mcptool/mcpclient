package dev.wrrulosdev.mcpclient.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.*;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.KeyMappingAccessor;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class KeyBindManager {

    /**
     * Internal registry containing every key mapping paired with
     * its corresponding cheat implementation.
     */
    private final List<KeyBindEntry> registry = new ArrayList<>();

    /**
     * Lightweight container used to associate a Minecraft key mapping
     * with the cheat it controls.
     *
     * @param mapping The registered key mapping instance.
     * @param cheat The cheat controlled by the keybind.
     */
    private record KeyBindEntry(
        KeyMapping mapping,
        CheatBase cheat
    ) {}

    /**
     * Creates the keybind manager and registers all cheats that
     * support keyboard activation.
     */
    public KeyBindManager() {
        register(Fly.INSTANCE);
        register(FakeCreative.INSTANCE);
        register(Jesus.INSTANCE);
        register(Spider.INSTANCE);
        register(NoFall.INSTANCE);
        register(WallHack.INSTANCE);
        register(HClip.INSTANCE);
        register(VClip.INSTANCE);
        register(FullBright.INSTANCE);
        register(AntiKB.INSTANCE);
        register(BlockTracker.INSTANCE);
    }

    /**
     * Registers a cheat and creates its corresponding Minecraft key mapping.
     * If a custom keybind exists in the saved settings, it will be used.
     * Otherwise, the cheat's default key is assigned.
     *
     * @param cheat The cheat to register.
     */
    private void register(CheatBase cheat) {
        int keyCode = getStoredKey(cheat);

        KeyMapping mapping = new KeyMapping(
            "key.mcpclient." + cheat.getIdentifier(),
            InputConstants.Type.KEYSYM,
            keyCode,
            KeyMapping.Category.DEBUG
        );

        KeyMappingHelper.registerKeyMapping(mapping);

        registry.add(new KeyBindEntry(mapping, cheat));
    }

    /**
     * Updates the key associated with a specific cheat identifier.
     * The change is immediately applied to Minecraft's key mapping
     * system and persisted to the game configuration.
     *
     * @param identifier The cheat identifier.
     * @param keyCode The new GLFW key code to assign.
     */
    public void updateKey(
        String identifier,
        int keyCode
    ) {
        for (KeyBindEntry entry : registry) {
            if (entry.cheat().getIdentifier().equals(identifier)) {
                entry.mapping().setKey(
                    InputConstants.Type.KEYSYM.getOrCreate(keyCode)
                );

                KeyMapping.resetMapping();
                Minecraft.getInstance().options.save();
                break;
            }
        }
    }

    /**
     * Processes all registered keybindings and executes the toggle
     * action of the corresponding cheat whenever a key press is detected.
     *
     */
    public void tick() {
        for (KeyBindEntry entry : registry) {
            if (entry.mapping().consumeClick()) {
                if (entry.cheat().runOnToggle()) {
                    entry.cheat().toggle();
                } else {
                    entry.cheat().run();
                }
            }
        }
    }

    /**
     * Retrieves the saved keybind associated with a cheat.
     * If no custom keybind has been stored, the cheat's default
     * key assignment is returned.
     *
     * @param cheat The cheat whose keybind should be resolved.
     * @return The GLFW key code assigned to the cheat.
     */
    private int getStoredKey(CheatBase cheat) {
        int savedKey = MCPClient.getSettingsManager()
            .getCheatsSettings()
            .getKeyForKeyBind(cheat.getIdentifier());

        return savedKey == 0
            ? cheat.getDefaultKey()
            : savedKey;
    }

    public int getCurrentKeyCode(String identifier) {
        for (KeyBindEntry entry : registry) {
            if (entry.cheat().getIdentifier().equals(identifier)) {
                KeyMappingAccessor accessor = (KeyMappingAccessor) (Object) entry.mapping();
                return accessor.getKey().getValue();
            }
        }
        
        return 0;
    }
}
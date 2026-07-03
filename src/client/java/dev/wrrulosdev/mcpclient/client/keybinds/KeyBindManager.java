package dev.wrrulosdev.mcpclient.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.*;
    import dev.wrrulosdev.mcpclient.client.exploits.*;
    import dev.wrrulosdev.mcpclient.client.mixins.accessor.KeyMappingAccessor;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class KeyBindManager {

    private final List<KeyBindEntry> registry = new ArrayList<>();

    private record KeyBindEntry(
        KeyMapping mapping,
        Object feature
    ) {
        public String getIdentifier() {
            if (feature instanceof CheatBase cheat) return cheat.getIdentifier();
            if (feature instanceof ExploitBase exploit) return exploit.getIdentifier();
            return "";
        }
    }

    /**
     * Initializes the key bind manager by registering all defined cheats and exploits.
     */
    public KeyBindManager() {
        // Cheats
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

        // Exploits
        register(CloudSync.INSTANCE);
        register(T2C.INSTANCE);
        register(EasyCommandBlocker.INSTANCE);
        register(CommandBridge.INSTANCE);
        register(Atlas.INSTANCE);
        register(MultiChat.INSTANCE);
        register(LiteBans.INSTANCE);
        register(HolographicDisplays.INSTANCE);
        register(PlugManX.INSTANCE);
        register(PluginManager.INSTANCE);
    }

    /**
     * Registers a feature (Cheat or Exploit) as a new KeyMapping.
     *
     * @param feature The feature object to bind to a key
     */
    private void register(Object feature) {
        String identifier = "";
        if (feature instanceof CheatBase cheat) identifier = cheat.getIdentifier();
        else if (feature instanceof ExploitBase exploit) identifier = exploit.getIdentifier();
        if (identifier.isEmpty()) return;
        int keyCode = getStoredKey(feature);

        KeyMapping mapping = new KeyMapping(
            "key.mcpclient." + identifier,
            InputConstants.Type.KEYSYM,
            keyCode,
            KeyMapping.Category.DEBUG
        );
        KeyMappingHelper.registerKeyMapping(mapping);
        registry.add(new KeyBindEntry(mapping, feature));
    }

    /**
     * Updates the key code for a specific registered feature.
     *
     * @param identifier The unique string identifier of the feature
     * @param keyCode    The new key code to be assigned
     */
    public void updateKey(String identifier, int keyCode) {
        for (KeyBindEntry entry : registry) {
            if (entry.getIdentifier().equals(identifier)) {
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
     * Ticks the key bind registry to process user input, triggering toggle or run
     * actions based on the feature type.
     */
    public void tick() {
        for (KeyBindEntry entry : registry) {
            if (entry.mapping().consumeClick()) {
                if (entry.feature() instanceof CheatBase cheat) {
                    if (cheat.runOnToggle()) {
                        cheat.toggle();
                    } else {
                        cheat.run();
                    }
                } else if (entry.feature() instanceof ExploitBase exploit) {
                    exploit.run();
                }
            }
        }
    }

    /**
     * Retrieves the stored key code for a feature from settings, falling back to
     * default if not previously configured.
     *
     * @param feature The feature object to look up
     * @return The configured or default key code
     */
    private int getStoredKey(Object feature) {
        if (feature instanceof CheatBase cheat) {
            int savedKey = MCPClient.getSettingsManager()
                .getCheatsSettings()
                .getKeyForKeyBind(cheat.getIdentifier());

            return savedKey == 0 ? cheat.getDefaultKey() : savedKey;

        } else if (feature instanceof ExploitBase exploit) {
            int savedKey = MCPClient.getSettingsManager()
                .getExploitsSettings()
                .getKeyForKeyBind(exploit.getIdentifier());

            return savedKey == 0 ? exploit.getDefaultKey() : savedKey;
        }

        return 0;
    }

    /**
     * Gets the current numeric key code for a specific feature.
     *
     * @param identifier The unique string identifier of the feature
     * @return The current key code value, or 0 if not found
     */
    public int getCurrentKeyCode(String identifier) {
        for (KeyBindEntry entry : registry) {
            if (entry.getIdentifier().equals(identifier)) {
                KeyMappingAccessor accessor = (KeyMappingAccessor) (Object) entry.mapping();
                return accessor.getKey().getValue();
            }
        }

        return 0;
    }
}
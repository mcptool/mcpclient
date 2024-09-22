package dev.wrrulos.mcpclient.keybinds;

import dev.wrrulos.mcpclient.Mcpclient;
import dev.wrrulos.mcpclient.session.SessionController;
import dev.wrrulos.mcpclient.settings.MCPToolSettings;
import dev.wrrulos.mcpclient.utils.ChatUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/*
    * Manages all keybinds for the client.
    * This class is responsible for initializing all keybinds and handling their respective actions.
 */
public class KeyBindManager {
    private static KeyBinding keyBindingAutoCommand1;
    private static KeyBinding keyBindingAutoCommand2;
    private static KeyBinding keyBindingAutoCommand3;
    public static String AUTO_COMMAND_1;
    public static String AUTO_COMMAND_2;
    public static String AUTO_COMMAND_3;

    /**
     * Initializes all keybinds for the client.
     */
    public void initialize() {
        keyBindingAutoCommand1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCommand 1",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V,
            "MCPClient Commands")
        );

        keyBindingAutoCommand2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCommand 2",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "MCPClient Commands")
        );

        keyBindingAutoCommand3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "AutoCommand 3",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N,
            "MCPClient Commands")
        );

        /*
            * This event is called every tick on the client.
         */
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingAutoCommand1.wasPressed()) {
                System.out.println("Auto command 1 key was pressed!");
                sendCommand(client, AUTO_COMMAND_1);
            }

            while (keyBindingAutoCommand2.wasPressed()) {
                System.out.println("Auto command 2 key was pressed!");
                sendCommand(client, AUTO_COMMAND_2);
            }

            while (keyBindingAutoCommand3.wasPressed()) {
                System.out.println("Auto command 3 key was pressed!");
                sendCommand(client, AUTO_COMMAND_3);
            }
        });
    }

    public void setKeyBindCommands() {
        MCPToolSettings settings = Mcpclient.getSettings();
        AUTO_COMMAND_1 = settings.getAutoCommand1();
        AUTO_COMMAND_2 = settings.getAutoCommand2();
        AUTO_COMMAND_3 = settings.getAutoCommand3();
    }

    /**
     * Sends a command or message to the server when a keybind is pressed.
     *
     * @param client The Minecraft client.
     */
    private static void sendCommand(MinecraftClient client, String command) {
        SessionController sessionController = Mcpclient.getSessionController();
        command = command.replace("%username%", sessionController.getUsername());
        Objects.requireNonNull(client.getNetworkHandler()).sendChatMessage(command);
    }
}

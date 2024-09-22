package dev.wrrulos.mcpclient.utils;

import dev.wrrulos.mcpclient.constants.messages.CommandConstants;

import java.util.ArrayList;
import java.util.List;

public class PluginTabStorage {
    private static final List<String> storedPluginNames = new ArrayList<>();
    private Boolean Listening = false;

    public void addPluginName(String name) {
        if (storedPluginNames.contains(name)) {
            return;
        }

        System.out.println("[LOG] Adding plugin name to storage: " + name);
        storedPluginNames.add(name);
    }

    public void sendStoredPluginNames() {
        if (storedPluginNames.isEmpty()) {
            ChatUtils.sendMessage(CommandConstants.PluginsCommand.NO_PLUGINS);
            return;
        }

        String plugins = String.join(" ", storedPluginNames);
        ChatUtils.sendMessage(CommandConstants.PluginsCommand.PLUGINS_FOUND + plugins);
        storedPluginNames.clear();
    }

    public Boolean getListening() {
        return Listening;
    }

    public void setListening(Boolean listening) {
        Listening = listening;
    }
}
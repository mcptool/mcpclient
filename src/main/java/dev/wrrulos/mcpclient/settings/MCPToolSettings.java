package dev.wrrulos.mcpclient.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MCPToolSettings {

    // Gson instance for JSON serialization/deserialization
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // File location for the configuration file
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "mcptool_settings.json");

    // Configuration fields with default values
    private String autoCommand1;
    private String autoCommand2;
    private String autoCommand3;
    private boolean stealthMode;

    /**
     * Default constructor initializing the settings with default values.
     */
    public MCPToolSettings() {
        this.autoCommand1 = "Hi %username%";
        this.autoCommand2 = "/ver";
        this.autoCommand3 = "This is auto command 3";
        this.stealthMode = false;
    }

    /**
     * Gets the value of autoCommand1.
     *
     * @return the string value of autoCommand1.
     */
    public String getAutoCommand1() {
        return autoCommand1;
    }

    /**
     * Sets the value of autoCommand1.
     *
     * @param autoCommand1 the string to set for autoCommand1.
     */
    public void setAutoCommand1(String autoCommand1) {
        this.autoCommand1 = autoCommand1;
    }

    /**
     * Gets the value of autoCommand2.
     *
     * @return the string value of autoCommand2.
     */
    public String getAutoCommand2() {
        return autoCommand2;
    }

    /**
     * Sets the value of autoCommand2.
     *
     * @param autoCommand2 the string to set for autoCommand2.
     */
    public void setAutoCommand2(String autoCommand2) {
        this.autoCommand2 = autoCommand2;
    }

    /**
     * Gets the value of autoCommand3.
     *
     * @return the string value of autoCommand3.
     */
    public String getAutoCommand3() {
        return autoCommand3;
    }

    /**
     * Sets the value of autoCommand3.
     *
     * @param autoCommand3 the string to set for autoCommand3.
     */
    public void setAutoCommand3(String autoCommand3) {
        this.autoCommand3 = autoCommand3;
    }

    /**
     * Gets the current value of stealthMode.
     *
     * @return true if stealthMode is enabled, false otherwise.
     */
    public boolean isStealthMode() {
        return stealthMode;
    }

    /**
     * Sets the value of stealthMode.
     *
     * @param stealthMode the boolean value to set for stealthMode.
     */
    public void setStealthMode(boolean stealthMode) {
        this.stealthMode = stealthMode;
    }

    /**
     * Saves the current settings to the configuration file in JSON format.
     */
    public void save() {
        try {
            // Ensure the config directory exists
            File configDir = CONFIG_FILE.getParentFile();

            if (!configDir.exists()) {
                configDir.mkdirs(); // Creates the directory if it does not exist
            }

            // Write the configuration to the file
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
                System.out.println("Settings saved to: " + CONFIG_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save settings to: " + CONFIG_FILE.getAbsolutePath());
        }
    }

    /**
     * Loads the settings from the configuration file. If the file does not exist,
     * it creates a new one with default values.
     *
     * @return an instance of MCPToolSettings with loaded or default values.
     */
    public static MCPToolSettings load() {
        // Check if the config file exists
        if (!CONFIG_FILE.exists()) {
            System.out.println("Config file not found, creating new one at: " + CONFIG_FILE.getAbsolutePath());
            MCPToolSettings settings = new MCPToolSettings();
            settings.save();  // Saves the file if it does not exist
            return settings;
        }

        // Load the settings from the file
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            MCPToolSettings settings = GSON.fromJson(reader, MCPToolSettings.class);
            System.out.println("Settings loaded from: " + CONFIG_FILE.getAbsolutePath());
            return settings;
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to load settings from: " + CONFIG_FILE.getAbsolutePath());
        }

        return new MCPToolSettings();  // Return default settings if an error occurs
    }
}

package dev.wrrulos.mcpclient.utils;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.io.*;

public class MCPToolCLI {

    /**
     * Executes a system command in the appropriate working directory based on the operating system.
     * This method runs on a separate thread to avoid freezing the Minecraft client.
     * @param command The system command to be executed.
     */
    public static void runCommand(String command) {
        // Run the command in a separate thread to avoid freezing the Minecraft client
        new Thread(() -> {
            try {
                // Detect the operating system (Windows, Linux/MacOS)
                String os = System.getProperty("os.name").toLowerCase();

                // Currently, the command execution is supported only for Windows
                if (!os.contains("win")) {
                    ChatUtils.sendMessage("§cMCPTool is only supported on Windows.");
                    return;
                }

                // Prepare ProcessBuilder for either Windows (cmd.exe) or Linux/MacOS (sh)
                ProcessBuilder processBuilder;
                if (os.contains("win")) {
                    processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                } else {
                    processBuilder = new ProcessBuilder("sh", "-c", command);
                }

                // Define the working directory based on the OS environment
                File workingDirectory;
                if (os.contains("win")) {
                    String appData = System.getenv("APPDATA"); // Retrieve %APPDATA% directory for Windows
                    workingDirectory = new File(appData, "MCPToolData"); // Set to MCPToolData folder
                } else {
                    String home = System.getProperty("user.home"); // Retrieve home directory for Linux/MacOS
                    workingDirectory = new File(home, ".config/MCPToolData"); // Set to .config/MCPToolData folder
                }

                // Debug output for the working directory (can be removed in production)
                System.out.println(workingDirectory);

                // Verify if the working directory exists and is valid
                if (!workingDirectory.exists() || !workingDirectory.isDirectory()) {
                    ChatUtils.sendMessage("§cMCPToolData directory not found.");
                    return;
                }

                // Locate the 'path' file inside the working directory
                File pathFile = new File(workingDirectory, "path");

                // Check if the 'path' file exists and is a valid file
                if (!pathFile.exists() || !pathFile.isFile()) {
                    ChatUtils.sendMessage("§cMCPTool path file not found.");
                    return;
                }

                // Read the content of the 'path' file to retrieve the execution directory
                String targetDirectory;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathFile)))) {
                    targetDirectory = reader.readLine(); // The first line contains the directory path
                } catch (Exception e) {
                    ChatUtils.sendMessage("§cFailed to read MCPTool path file.");
                    return;
                }

                // Validate the target execution directory specified in the 'path' file
                File executionDirectory = new File(targetDirectory);

                if (!executionDirectory.exists() || !executionDirectory.isDirectory()) {
                    ChatUtils.sendMessage("§cMCPTool execution directory not found.");
                    // &cThe directory specified in the 'path' file does not exist.
                    return;
                }

                // Inform the player that MCPTool is being executed
                ChatUtils.sendMessage("§aExecuting MCPTool...");

                // Set the directory where the command will be executed
                processBuilder.directory(executionDirectory);

                // Start the process
                Process process = processBuilder.start();

                // Force the process to flush its output (ensures real-time output)
                try (Writer stdIn = new OutputStreamWriter(process.getOutputStream())) {
                    stdIn.flush();
                }

                // Capture and handle the process's stdout in real-time
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                // Create a thread to handle stdout and send the output to the player in real time
                new Thread(() -> {
                    try {
                        String line;
                        while ((line = stdInput.readLine()) != null) {
                            String finalLine = translateColorCodes(line); // Translate color codes for Minecraft
                            ChatUtils.sendMessage(finalLine);
                        }
                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to read MCPTool output: " + e.getMessage());
                    }
                }).start();

                // Create a separate thread to handle stderr and display error messages in real time
                new Thread(() -> {
                    try {
                        String line;

                        while ((line = stdError.readLine()) != null) {
                            String finalLine = translateColorCodes(line); // Translate color codes for Minecraft
                            ChatUtils.sendMessage(finalLine);
                        }
                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to read MCPTool error output: " + e.getMessage());
                    }
                }).start();

                // Wait for the process to finish execution
                process.waitFor();

            } catch (Exception e) {
                System.out.println("[ERROR] Failed to execute MCPTool: " + e.getMessage());
                // Notify the player if MCPTool could not be found or executed
                ChatUtils.sendMessage("§cFailed to execute MCPTool.");
                // &cMCPTool was not found on the system. If you have it installed, change the MCPTool path value in settings.
            }
        }).start(); // Start the thread to prevent blocking the main game thread
    }

    /**
     * Translates Minecraft-style color codes (&1, &2, etc.) to the § symbol.
     * @param input The input string containing & color codes.
     * @return The translated string with § color codes.
     */
    private static String translateColorCodes(String input) {
        return input.replaceAll("&([0-9a-fk-or])", "§$1");
    }
}

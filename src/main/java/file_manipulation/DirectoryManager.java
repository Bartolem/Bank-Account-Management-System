package file_manipulation;

import logging.LoggerConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class DirectoryManager {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void createDirectory(Path directoryPath) {
        // Check if the directory does not exist
        if (!Files.exists(directoryPath)) {
            try {
                // Attempt to create the directory
                Files.createDirectories(directoryPath);
                LOGGER.finest("Directory created successfully: " + directoryPath);
            } catch (IOException e) {
                System.out.println();
                LOGGER.severe("Failed to create directory: " + directoryPath + ":" + e.getMessage());
            }
        } else {
            LOGGER.info("Directory already exists: " + directoryPath);
        }
    }
}

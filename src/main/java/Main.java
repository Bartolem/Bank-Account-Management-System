import file_manipulation.FileManipulator;
import logging.LoggerConfig;
import user_interface.UserInterface;

public class Main {
    public static void main(String[] args) {
        // Setup logger and enable logging to file
        LoggerConfig.setupLogger();

        // Check for the "-setup" argument to properly set up the program
        if (args.length > 0 && args[0].equals("-setup")) FileManipulator.createNecessaryDirectoriesAndFiles();

        // Check for the "-log" argument to enable detailed logging to console
        if (args.length > 0 && args[0].equals("-log")) LoggerConfig.enableConsoleLog();

        // Initialize user interface and starts the program
        new UserInterface().initialize();
    }
}
package logging;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    private static final Logger LOGGER = Logger.getLogger(LoggerConfig.class.getName());
    private static final String LOG_FILE_PATH = "/logging.properties";

    public static void setupLogger() {
        try {
            LogManager.getLogManager().readConfiguration(LoggerConfig.class.getResourceAsStream(LOG_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void enableConsoleLog() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

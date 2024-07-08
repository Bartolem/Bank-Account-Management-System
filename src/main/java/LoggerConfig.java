import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    private static final Logger LOGGER = Logger.getLogger(LoggerConfig.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(LoggerConfig.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

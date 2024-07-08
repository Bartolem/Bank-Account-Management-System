package file_manipulation;

import java.io.*;
import java.util.Objects;
import java.util.logging.Logger;

public class LogoLoader {
    private static final Logger LOGGER = Logger.getLogger(AccountNumberCSVHandler.class.getName());

    public static String read(String fileName) {
        StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(LogoLoader.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;

            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }

            LOGGER.info("Logo successfully loaded from " + fileName);
            return resultStringBuilder.toString();
        } catch (IOException e) {
            LOGGER.severe("Failed to load logo from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

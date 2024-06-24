package file_manipulation;

import java.io.*;
import java.util.Objects;

public class LogoLoader {
    public static String read(String fileName) {
        StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(LogoLoader.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;

            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }

            return resultStringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

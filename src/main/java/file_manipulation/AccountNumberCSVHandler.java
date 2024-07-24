package file_manipulation;

import logging.LoggerConfig;

import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AccountNumberCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(Set<Integer> accountNumbers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int number : accountNumbers) {
                String formattedNumber = String.format("%s,", number);
                if (accountNumbers.stream().toList().indexOf(number) == accountNumbers.size()-1) {
                    formattedNumber = String.format("%s", number);
                }
                writer.write(formattedNumber);
            }

            writer.close();
            LOGGER.finest("Account numbers successfully saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save account numbers to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void read(Set<Integer> accountNumbers, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                // Read account numbers detail
                String[] fileContent = line.split(",");

                for (String number : fileContent) {
                    accountNumbers.add(Integer.valueOf(number));
                }
            }
            LOGGER.finest("Account numbers successfully loaded from " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load account numbers from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

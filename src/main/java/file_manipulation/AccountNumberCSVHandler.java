package file_manipulation;

import logging.LoggerConfig;
import user_interface.UserInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AccountNumberCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(ArrayList<Integer> accountNumbers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int number : accountNumbers) {
                String formattedNumber = String.format("%s,", number);
                if (accountNumbers.indexOf(number) == accountNumbers.size()-1) {
                    formattedNumber = String.format("%s", number);
                }
                writer.write(formattedNumber);
            }

            writer.close();
            if (UserInterface.isLoggingEnabled()) LOGGER.info("Account numbers successfully saved to " + fileName);
        } catch (IOException e) {
            if (UserInterface.isLoggingEnabled()) LOGGER.severe("Failed to save account numbers to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void read(ArrayList<Integer> accountNumbers, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                // Read account numbers detail
                String[] fileContent = line.split(",");

                for (String number : fileContent) {
                    accountNumbers.add(Integer.valueOf(number));
                }
            }
            if (UserInterface.isLoggingEnabled()) LOGGER.info("Account numbers successfully loaded from " + fileName);
        } catch (IOException e) {
            if (UserInterface.isLoggingEnabled()) LOGGER.severe("Failed to load account numbers from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

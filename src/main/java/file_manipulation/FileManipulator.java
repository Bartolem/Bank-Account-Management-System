package file_manipulation;

import bank.Bank;
import logging.LoggerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FileManipulator {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void createNecessaryDirectoriesAndFiles() {
        DirectoryManager.createDirectory(Path.of("logs"));
        DirectoryManager.createDirectory(Path.of("transactions"));
        DirectoryManager.createDirectory(Path.of("users"));
        DirectoryManager.createDirectory(Path.of("accounts"));

        FileManipulator.createEmptyFile(Path.of("users/users.csv"));
        FileManipulator.createEmptyFile(Path.of("users/user_credentials.csv"));
        FileManipulator.createEmptyFile(Path.of("accounts/accounts.csv"));
        FileManipulator.createEmptyFile(Path.of("accounts/account_numbers.csv"));
    }

    public static void loadDataFromFile() {
        UsersCSVHandler.read(Bank.getInstance(), new File("users/users.csv").getAbsolutePath());
        AccountsCSVHandler.read(Bank.getInstance(), new File("accounts/accounts.csv").getAbsolutePath());
        AccountNumberCSVHandler.read(Bank.getInstance().getAccountNumbers(), new File("accounts/account_numbers.csv").getAbsolutePath());
    }

    public static void saveDataToFile() {
        UsersCSVHandler.write(Bank.getInstance().getAllUsers(), new File("users/users.csv").getAbsolutePath());
        AccountsCSVHandler.write(Bank.getInstance().getAllAccounts(), new File("accounts/accounts.csv").getAbsolutePath());
        AccountNumberCSVHandler.write(Bank.getInstance().getAccountNumbers(), new File("accounts/account_numbers.csv").getAbsolutePath());
    }

    public static void createEmptyFile(Path filePath) {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                LOGGER.finest("File created successfully: " + filePath);
            } else {
                LOGGER.info("File already exists: " + filePath);
            }
        } catch (IOException e) {
            LOGGER.severe("Failed to create file: " + filePath + ":" + e.getMessage());
        }
    }
}

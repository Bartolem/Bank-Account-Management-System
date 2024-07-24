package file_manipulation;

import accounts.Account;
import accounts.TransactionLimitDateRange;
import bank.Bank;
import logging.LoggerConfig;
import transaction.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class FileManipulator {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void createNecessaryDirectoriesAndFiles() {
        DirectoryManager.createDirectory(Path.of("logs"));
        DirectoryManager.createDirectory(Path.of("transactions"));
        DirectoryManager.createDirectory(Path.of("users"));
        DirectoryManager.createDirectory(Path.of("accounts"));
        DirectoryManager.createDirectory(Path.of("transaction_limits/daily_limits"));
        DirectoryManager.createDirectory(Path.of("transaction_limits/monthly_limits"));

        FileManipulator.createEmptyFile(Path.of("users/users.csv"));
        FileManipulator.createEmptyFile(Path.of("users/user_credentials.csv"));
        FileManipulator.createEmptyFile(Path.of("accounts/accounts.csv"));
        FileManipulator.createEmptyFile(Path.of("accounts/account_numbers.csv"));
    }

    public static void saveTransactionHistoryToFile(List<Transaction> transactionHistory, int accountNumber) {
        TransactionHistoryCSVHandler.write(transactionHistory, new File("transactions/transaction_history_" + accountNumber + ".csv").getAbsolutePath());
    }

    public static void loadTransactionHistoryFromFile(int accountNumber) {
        TransactionHistoryCSVHandler.read(new File("transactions/transaction_history_" + accountNumber + ".csv").getAbsolutePath());
    }

    public static void saveTransactionLimitToFile(Account account) {
        TransactionLimitsCSVHandler.write(account, new File("transaction_limits/daily_limits/daily_limits_" + account.getAccountNumber() + ".csv").getAbsolutePath(), TransactionLimitDateRange.DAILY);
        TransactionLimitsCSVHandler.write(account, new File("transaction_limits/monthly_limits/monthly_limits_" + account.getAccountNumber() + ".csv").getAbsolutePath(), TransactionLimitDateRange.MONTHLY);
    }

    public static void loadTransactionLimitFromFile(Account account) {
        TransactionLimitsCSVHandler.read(account, new File("transaction_limits/daily_limits/daily_limits_" + account.getAccountNumber() + ".csv").getAbsolutePath(), TransactionLimitDateRange.DAILY);
        TransactionLimitsCSVHandler.read(account, new File("transaction_limits/monthly_limits/monthly_limits_" + account.getAccountNumber() + ".csv").getAbsolutePath(), TransactionLimitDateRange.MONTHLY);
    }

    public static void loadDataFromFile() {
        UsersCSVHandler.read(Bank.getInstance(), new File("users/users.csv").getAbsolutePath());
        AccountsCSVHandler.read(Bank.getInstance(), new File("accounts/accounts.csv").getAbsolutePath());
        AccountNumberCSVHandler.read(new HashSet<>(), new File("accounts/account_numbers.csv").getAbsolutePath());
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

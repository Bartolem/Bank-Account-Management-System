package file_manipulation;

import bank.Bank;

import java.io.File;
import java.io.IOException;

public class FileManipulator {
    public static void loadDataFromFile() {
        try {
            CSVToUsers.read(Bank.getInstance(), new File("users.csv").getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        CSVToAccounts.read(Bank.getInstance(), "accounts.csv");
        CSVToAccountNumber.read(Bank.getInstance().getAccountNumbers(), "account_numbers.csv");
    }

    public static void saveDataToFile() {
        try {
            UsersToCSV.write(Bank.getInstance().getAllUsers(), new File("users.csv").getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        AccountsToCSV.write(Bank.getInstance().getAllAccounts(), "accounts.csv");
        AccountNumberToCSV.write(Bank.getInstance().getAccountNumbers(), "account_numbers.csv");
    }
}

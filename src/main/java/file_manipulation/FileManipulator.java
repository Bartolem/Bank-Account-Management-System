package file_manipulation;

import bank.Bank;

import java.io.File;

public class FileManipulator {
    public static void loadDataFromFile() {
        CSVToUsers.read(Bank.getInstance(), new File("users.csv").getAbsolutePath());
        CSVToAccounts.read(Bank.getInstance(), new File("accounts.csv").getAbsolutePath());
        CSVToAccountNumber.read(Bank.getInstance().getAccountNumbers(), new File("account_numbers.csv").getAbsolutePath());
    }

    public static void saveDataToFile() {
        UsersToCSV.write(Bank.getInstance().getAllUsers(), new File("users.csv").getAbsolutePath());
        AccountsToCSV.write(Bank.getInstance().getAllAccounts(), new File("accounts.csv").getAbsolutePath());
        AccountNumberToCSV.write(Bank.getInstance().getAccountNumbers(), new File("account_numbers.csv").getAbsolutePath());
    }
}

package file_manipulation;

import bank.Bank;

import java.io.File;

public class FileManipulator {
    public static void loadDataFromFile() {
        UsersCSVHandler.read(Bank.getInstance(), new File("users.csv").getAbsolutePath());
        AccountsCSVHandler.read(Bank.getInstance(), new File("accounts.csv").getAbsolutePath());
        AccountNumberCSVHandler.read(Bank.getInstance().getAccountNumbers(), new File("account_numbers.csv").getAbsolutePath());
    }

    public static void saveDataToFile() {
        UsersCSVHandler.write(Bank.getInstance().getAllUsers(), new File("users.csv").getAbsolutePath());
        AccountsCSVHandler.write(Bank.getInstance().getAllAccounts(), new File("accounts.csv").getAbsolutePath());
        AccountNumberCSVHandler.write(Bank.getInstance().getAccountNumbers(), new File("account_numbers.csv").getAbsolutePath());
    }
}

package file_manipulation;

import bank.Bank;

public class FileManipulator {
    public static void loadDataFromFile() {
        CSVToUsers.read(Bank.getInstance(), "users.csv");
        CSVToAccounts.read(Bank.getInstance(), "accounts.csv");
        CSVToAccountNumber.read(Bank.getInstance().getAccountNumbers(), "account_numbers.csv");
    }

    public static void saveDataToFile() {
        UsersToCSV.write(Bank.getInstance().getAllUsers(), "src/main/resources/users.csv");
        AccountsToCSV.write(Bank.getInstance().getAllAccounts(), "src/main/resources/accounts.csv");
        AccountNumberToCSV.write(Bank.getInstance().getAccountNumbers(), "src/main/resources/account_numbers.csv");
    }
}

package user_interface;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.SavingsAccount;
import file_manipulation.*;
import users.User;

import java.math.BigDecimal;
import java.util.Scanner;

public class AdminPanel extends UserPanel {
    private final User user;

    public AdminPanel(String ID, Scanner scanner) {
        super(ID, scanner);
        this.user = getUser();
    }

    @Override
    public void start() {
        System.out.println("* Admin panel *");
        loop:while (true) {
            System.out.println("\nChoose action");
            System.out.println("(1) Menage accounts");
            System.out.println("(2) Menage users");
            System.out.println("(3) Set bank system settings");
            System.out.println("(4) View bank details");
            System.out.println("(X) Log out");
            printCursor();
            String action = getScanner().nextLine();

            switch (action) {
                case "1" -> menageAccounts();
                case "2" -> menageUsers();
                case "3" -> setSystemSettings();
                case "4" -> bankDetails();
                case "x", "X" -> {
                    break loop;
                }
            }
        }
    }

    private void menageUsers() {
        System.out.println("(1) Remove user");
        System.out.println("(2) Block user");
        System.out.println("(3) Show user details");
        System.out.println("(X) Quit");
    }

    private void menageAccounts() {
        System.out.println("(1) Remove account from bank");
        System.out.println("(2) Block an account");
        System.out.println("(3) Show account details");
        System.out.println("(4) Show account transaction history");
        System.out.println("(X) Quit");

        switch (getScanner().nextLine()) {
            case "1" -> removeAccount();
            case "2" -> blockAccount();
            case "3" -> showAccountDetail();
            case "4" -> showTransactionHistory();
            case "X, x" -> start();
        }
    }

    private void showAccountDetail() {
        System.out.println(getBank().getAccount(verifyAccountNumber()));
    }

    private void removeAccount() {
        getBank().remove(verifyAccountNumber(), user);
        saveDataToFile();
    }

    private void blockAccount() {
        Account account = getBank().getAccount(verifyAccountNumber());
        if (account.isBlocked()) {
            System.out.print("Account is currently blocked, do you want to unlock it? (y/n): ");
            if (getScanner().nextLine().equals("y") || getScanner().nextLine().equals("yes")) {
                unlockAccount(account);
            } else menageAccounts();
        } else {
            account.block();
        }
        saveDataToFile();
    }

    private void unlockAccount(Account account) {
        account.unlock();
        saveDataToFile();
    }

    private void showTransactionHistory() {
        System.out.println(getBank().getAccount(verifyAccountNumber()).getTransactionHistory());
    }

    private void setSystemSettings() {
        System.out.println("(1) Set interest rate for savings account [0.3-8%] (global)");
        System.out.println("(2) Set minimal balance required for savings account [0-25000] (global)");
        System.out.println("(3) Set overdraft limit for current account [300-5000] (global)");
        printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> {
                System.out.println(SavingsAccount.getInterestRate());
                System.out.print("Interest rate: ");
                SavingsAccount.setInterestRate(new BigDecimal((getScanner().nextLine())));
                System.out.println(SavingsAccount.getInterestRate());
            }
            case "2" -> {
                System.out.print("Minimal balance: ");
                SavingsAccount.setMinBalance(new BigDecimal(getScanner().nextLine()));
            }
            case "3" -> {
                System.out.print("Overdraft limit: ");
                CurrentAccount.setOverdraftLimit(new BigDecimal(getScanner().nextLine()));
            }
        }
    }

    private void showSettings() {

    }

    private int verifyAccountNumber() {
        System.out.print("Account number: ");
        String accountNumber = getScanner().nextLine();

        if (checkAccountNumber(accountNumber) || accountNumber.length() != 8) {
            System.out.println("Invalid account number.");
            menageAccounts();
        }
        return Integer.parseInt(accountNumber);
    }

    private void saveDataToFile() {
        UsersToCSV.write(getBank().getAllUsers(), "users.csv");
        AccountsToCSV.write(getBank().getAllAccounts(), "accounts.csv");
        AccountNumberToCSV.write(getBank().getAccountNumbers(), "account_numbers.csv");
    }

    private void bankDetails() {
        System.out.println("(1) Show bank statistics");
        System.out.println("(2) Show system settings (interest rate, overdraft limit etc.)");
        System.out.println("(3) Print all accounts");
        System.out.println("(4) Print all users");
        System.out.println("(5) Print all account types");
        System.out.println("(6) Print all available currencies");
        System.out.println("(X) Quit");
        printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> System.out.println(getBank());
            case "2" -> showSettings();
            case "3" -> getBank().printAccounts();
            case "4" -> getBank().printUsers();
            case "5" -> getBank().printAccountTypes();
            case "6" -> getBank().printCurrencies();
            case "X, x" -> start();
        }
    }
}

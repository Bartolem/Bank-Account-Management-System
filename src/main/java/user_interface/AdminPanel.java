package user_interface;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.SavingsAccount;
import file_manipulation.*;
import users.User;
import validation.Validation;

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
            UserInterface.printCursor();
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
        System.out.println("Account successfully deleted from bank.");
        FileManipulator.saveDataToFile();
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
        FileManipulator.saveDataToFile();
    }

    private void unlockAccount(Account account) {
        account.unlock();
        FileManipulator.saveDataToFile();
    }

    private void showTransactionHistory() {
        Account account = getBank().getAccount(verifyAccountNumber());
        FileManipulator.loadTransactionHistoryFromFile(account.getAccountNumber());
        FileManipulator.loadTransactionLimitFromFile(account);
        System.out.println(account.getTransactionHistory());
    }

    private void setSystemSettings() {
        System.out.println("(1) Set interest rate for savings account [0.3-8%] (global)");
        System.out.println("(2) Set minimal balance required for savings account [0-25000] (global)");
        System.out.println("(3) Set overdraft limit for current account [300-5000] (global)");
        UserInterface.printCursor();
        String input;

        switch (getScanner().nextLine()) {
            case "1" -> {
                System.out.print("Interest rate: ");
                input = getScanner().nextLine();
                if (Validation.validateNumber(input)) {
                    SavingsAccount.setInterestRate(new BigDecimal((input)));
                }
            }
            case "2" -> {
                System.out.print("Minimal balance: ");
                input = getScanner().nextLine();
                if (Validation.validateNumber(input)) {
                    SavingsAccount.setMinBalance(new BigDecimal(input));
                }
            }
            case "3" -> {
                System.out.print("Overdraft limit: ");
                input = getScanner().nextLine();
                if (Validation.validateNumber(input)) {
                    CurrentAccount.setOverdraftLimit(new BigDecimal(input));
                }
            }
        }
    }

    private void showSettings() {
        System.out.println("Interest rate (Savings): " + SavingsAccount.getInterestRate() + "%");
        System.out.println("Minimal balance (Savings): " + SavingsAccount.getMinBalance());
        System.out.println("Overdraft limit (Current): " + CurrentAccount.getOverdraftLimit());
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

    private void bankDetails() {
        System.out.println("(1) Show bank statistics");
        System.out.println("(2) Show system settings (interest rate, overdraft limit etc.)");
        System.out.println("(3) Print all accounts");
        System.out.println("(4) Print all users");
        System.out.println("(5) Print all account types");
        System.out.println("(6) Print all available currencies");
        System.out.println("(7) Print all user roles");
        System.out.println("(X) Quit");
        UserInterface.printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> System.out.println(getBank());
            case "2" -> showSettings();
            case "3" -> getBank().printAccounts();
            case "4" -> getBank().printUsers();
            case "5" -> getBank().printAccountTypes();
            case "6" -> getBank().printCurrencies();
            case "7" -> getBank().printRoles();
            case "X, x" -> start();
        }
    }
}

package user_interface;

import users.User;

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
        System.out.println("(1) Remove account");
        System.out.println("(2) Block account");
        System.out.println("(3) Show account details");
        System.out.println("(X) Quit");

        switch (getScanner().nextLine()) {
            case "1" -> removeAccount();
            case "2" -> blockAccount();
            case "3" -> showAccountDetail();
            case "X, x" -> start();
        }
    }

    private void showAccountDetail() {
        getBank().getAccount(verifyAccountNumber());
    }

    private void blockAccount() {
        // Implement block account function
    }

    private void setSystemSettings() {

    }

    private void removeAccount() {
        getBank().remove(verifyAccountNumber(), user);
    }

    private int verifyAccountNumber() {
        System.out.print("Account number: ");
        String accountNumber = getScanner().nextLine();

        if (!checkAccountNumber(accountNumber) || accountNumber.length() != 8) {
            System.out.println("Invalid account number.");
            menageAccounts();
        }
        return Integer.parseInt(accountNumber);
    }

    private void bankDetails() {
        System.out.println("(1) Show bank details");
        System.out.println("(2) Print all accounts");
        System.out.println("(3) Print all users");
        System.out.println("(4) Print all account types");
        System.out.println("(5) Print all available currencies");
        System.out.println("(X) Quit");

        switch (getScanner().nextLine()) {
            case "1" -> System.out.println(getBank());
            case "2" -> getBank().printAccounts();
            case "3" -> getBank().printUsers();
            case "4" -> getBank().printAccountTypes();
            case "5" -> getBank().printCurrencies();
            case "X, x" -> start();
        }
    }
}

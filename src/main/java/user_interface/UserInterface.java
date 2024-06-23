package user_interface;

import accounts.Account;
import bank.Bank;
import file_manipulation.FileManipulator;
import file_manipulation.LogoLoader;
import file_manipulation.TransactionHistoryToCSV;
import users.Admin;
import users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static authentication.Role.ACCOUNT_OWNER;
import static authentication.Role.ADMIN;

public class UserInterface {
    private final Bank bank;
    private final Scanner scanner;
    private final Registration registration;
    private final UserCreation userCreation;
    private final AccountCreation accountCreation;

    public UserInterface(Scanner scanner, Registration register, UserCreation userCreation, AccountCreation accountCreation) {
        this.bank = Bank.getInstance();
        this.scanner = scanner;
        this.registration = register;
        this.userCreation = userCreation;
        this.accountCreation = accountCreation;
    }

    public void start() {
        FileManipulator.loadDataFromFile();
        printLogo();
        System.out.println("Welcome to Bartolem's Online Banking Application.");

        loop: while (true) {
            printStartingMessage();
            printCursor();
            switch (scanner.nextLine()) {
                case "1":
                    // Log in to the system
                    login();
                    break;
                case "2":
                    // Create new account
                    accountCreation();
                    FileManipulator.loadDataFromFile();
                    break;
                case "X":
                case "x":
                    // Exit
                    break loop;
            }
        }

        scanner.close();
        System.exit(0);
    }

    private void accountCreation() {
        User user = userCreation.createUser();
        System.out.println(user);
        Account account = accountCreation.createAccount(user);
        if (register(user, account)) {
            addUserAndAccountToBank(user, account);
        } else System.out.println("Registration process failed.");
    }

    private void login() {
        printLoginMessage();
        System.out.print("ID: ");
        String ID = scanner.nextLine();

        if (bank.getUser(ID) == null) {
            System.out.println("There is no account with provided ID.");
            login();
        }

        printCursor();
        char[] password = System.console().readPassword("Enter your password: ");

        Login login = new Login(ID, Arrays.toString(password));
        if (bank.getUser(ID).hasRole(ADMIN)) {
            if (login.verifyUser()) {
                // Open admin panel using only ID
                AdminPanel adminPanel = new AdminPanel(ID, scanner);
                adminPanel.start();
                FileManipulator.saveDataToFile();
            } else login();
        } else if (bank.getUser(ID).hasRole(ACCOUNT_OWNER)) {
            System.out.print("Account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine());
            if (login.verifyAccount(accountNumber)) {
                // Open account owner panel using ID, and account number
                AccountOwnerPanel ownerPanel = new AccountOwnerPanel(ID, scanner, accountNumber, userCreation);
                ownerPanel.start();
                FileManipulator.saveDataToFile();
            } else login();
        }
    }

    private boolean register(User user, Account account) {
        while (true) {
            System.out.println("Provide password for your new account.");
            printCursor();
            char[] password = System.console().readPassword("Enter your password: ");

            if (!registration.checkPasswordLength(Arrays.toString(password))) {
                System.out.println("Password need be at least 5 characters long.");
            } else {
                System.out.println("Confirm provided password.");
                printCursor();

                if (registration.checkPasswordsEquality(Arrays.toString(password), Arrays.toString(System.console().readPassword()))) {
                    String ID = user.getPerson().getID();
                    registration.registerUser(ID, Arrays.toString(password));
                    System.out.println("You registration process has been successfully completed.");
                    System.out.println("accounts.Account number: " + account.getAccountNumber());
                    System.out.println("ID: " + ID);
                    return true;
                }

                System.out.println("Try again.");
            }
        }
    }

    protected static void printCursor() {
        System.out.print("> ");
    }

    private void addUserAndAccountToBank(User user, Account account) {
        bank.addUser(user);
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        TransactionHistoryToCSV.write(new ArrayList<>(), "transaction_history_" + account.getAccountNumber());
        FileManipulator.saveDataToFile();
    }

    private void printStartingMessage() {
        System.out.println();
        System.out.println("Choose right option.");
        System.out.println("(1) Open account that is already created.");
        System.out.println("(2) Create an account.");
        System.out.println("(X) Exit.");
    }

    private void printLoginMessage() {
        System.out.println("Enter your user ID and password, to log in.");
    }

    protected static void printLogo() {
        System.out.println(LogoLoader.read("ascii logo.txt"));
    }
}

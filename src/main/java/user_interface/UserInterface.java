package user_interface;

import accounts.Account;
import bank.Bank;
import file_manipulation.CSVToTransactionHistory;
import file_manipulation.FileManipulator;
import file_manipulation.LogoLoader;
import file_manipulation.TransactionHistoryToCSV;
import users.Admin;
import users.User;

import java.io.File;
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

    public UserInterface() {
        this.bank = Bank.getInstance();
        this.scanner = new Scanner(System.in);
        this.registration = new Registration();
        this.userCreation = new UserCreation(scanner, this);
        this.accountCreation = new AccountCreation(scanner);
    }

    public void initialize() {
        FileManipulator.loadDataFromFile();
        System.out.println("Welcome to Bartolem's Online Banking Application.");
        start();
    }

    public void start() {
        printLogo();

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
                    break;
                case "x", "X":
                    // Exit
                    break loop;
            }
        }

        scanner.close();
        System.exit(0);
    }

    private void accountCreation() {
        System.out.println("Have you used our bank's services before? (y/n)");
        printCursor();
        String answer = scanner.nextLine();

        User user;

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            System.out.println("Enter your user ID: ");
            printCursor();

            String ID = scanner.nextLine();
            user = bank.getUser(ID);

            if (user == null) {
                System.out.println("There is no user with provided ID.");
                start();
            } else if (user.getNumberOfOwnedAccounts() == User.MAX_NUMBER_OF_ACCOUNTS) {
                System.out.println("The limit for the maximum number of accounts to be created has been reached");
                start();
            }
        } else {
            user = userCreation.createUser();
            bank.addUser(user);
            System.out.println(user);
        }
        Account account = accountCreation.createAccount(user);
        if (register(user, account)) {
            addAccountToBank(account);
        } else System.out.println("Registration process failed.");
    }

    private void login() {
        printLoginMessage();
        System.out.print("Enter your user ID: ");
        String ID = scanner.nextLine();

        if (ID.equalsIgnoreCase("X")) {
            start();
        }
        if (bank.getUser(ID) == null) {
            System.out.println("There is no account with provided ID.");
            login();
        }

        char[] password = System.console().readPassword("Enter your password: ");

        Login login = new Login(ID, Arrays.toString(password));
        if (bank.getUser(ID).hasRole(ADMIN)) {
            if (login.verifyUser()) {
                // Open admin panel using only ID
                new AdminPanel(ID, scanner).start();
                FileManipulator.saveDataToFile();
            } else login();
        } else if (bank.getUser(ID).hasRole(ACCOUNT_OWNER)) {
            System.out.print("Account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine());
            if (login.verifyAccount(accountNumber)) {
                // Open account owner panel using ID, and account number
                new AccountOwnerPanel(ID, scanner, accountNumber, userCreation, this).initialize();
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
                    System.out.println("Account number: " + account.getAccountNumber());
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

    protected static void  printBorder() {
        System.out.println("==================================================");
    }

    private void addAccountToBank(Account account) {
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        TransactionHistoryToCSV.write(new ArrayList<>(), new File("transactions/transaction_history_" + account.getAccountNumber() + ".csv").getAbsolutePath());
        CSVToTransactionHistory.read(new File("transactions/transaction_history_" + account.getAccountNumber() + ".csv").getAbsolutePath());
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
        System.out.println("Enter your user ID and password, to log in. Type (X) to exit.");
    }

    protected static void printLogo() {
        System.out.println(LogoLoader.read("ascii logo.txt"));
    }
}

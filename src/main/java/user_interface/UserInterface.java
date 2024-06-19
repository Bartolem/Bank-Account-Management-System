package user_interface;

import accounts.*;
import bank.Bank;
import users.*;
import file_manipulation.*;
import java.util.*;

import static authentication.Role.*;

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
        loadDataFromFile();
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
                    loadDataFromFile();
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
        Account account = accountCreation.createAccount(user);
        if (register(user, account)) {
            addUserAndAccountToBank(user, account);
        } else System.out.println("Registration process failed.");
    }

    private void login() {
        printLoginMessage();
        System.out.print("ID: ");
        String ID = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Login login = new Login(ID, password);
        if (bank.getUser(ID).hasRole(ADMIN)) {
            if (login.verifyUser()) {
                // Open admin panel using only ID
                AdminPanel adminPanel = new AdminPanel(ID, scanner);
                adminPanel.start();
                saveDataToFile();
            } else login();
        } else if (bank.getUser(ID).hasRole(ACCOUNT_OWNER)) {
            System.out.print("Account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine());
            if (login.verifyAccount(accountNumber)) {
                // Open account owner panel using ID, and account number
                AccountOwnerPanel ownerPanel = new AccountOwnerPanel(ID, scanner, accountNumber, userCreation);
                ownerPanel.start();
                saveDataToFile();
            } else login();
        }
    }

    private boolean register(User user, Account account) {
        while (true) {
            System.out.println("Provide password for your new account.");
            printCursor();
            String password = scanner.nextLine();

            if (!registration.checkPasswordLength(password)) {
                System.out.println("Password need be at least 5 characters long.");
            } else {
                System.out.println("Confirm provided password.");
                printCursor();

                if (registration.checkPasswordsEquality(password, scanner.nextLine().trim())) {
                    String ID = user.getPerson().getID();
                    registration.registerUser(ID, password);
                    System.out.println("You registration process has been successfully completed.");
                    System.out.println("accounts.Account number: " + account.getAccountNumber());
                    System.out.println("ID: " + ID);
                    return true;
                }

                System.out.println("Try again.");
            }
        }
    }

    private void printCursor() {
        System.out.print("> ");
    }

    private void addUserAndAccountToBank(User user, Account account) {
        bank.addUser(user);
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        saveDataToFile();
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

    private void printLogo() {
        System.out.println(LogoLoader.read("ascii logo.txt"));
    }

    private void loadDataFromFile() {
        CSVToUsers.read(bank, "src/main/resources/users.csv");
        CSVToAccounts.read(bank, "src/main/resources/accounts.csv");
        CSVToAccountNumber.read(bank.getAccountNumbers(), "src/main/resources/account_numbers.csv");
    }

    private void saveDataToFile() {
        UsersToCSV.write(bank.getAllUsers(), "src/main/resources/users.csv");
        AccountsToCSV.write(bank.getAllAccounts(), "src/main/resources/accounts.csv");
        AccountNumberToCSV.write(bank.getAccountNumbers(), "src/main/resources/account_numbers.csv");
    }
}

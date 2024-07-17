package user_interface;

import accounts.Account;
import bank.Bank;
import file_manipulation.FileManipulator;
import file_manipulation.LogoLoader;
import file_manipulation.TransactionHistoryCSVHandler;
import users.Admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final AccountCreation accountCreation;
    private final LoginService loginService;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.accountCreation = new AccountCreation(scanner, this);
        this.loginService = new LoginService(this, scanner);
    }

    public void initialize() {
        FileManipulator.loadDataFromFile();
        printLogo();
        System.out.println("Welcome to Bartolem's Online Banking Application.");
        start();
    }

    public void start() {
        loop: while (true) {
            printStartingMessage();
            printCursor();
            switch (scanner.nextLine()) {
                case "1":
                    // Log in to the system
                    loginService.login();
                    break;
                case "2":
                    // Create new account
                    accountCreation.register();
                    break;
                case "x", "X":
                    // Exit
                    break loop;
            }
        }

        exitApplication();
    }

    private void exitApplication() {
        scanner.close();
        System.exit(0);
    }

    protected static void printCursor() {
        System.out.print("> ");
    }

    protected static void  printBorder() {
        System.out.println("==================================================");
    }

    private void printStartingMessage() {
        System.out.println();
        System.out.println("Choose right option.");
        System.out.println("(1) Open account that is already created.");
        System.out.println("(2) Create an account.");
        System.out.println("(X) Exit.");
    }

    public static void printLoginMessage() {
        System.out.println("Enter your user ID and password, to log in. Type (X) to exit.");
    }

    protected static void printLogo() {
        System.out.println(LogoLoader.read("ascii logo.txt"));
    }
}

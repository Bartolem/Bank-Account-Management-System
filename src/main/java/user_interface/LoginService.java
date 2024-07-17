package user_interface;

import authentication.Authentication;
import bank.Bank;
import file_manipulation.FileManipulator;
import validation.Validation;

import java.util.Arrays;
import java.util.Scanner;

import static authentication.Role.ACCOUNT_OWNER;
import static authentication.Role.ADMIN;

public class LoginService {
    private final Authentication authentication;
    private final UserInterface userInterface;
    private final Scanner scanner;
    private final Bank bank;

    public LoginService(UserInterface userInterface, Scanner scanner) {
        this.authentication = Authentication.getInstance();
        this.userInterface = userInterface;
        this.scanner = scanner;
        this.bank = Bank.getInstance();
    }

    public void login() {
        UserInterface.printLoginMessage();
        System.out.print("Enter your user ID: ");
        String ID = scanner.nextLine();

        if (ID.equalsIgnoreCase("X")) {
            userInterface.start();
        }
        if (bank.getUser(ID) == null) {
            System.out.println("There is no account with provided ID.");
            login();
        }

        char[] password = System.console().readPassword("Enter your password: ");

        if (bank.getUser(ID).hasRole(ADMIN)) {
            if (authentication.authenticateUser(ID, Arrays.toString(password))) {
                // Open admin panel using only ID
                new AdminPanel(ID, scanner).start();
                FileManipulator.saveDataToFile();
            } else login();
        } else if (bank.getUser(ID).hasRole(ACCOUNT_OWNER)) {
            System.out.print("Account number: ");
            String accountNumber = scanner.nextLine();
            if (Validation.validateNumber(accountNumber) && verifyAccount(Integer.parseInt(accountNumber), ID, Arrays.toString(password))) {
                // Open account owner panel using ID, and account number
                new AccountOwnerPanel(ID, scanner, Integer.parseInt(accountNumber), new UserCreation(scanner, userInterface), userInterface).initialize();
                FileManipulator.saveDataToFile();
            } else login();
        }
    }

    private boolean verifyAccount(int accountNumber, String userID, String password) {
        if (authentication.authenticateUser(userID, password)) {
            if (Bank.getInstance().getAccount(accountNumber) != null) {
                System.out.println("Successfully logged.");
                return true;
            } else System.out.println("Not found account with number: " + accountNumber);
        } else System.out.println("Wrong ID or password");

        return false;
    }
}

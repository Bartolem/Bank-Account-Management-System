package user_interface;

import accounts.Account;
import authentication.Authentication;
import users.User;

import java.util.Arrays;

public class RegistrationService {
    private final Authentication authentication;
    public static final int MIN_ALLOWED_PASSWORD_LENGTH = 6;

    public RegistrationService() {
        this.authentication = Authentication.getInstance();
    }

    public boolean register(User user, Account account) {
        while (true) {
            System.out.println("Provide password for your new account.");
            UserInterface.printCursor();
            char[] password = System.console().readPassword("Enter your password: ");

            if (!checkPasswordLength(Arrays.toString(password))) {
                System.out.println("Password need be at least 5 characters long.");
            } else {
                System.out.println("Confirm provided password.");
                UserInterface.printCursor();

                if (checkPasswordsEquality(Arrays.toString(password), Arrays.toString(System.console().readPassword()))) {
                    String ID = user.getPerson().getID();
                    authentication.addUserCredentials(ID, Arrays.toString(password));
                    System.out.println("You registration process has been successfully completed.");
                    System.out.println("Account number: " + account.getAccountNumber());
                    System.out.println("ID: " + ID);
                    return true;
                }

                System.out.println("Try again.");
            }
        }
    }

    private boolean checkPasswordLength(String password) {
        return password.length() >= MIN_ALLOWED_PASSWORD_LENGTH;
    }

    private boolean checkPasswordsEquality(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }
}

package user_interface;

import accounts.Account;
import authentication.Authentication;
import bank.Bank;
import users.User;

public class Login {
    Authentication authentication;
    private final String userID;
    private final String password;
    private int accountNumber;

    public Login(String userID, String password) {
        this.authentication = Authentication.getInstance();
        this.userID = userID;
        this.password = password;
    }

    public Login(String userID, String password, int accountNumber) {
        this(userID, password);
        this.accountNumber = accountNumber;
    }

    public User verifyUser() {
        return authentication.authenticateUser(userID, password);
    }

    public Account verifyAccount() {
        if (verifyUser() != null) {
            if (Bank.getInstance().getAccount(accountNumber) != null) {
                System.out.println("Successfully logged.");
                return Bank.getInstance().getAccount(accountNumber);
            } else System.out.println("Not found account with number: " + accountNumber);
        } else System.out.println("Wrong ID or password");

        return null;
    }
}

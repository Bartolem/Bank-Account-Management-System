package user_interface;

import authentication.Authentication;
import bank.Bank;

public class Login {
    private final Authentication authentication;
    private final String userID;
    private final String password;

    public Login(String userID, String password) {
        this.authentication = Authentication.getInstance();
        this.userID = userID;
        this.password = password;
    }

    public boolean verifyUser() {
        return authentication.authenticateUser(userID, password);
    }

    public boolean verifyAccount(int accountNumber) {
        if (verifyUser()) {
            if (Bank.getInstance().getAccount(accountNumber) != null) {
                System.out.println("Successfully logged.");
                return true;
            } else System.out.println("Not found account with number: " + accountNumber);
        } else System.out.println("Wrong ID or password");

        return false;
    }
}

package user_interface;

import authentication.Authentication;

public class Registration {
    private final Authentication authentication;

    public Registration() {
        this.authentication = Authentication.getInstance();
    }

    public boolean checkPasswordLength(String password) {
        int minAllowedPasswordLength = 6;
        return password.length() >= minAllowedPasswordLength;
    }

    public boolean checkPasswordsEquality(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }

    public void registerUser(String ID, String password) {
        authentication.addUserCredentials(ID, password);
    }
}
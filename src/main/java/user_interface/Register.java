package user_interface;

import authentication.Authentication;

public class Register {
    private final Authentication authentication;

    public Register() {
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

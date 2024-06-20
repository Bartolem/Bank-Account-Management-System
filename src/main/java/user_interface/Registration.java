package user_interface;

import authentication.Authentication;

public class Registration {
    private final Authentication authentication;
    public static final int MIN_ALLOWED_PASSWORD_LENGTH = 6;

    public Registration() {
        this.authentication = Authentication.getInstance();
    }

    public boolean checkPasswordLength(String password) {
        return password.length() >= MIN_ALLOWED_PASSWORD_LENGTH;
    }

    public boolean checkPasswordsEquality(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }

    public void registerUser(String ID, String password) {
        authentication.addUserCredentials(ID, password);
    }
}

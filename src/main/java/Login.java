public class Login {
    Authentication authentication;
    private final String userID;
    private final String password;
    private int accountNumber;

    public Login(String userID, String password) {
        this.authentication = Authentication.getInstance();
        this.userID = userID;
        this.password = password;
        verifyUser();
    }

    public Login(String userID, String password, int accountNumber) {
        this(userID, password);
        this.accountNumber = accountNumber;
        verifyAccount();
    }

    private User verifyUser() {
        return authentication.authenticateUser(userID, password);
    }

    private void verifyAccount() {
        if (verifyUser() != null) {
            if (Bank.getInstance().getAccount(accountNumber) != null) {
                System.out.println("Successfully logged to:\n" + Bank.getInstance().getAccount(accountNumber));
            } else System.out.println("Not found account with number: " + accountNumber);
        } else System.out.println("Wrong ID or password");
    }
}

public class Login {
    Authentication authentication;
    private final String userID;
    private final String password;
    private final int accountNumber;

    public Login(String userID, String password, int accountNumber) {
        this.authentication = Authentication.getInstance();
        this.userID = userID;
        this.password = password;
        this.accountNumber = accountNumber;
        verifyAccount();
    }

    private void verifyAccount() {
        if (authentication.authenticateUser(userID, password) != null) {
            if (Bank.getInstance().getAccount(accountNumber) != null) {
                System.out.println("Successfully logged to:\n" + Bank.getInstance().getAccount(accountNumber));
            } else System.out.println("Not found account with number: " + accountNumber);
        } else System.out.println("Wrong ID or password");
    }
}

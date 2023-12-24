package user_interface;

import accounts.*;
import bank.Bank;
import users.User;

public class AccountOwnerPanel {
    private final Bank bank;
    private final User user;
    private final Account account;

    public AccountOwnerPanel(String ID, int accountNumber) {
        this.bank = Bank.getInstance();
        this.user = bank.getUser(ID);
        this.account = bank.getAccount(accountNumber);
    }

    public void start() {
        System.out.println("Welcome " + user.getPerson().getFullName());
        System.out.println("Your account: \n" + account);
    }
}

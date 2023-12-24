package user_interface;

import accounts.*;
import users.User;

import java.util.Scanner;

public class AccountOwnerPanel extends UserPanel {
    private final User user;
    private final Account account;

    public AccountOwnerPanel(String ID, Scanner scanner, int accountNumber) {
        super(ID, scanner);
        this.user = getUser();
        this.account = getBank().getAccount(accountNumber);
    }

    @Override
    public void start() {
        greetings();
        loop:while (true) {
            System.out.println("\nChoose action");
            System.out.println("(1) Deposit");
            System.out.println("(2) Withdraw");
            System.out.println("(3) Transfer");
            System.out.println("(4) Settings");
            System.out.println("(X) Log out");
            printCursor();
            String action = getScanner().nextLine();

            switch (action) {
                case "1" -> deposit();
                case "2" -> withdraw();
                case "3" -> transfer();
                case "4" -> settings();
                case "x", "X" -> {
                    break loop;
                }
            }
        }
    }

    private void deposit() {

    }

    private void withdraw() {

    }

    private void transfer() {

    }

    private void settings() {

    }

    private void greetings() {
        System.out.println("\nWelcome " + user.getPerson().getFullName());
        System.out.println("\nYour account: \n" + account);
    }
}

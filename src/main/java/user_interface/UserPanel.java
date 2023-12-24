package user_interface;

import bank.Bank;
import users.User;

import java.util.Scanner;

public abstract class UserPanel {
    private final Bank bank;
    private final User user;
    private final Scanner scanner;

    public UserPanel(String ID, Scanner scanner) {
        this.bank = Bank.getInstance();
        this.user = bank.getUser(ID);
        this.scanner = scanner;
    }

    public User getUser() {
        return user;
    }

    public Bank getBank() {
        return bank;
    }

    public Scanner getScanner() {
        return scanner;
    }

    abstract void start();

    public void printCursor() {
        System.out.print("> ");
    }
}

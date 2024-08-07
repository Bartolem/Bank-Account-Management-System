package bank;

import accounts.*;
import authentication.Role;
import currencies.CurrencyCodes;
import users.*;

import java.util.*;

public class Bank {
    private final HashMap<Integer, Account> accounts;
    private final HashMap<String, User> users;
    private static Bank bank;

    private Bank() {
        this.accounts = new HashMap<>();
        this.users = new HashMap<>();
    }

    public static Bank getInstance() {
        if (bank == null) {
            bank = new Bank();
        }
        return bank;
    }

    public void addAccount(int accountNumber, Account account, User user) {
        if (hasAdminRole(user)) {
            accounts.putIfAbsent(accountNumber, account);
        } else System.out.println("Access denied!");
    }

    public void addUser(User user) {
        users.putIfAbsent(user.getPerson().getID(), user);
    }

    public boolean hasAdminRole(User user) {
        if (user != null) {
            // Only allow access to admin user
            return user.hasRole(Role.ADMIN);
        }
        return false;
    }

    public boolean hasTransactionViewerRole(User user) {
        if (user != null) {
            // Only allow access to transaction viewer user
            return user.hasRole(Role.TRANSACTION_VIEWER);
        }
        return false;
    }

    public int size() {
        return accounts.size();
    }

    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    public boolean contains(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public User getUser(String ID) {
        return users.get(ID);
    }

    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Set<Integer> getAccountNumbers() {
        return accounts.keySet();
    }

    public void remove(int accountNumber, User user) {
        if (hasAdminRole(user)) {
            accounts.remove(accountNumber);
        } else System.out.println("Access denied!");
    }

    public void printAccounts() {
        for (Account account : accounts.values()) {
            System.out.println(account + "\n");
        }
    }

    public void printUsers() {
        for (User user : users.values()) {
            System.out.println(user + "\n");
        }
    }

    public void printCurrencies() {
        System.out.println(Arrays.toString(CurrencyCodes.values()));
    }

    public void printAccountTypes() {
        System.out.println(Arrays.toString(AccountTypes.values()));
    }

    public void printRoles() {
        System.out.println(Arrays.toString(Role.values()));
    }

    @Override
    public String toString() {
        return "Number of \nAccounts: " + accounts.size() +
                "\nUsers: " + users.size() +
                "\nSupported currencies: " + CurrencyCodes.values().length +
                "\nAccount types: " + AccountTypes.values().length +
                "\nRoles: " + Role.values().length;
    }
}

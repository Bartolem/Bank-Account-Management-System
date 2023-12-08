import java.util.ArrayList;
import java.util.HashMap;

public class Bank {
    private final HashMap<Integer, Account> accounts;
    private static Bank bank;

    private Bank() {
        this.accounts = new HashMap<>();
    }

    public static Bank getInstance() {
        if (bank == null) {
            bank = new Bank();
        }
        return bank;
    }

    public void add(int accountNumber, Account account) {
        accounts.putIfAbsent(accountNumber, account);
    }

    public int size() {
        return accounts.size();
    }

    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Integer> getAccountNumbers() {
        return new ArrayList<>(accounts.keySet());
    }

    public void remove(int accountNumber) {
        accounts.remove(accountNumber);
    }

    public void printAccounts() {
        for (Account account : accounts.values()) {
            System.out.println(account + "\n");
        }
    }

    @Override
    public String toString() {
        return accounts.toString();
    }
}

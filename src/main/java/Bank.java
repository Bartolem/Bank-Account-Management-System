import java.util.ArrayList;
import java.util.HashMap;

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

    public void addAccount(int accountNumber, Account account) {
        accounts.putIfAbsent(accountNumber, account);
    }

    public void addUser(User user) {
        users.putIfAbsent(user.getPerson().getID(), user);
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

    public User getUser(String ID) {
        return users.get(ID);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
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

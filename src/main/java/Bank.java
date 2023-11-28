import java.util.ArrayList;
import java.util.Objects;

public class Bank {
    private ArrayList<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void add(Account account) {
        accounts.add(account);
    }

    public int size() {
        return accounts.size();
    }

    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    public Account getAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getACCOUNT_NUMBER() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    public void remove(int accountNumber) {
        if (getAllAccounts() != null) {
            accounts.remove(getAccount(accountNumber));
        }
    }

    public void printAccounts() {
        for (Account account : accounts) {
            System.out.println(account + "\n");
        }
    }

    @Override
    public String toString() {
        return accounts.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank bank)) return false;
        return accounts.equals(bank.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts);
    }
}

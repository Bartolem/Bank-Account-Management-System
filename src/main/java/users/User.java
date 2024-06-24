package users;

import accounts.Account;
import authentication.*;

import java.util.ArrayList;

public class User {
    public static int MAX_NUMBER_OF_ACCOUNTS = 5;
    private final ArrayList<Account> ownedAccounts;
    private final Person person;
    private final Role role;

    public User(Person person, Role role) {
        this.ownedAccounts = new ArrayList<>();
        this.person = person;
        this.role = role;
    }

    public Person getPerson() {
        return person;
    }

    public Role getRole() {
        return role;
    }

    public boolean hasRole(Role role) {
        return getRole().equals(role);
    }

    public ArrayList<Account> getOwnedAccounts() {
        return ownedAccounts;
    }

    public int getNumberOfOwnedAccounts() {
        return ownedAccounts.size();
    }

    public void addOwnedAccount(Account account) {
        if (!ownedAccounts.contains(account) &&
                account.getUser()
                        .getPerson()
                        .getID()
                        .equals(person.getID()) &&
        getNumberOfOwnedAccounts() < MAX_NUMBER_OF_ACCOUNTS) {
            ownedAccounts.add(account);
        }
    }

    @Override
    public String toString() {
        return getRole() + "\n" + person.toString();
    }
}

import java.util.ArrayList;

public class User {
    private final Person person;
    private final ArrayList<Account> ownedAccounts;

    public User(Person person) {
        this.person = person;
        this.ownedAccounts = new ArrayList<>();
    }

    public Person getPerson() {
        return person;
    }

    public ArrayList<Account> getOwnedAccounts() {
        return ownedAccounts;
    }

    public void addOwnedAccount(Account account) {
        if (!ownedAccounts.contains(account) && account.getUser().getPerson().getID().equals(person.getID())) {
            ownedAccounts.add(account);
        }
    }

    @Override
    public String toString() {
        return person.toString();
    }
}

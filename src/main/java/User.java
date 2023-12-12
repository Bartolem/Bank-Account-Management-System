import java.util.ArrayList;

public class User {
    private final ArrayList<Account> ownedAccounts;
    private final Person person;
    private final Role role;

    public User(Person person) {
        this.ownedAccounts = new ArrayList<>();
        this.person = person;
        this.role = Role.ACCOUNT_OWNER;
    }

    protected User(Person person, Role role) {
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
        return this.role.equals(role);
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

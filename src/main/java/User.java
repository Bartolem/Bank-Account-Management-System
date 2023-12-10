import java.util.ArrayList;

public class User {
    private Person person;
    private ArrayList<Account> ownedAccounts;

    public User(Person person) {
        this.person = person;
        this.ownedAccounts = new ArrayList<>();
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return person.toString();
    }
}

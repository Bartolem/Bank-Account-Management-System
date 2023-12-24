import accounts.Account;
import accounts.SavingsAccount;
import currencies.CurrencyCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Person;
import users.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    Address address;
    Person person;
    User user;
    Account account;
    SavingsAccount savingsAccount;

    @BeforeEach
    void createUserObject() {
        address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        person = new Person("Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        user = new User(person);
        account = new Account(user, CurrencyCodes.PLN, "2000");
        savingsAccount = new SavingsAccount(user, CurrencyCodes.RUB, "10000");
    }

    @Test
    void getPerson() {
        assertEquals(person, user.getPerson());
    }

    @Test
    void getOwnedAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(savingsAccount);
        assertEquals(accounts, user.getOwnedAccounts());
    }

    @Test
    void addOwnedAccount() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(savingsAccount);
        user.addOwnedAccount(savingsAccount);
        assertEquals(accounts, user.getOwnedAccounts());
    }
}
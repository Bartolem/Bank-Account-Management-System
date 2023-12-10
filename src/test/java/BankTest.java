import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank = Bank.getInstance();
    Account account;
    SavingsAccount savingsAccount;
    CurrentAccount currentAccount;
    Address address;
    Person person;
    User user;

    void addTwoAccounts() {
        savingsAccount =  new SavingsAccount(user, "PLN", "1000");
        currentAccount = new CurrentAccount(user, "USD", "300");

        bank.addAccount(savingsAccount.getAccountNumber(), savingsAccount);
        bank.addAccount(currentAccount.getAccountNumber(), currentAccount);
    }

    @BeforeEach
    void createAccountObject() {
        address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        person = new Person("Adam", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        user = new User(person);
        account = new Account(user, "PLN", "0");
    }

    void addOneAccount() {
        bank.addAccount(account.getAccountNumber(), account);
    }

    @Test
    void addAccount() {
        addOneAccount();
        assertFalse(bank.isEmpty());
        assertEquals(5, bank.size());
    }

    @Test
    void addAFewDifferentAccount() {
        addTwoAccounts();
        assertFalse(bank.isEmpty());
        assertEquals(4, bank.size());
    }

    @Test
    void getAccount() {
        bank.addAccount(account.getAccountNumber(), account);
        assertNotNull(bank.getAccount(account.getAccountNumber()));
    }

    @Test
    void getAllAccountsIfThereIsNoAccount() {
        bank.remove(account.getAccountNumber());
        assertTrue(bank.isEmpty());
    }

    @Test
    void getAllAccountsIfThereIsAtLeastOne() {
        bank.addAccount(account.getAccountNumber(), account);
        assertFalse(bank.isEmpty());
        assertNotNull(bank.getAllAccounts());
        assertEquals(2, bank.size());
    }

    @Test
    void remove() {
        bank.remove(account.getAccountNumber());
        assertTrue(bank.isEmpty());
    }

    @Test
    void removeIfThereIsOnlyOne() {
        bank.remove(account.getAccountNumber());
        assertEquals(1, bank.size());
    }
}
import accounts.Account;
import accounts.CurrentAccount;
import accounts.SavingsAccount;
import bank.Bank;
import currencies.CurrencyCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Admin;
import users.Person;
import users.User;

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
        savingsAccount =  new SavingsAccount(user, CurrencyCodes.PLN, "1000");
        currentAccount = new CurrentAccount(user, CurrencyCodes.USD, "300");

        bank.addAccount(savingsAccount.getAccountNumber(), savingsAccount, Admin.getInstance());
        bank.addAccount(currentAccount.getAccountNumber(), currentAccount, Admin.getInstance());
    }

    @BeforeEach
    void createAccountObject() {
        address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        person = new Person("Adam", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        user = new User(person);
        account = new Account(user, CurrencyCodes.PLN, "0");
    }

    void addOneAccount() {
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
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
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        assertNotNull(bank.getAccount(account.getAccountNumber()));
    }

    @Test
    void getAllAccountsIfThereIsNoAccount() {
        bank.remove(account.getAccountNumber(), Admin.getInstance());
        assertTrue(bank.isEmpty());
    }

    @Test
    void getAllAccountsIfThereIsAtLeastOne() {
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        assertFalse(bank.isEmpty());
        assertNotNull(bank.getAllAccounts());
        assertEquals(2, bank.size());
    }

    @Test
    void remove() {
        bank.remove(account.getAccountNumber(), Admin.getInstance());
        assertTrue(bank.isEmpty());
    }

    @Test
    void removeIfThereIsOnlyOne() {
        bank.remove(account.getAccountNumber(), Admin.getInstance());
        assertEquals(1, bank.size());
    }
}
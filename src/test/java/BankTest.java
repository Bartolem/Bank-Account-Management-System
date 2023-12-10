import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    static Bank bank = Bank.getInstance();
    static Account account = new Account("Adam", "PLN", "0");

    void addTwoAccounts() {
        SavingsAccount savingsAccount =  new SavingsAccount("Jarek", "PLN", "1000");
        CurrentAccount currentAccount = new CurrentAccount("Bartolem", "USD", "300");

        bank.addAccount(savingsAccount.getAccountNumber(), savingsAccount);
        bank.addAccount(currentAccount.getAccountNumber(), currentAccount);
    }

    @BeforeAll
    static void addOneAccount() {
        bank.addAccount(account.getAccountNumber(), account);
    }

    @Test
    void addAccount() {
        bank.addAccount(account.getAccountNumber(), account);
        assertFalse(bank.isEmpty());
        assertEquals(3, bank.size());
    }

    @Test
    void addAFewDifferentAccount() {
        addTwoAccounts();
        assertFalse(bank.isEmpty());
        assertEquals(3, bank.size());
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
        assertEquals(1, bank.size());
    }

    @Test
    void remove() {
        bank.remove(account.getAccountNumber());
        assertTrue(bank.isEmpty());
    }

    @Test
    void removeIfThereIsOnlyOne() {
        bank.remove(account.getAccountNumber());
        assertTrue(bank.isEmpty());
    }
}
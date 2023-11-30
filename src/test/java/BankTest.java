import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank;
    Account account = new Account("Adam", "2000");

    void addTwoAccounts() {
        bank.add(new SavingsAccount("Jarek", "1000", "1000"));
        bank.add(new CurrentAccount("Bartolem", "300"));
    }

    @BeforeEach
    void createBankObject() {
        bank = new Bank();
        bank.add(account);
    }
    @Test
    void addOneAccount() {
        assertFalse(bank.isEmpty());
        assertEquals(1, bank.size());
    }

    @Test
    void addAFewDifferentAccount() {
        addTwoAccounts();
        assertFalse(bank.isEmpty());
        assertEquals(3, bank.size());
    }

    @Test
    void getAccount() {
        assertNotNull(bank.getAccount(account.getACCOUNT_NUMBER()));
    }

    @Test
    void getAllAccountsIfThereIsNoAccount() {
        bank.remove(account.getACCOUNT_NUMBER());
        assertTrue(bank.isEmpty());
    }

    @Test
    void getAllAccountsIfThereIsAtLeastOne() {
        assertFalse(bank.isEmpty());
        assertNotNull(bank.getAllAccounts());
        assertEquals(1, bank.size());
    }

    @Test
    void remove() {
        addTwoAccounts();
        bank.remove(account.getACCOUNT_NUMBER());
        assertFalse(bank.isEmpty());
        assertEquals(2, bank.size());
    }

    @Test
    void removeIfThereIsOnlyOneAccount() {
        bank.remove(account.getACCOUNT_NUMBER());
        assertTrue(bank.isEmpty());
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void createAccountObject() {
        account = new Account("Adam", 2000);
    }
    
    @Test
    void setOwnerName() {
        account.setOwnerName("Bartosz");
        assertEquals("Bartosz", account.getOwnerName());
    }

    @Test
    void setBalance() {
        account.setBalance(500);
        assertEquals(500, account.getBalance());
    }

    @Test
    void deposit() {
        account.deposit(100);
        assertEquals(2100, account.getBalance());
    }

    @Test
    void depositWhenAmountIsNegativeValue() {
        account.deposit(-100);
        assertEquals(2000, account.getBalance());
    }

    @Test
    void depositSuccessful() {
        assertTrue(account.deposit(100));
    }

    @Test
    void depositNotSuccessfulWhenAmountIsNegativeValue() {
        assertFalse(account.deposit(-100));
    }

    @Test
    void depositNotSuccessfulWhenAmountIs0() {
        assertFalse(account.deposit(0));
    }

    @Test
    void withdraw() {
        account.withdraw(100);
        assertEquals(1900, account.getBalance());
    }

    @Test
    void withdrawWhenAmountIsNegativeValue() {
        account.withdraw(-100);
        assertEquals(2000, account.getBalance());
    }

    @Test
    void getBalance() {
        assertEquals(2000, account.getBalance());
    }
}
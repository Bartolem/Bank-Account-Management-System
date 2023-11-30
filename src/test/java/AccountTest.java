import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void createAccountObject() {
        account = new Account("Adam", "2000");
    }

    @Test
    void setOwnerName() {
        account.setOwnerName("Bartosz");
        assertEquals("Bartosz", account.getOwnerName());
    }

    @Test
    void setBalance() {
        account.setBalance(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    void deposit() {
        account.deposit(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(2100), account.getBalance());
    }

    @Test
    void depositWhenAmountIsNegativeValue() {
        account.deposit(new BigDecimal(-100));
        assertEquals(BigDecimal.valueOf(2000), account.getBalance());
    }

    @Test
    void depositSuccessful() {
        assertTrue(account.deposit(new BigDecimal(100)));
    }

    @Test
    void depositNotSuccessfulWhenAmountIsNegativeValue() {
        assertFalse(account.deposit(new BigDecimal(-100)));
    }

    @Test
    void depositNotSuccessfulWhenAmountIs0() {
        assertFalse(account.deposit(new BigDecimal(0)));
    }

    @Test
    void withdraw() {
        account.withdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(1900), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIsNegativeValue() {
        account.withdraw(BigDecimal.valueOf(-100));
        assertEquals(BigDecimal.valueOf(2000), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIs0() {
        account.withdraw(BigDecimal.valueOf(0));
        assertEquals(BigDecimal.valueOf(2000), account.getBalance());
    }

    @Test
    void withdrawSuccessful() {
        assertTrue(account.withdraw(BigDecimal.valueOf(100)));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsNegativeValue() {
        assertFalse(account.withdraw(BigDecimal.valueOf(-100)));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIs0() {
        assertFalse(account.withdraw(BigDecimal.valueOf(0)));
    }

    @Test
    void getBalance() {
        assertEquals(BigDecimal.valueOf(2000), account.getBalance());
    }
}
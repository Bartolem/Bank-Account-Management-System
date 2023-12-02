import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void createAccountObject() {
        account = new Account("Adam", "PLN", "2000");
    }

    @Test
    void setOwnerName() {
        account.setOwnerName("Bartosz");
        assertEquals("Bartosz", account.getOwnerName());
    }

    @Test
    void setBalance() {
        account.setBalance("500");
        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }

    @Test
    void deposit() {
        account.deposit(BigDecimal.valueOf(100));
        assertEquals(new BigDecimal("2100.00"), account.getBalance());
    }

    @Test
    void depositWhenAmountIsNegativeValue() {
        account.deposit(BigDecimal.valueOf(-100));
        assertEquals(new BigDecimal("2000.00"), account.getBalance());
    }

    @Test
    void depositSuccessful() {
        assertTrue(account.deposit(BigDecimal.valueOf(100)));
    }

    @Test
    void depositNotSuccessfulWhenAmountIsNegativeValue() {
        assertFalse(account.deposit(BigDecimal.valueOf(-100)));
    }

    @Test
    void depositNotSuccessfulWhenAmountIs0() {
        assertFalse(account.deposit(BigDecimal.valueOf(0)));
    }

    @Test
    void withdraw() {
        account.withdraw(BigDecimal.valueOf(100));
        assertEquals(new BigDecimal("1900.00"), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIsNegativeValue() {
        account.withdraw(BigDecimal.valueOf(-100));
        assertEquals(new BigDecimal("2000.00"), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIs0() {
        account.withdraw(BigDecimal.valueOf(0));
        assertEquals(new BigDecimal("2000.00"), account.getBalance());
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
        assertEquals(new BigDecimal("2000.00"), account.getBalance());
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    SavingsAccount savingsAccount;

    @BeforeEach
    void createSavingsAccountObject() {
        savingsAccount = new SavingsAccount("Michał", "RU", "10000");
    }

    @Test
    void calculateInterestRate() {
        // interest rate (0.3%)
        savingsAccount.calculateInterestRate();
        assertEquals(new BigDecimal("10030.00"), savingsAccount.getBalance());
    }

    @Test
    void setMinBalance() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(5000), savingsAccount.getMinBalance());
    }

    @Test
    void setMinBalanceWhenAmountIsNegativeValue() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(-5000));
        assertEquals(new BigDecimal("0"), savingsAccount.getMinBalance());
    }


    @Test
    void setMinBalanceWhenAmountIs0() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(0));
        assertEquals(new BigDecimal("0"), savingsAccount.getMinBalance());
    }

    @Test
    void withdraw() {
        savingsAccount.withdraw(BigDecimal.valueOf(3000));
        assertEquals(new BigDecimal("7000.00"), savingsAccount.getBalance());

        savingsAccount.withdraw(BigDecimal.valueOf(6000));
        assertEquals(new BigDecimal("1000.00"), savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        savingsAccount.withdraw(BigDecimal.valueOf(16000));
        assertEquals(new BigDecimal("10000.00"), savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanMinimalBalance() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(1000));
        savingsAccount.withdraw(BigDecimal.valueOf(9500));
        assertEquals(new BigDecimal("10000.00"), savingsAccount.getBalance());
    }

    @Test
    void withdrawSuccessful() {
        assertTrue(savingsAccount.withdraw(BigDecimal.valueOf(1000)));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanBalance() {
        assertFalse(savingsAccount.withdraw(BigDecimal.valueOf(16000)));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanMinimalBalance() {
        assertFalse(savingsAccount.withdraw(BigDecimal.valueOf(50000)));
    }
}
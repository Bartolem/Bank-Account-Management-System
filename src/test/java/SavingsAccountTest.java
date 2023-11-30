import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    SavingsAccount savingsAccount;

    @BeforeEach
    void createSavingsAccountObject() {
        savingsAccount = new SavingsAccount("Michał", "10000", "1000");
    }

    @Test
    void calculateInterestRate() {
        // interest rate (0.3%)
        savingsAccount.calculateInterestRate();
        assertEquals(BigDecimal.valueOf(10030), savingsAccount.getBalance());
    }

    @Test
    void setMinBalance() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(5000), savingsAccount.getMinBalance());
    }

    @Test
    void setMinBalanceWhenAmountIsNegativeValue() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(-5000));
        assertEquals(BigDecimal.valueOf(1000), savingsAccount.getMinBalance());
    }


    @Test
    void setMinBalanceWhenAmountIs0() {
        savingsAccount.setMinBalance(BigDecimal.valueOf(0));
        assertEquals(BigDecimal.valueOf(1000), savingsAccount.getMinBalance());
    }

    @Test
    void withdraw() {
        savingsAccount.withdraw(BigDecimal.valueOf(3000));
        assertEquals(BigDecimal.valueOf(7000), savingsAccount.getBalance());

        savingsAccount.withdraw(BigDecimal.valueOf(6000));
        assertEquals(1000, savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        savingsAccount.withdraw(BigDecimal.valueOf(16000));
        assertEquals(BigDecimal.valueOf(10000), savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanMinimalBalance() {
        savingsAccount.withdraw(BigDecimal.valueOf(9500));
        assertEquals(BigDecimal.valueOf(10000), savingsAccount.getBalance());
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    SavingsAccount savingsAccount;

    @BeforeEach
    void createSavingsAccountObject() {
        savingsAccount = new SavingsAccount("Michał", 10000, 1000);
    }

    @Test
    void calculateInterestRate() {
        // interest rate (0.3%)
        savingsAccount.calculateInterestRate();
        assertEquals(10030, savingsAccount.getBalance());
    }

    @Test
    void setMinBalance() {
        savingsAccount.setMinBalance(5000);
        assertEquals(5000, savingsAccount.getMinBalance());
    }

    @Test
    void setMinBalanceWhenAmountIsNegativeValue() {
        savingsAccount.setMinBalance(-5000);
        assertEquals(1000, savingsAccount.getMinBalance());
    }


    @Test
    void setMinBalanceWhenAmountIs0() {
        savingsAccount.setMinBalance(0);
        assertEquals(1000, savingsAccount.getMinBalance());
    }

    @Test
    void withdraw() {
        savingsAccount.withdraw(3000);
        assertEquals(7000, savingsAccount.getBalance());

        savingsAccount.withdraw(6000);
        assertEquals(1000, savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        savingsAccount.withdraw(16000);
        assertEquals(10000, savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanMinimalBalance() {
        savingsAccount.withdraw(9500);
        assertEquals(10000, savingsAccount.getBalance());
    }

    @Test
    void withdrawSuccessful() {
        assertTrue(savingsAccount.withdraw(1000));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanBalance() {
        assertFalse(savingsAccount.withdraw(16000));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanMinimalBalance() {
        assertFalse(savingsAccount.withdraw(50000));
    }
}
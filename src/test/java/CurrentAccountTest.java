import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {
    CurrentAccount currentAccount;

    @BeforeEach
    void createCurrentAccountObject() {
        currentAccount = new CurrentAccount("Marek", 5000);
    }

    @Test
    void withdraw() {
        currentAccount.withdraw(1000);
        assertEquals(4000, currentAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        currentAccount.withdraw(6000);
        assertEquals(-1000, currentAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanOverdraftLimit() {
        currentAccount.withdraw(50000);
        assertEquals(5000, currentAccount.getBalance());
    }

    @Test
    void withdrawSuccessful() {
        assertTrue(currentAccount.withdraw(1000));
    }

    @Test
    void withdrawSuccessfulWhenAmountIsBiggerThanBalance() {
        assertTrue(currentAccount.withdraw(6000));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanOverdraftLimit() {
        assertFalse( currentAccount.withdraw(50000));
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {
    CurrentAccount currentAccount;
    Address address;
    Person person;
    User user;

    @BeforeEach
    void createCurrentAccountObject() {
        address = new Address("Kawowa 72", "Warsaw", "Poland", "27-856");
        person = new Person("Marek", "Lipa", "1986-04-26", address, "mila@inrt.pl", "906 656 567");
        user = new User(person);
        currentAccount = new CurrentAccount(user, "PLN", "5000");
    }

    @Test
    void withdraw() {
        currentAccount.withdraw(BigDecimal.valueOf(1000));
        assertEquals(new BigDecimal("4000.00"), currentAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        currentAccount.withdraw(BigDecimal.valueOf(6000));
        assertEquals(new BigDecimal("-1000.00"), currentAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanOverdraftLimit() {
        currentAccount.withdraw(BigDecimal.valueOf(50000));
        assertEquals(new BigDecimal("5000.00"), currentAccount.getBalance());
    }

    @Test
    void withdrawSuccessful() {
        assertTrue(currentAccount.withdraw(BigDecimal.valueOf(1000)));
    }

    @Test
    void withdrawSuccessfulWhenAmountIsBiggerThanBalance() {
        assertTrue(currentAccount.withdraw(BigDecimal.valueOf(6000)));
    }

    @Test
    void withdrawNotSuccessfulWhenAmountIsBiggerThanOverdraftLimit() {
        assertFalse(currentAccount.withdraw(BigDecimal.valueOf(50000)));
    }
}
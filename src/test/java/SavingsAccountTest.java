import accounts.SavingsAccount;
import authentication.Role;
import currencies.CurrencyCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Person;
import users.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    BigDecimal defaultMinBalance = new BigDecimal("1000");
    BigDecimal defaultInterestRate = new BigDecimal("4.0");
    SavingsAccount savingsAccount;
    User user;
    Address address;
    Person person;

    @BeforeEach
    void createSavingsAccountObject() {
        address = new Address("Kawowa 72", "Warsaw", "Poland", "27-856");
        person = new Person("Michał", "Lipa", "1986-04-26", address, "mila@inrt.pl", "906 656 567");
        user = new User(person, Role.ACCOUNT_OWNER);
        savingsAccount = new SavingsAccount(user, CurrencyCodes.RUB, "10000");
    }

    @Test
    void calculateInterestRate() {
        savingsAccount.calculateInterestRate();
        assertEquals(new BigDecimal("10400.00"), savingsAccount.getBalance());
    }

    @Test
    void setMinBalance() {
        SavingsAccount.setMinBalance(BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(5000), SavingsAccount.getMinBalance());
    }

    @Test
    void setMinBalanceWhenAmountIsNegativeValue() {
        SavingsAccount.setMinBalance(BigDecimal.valueOf(-5000));
        assertEquals(defaultMinBalance, SavingsAccount.getMinBalance());
    }

    @Test
    void setMinBalanceWhenAmountIs0() {
        SavingsAccount.setMinBalance(BigDecimal.valueOf(0));
        assertEquals(BigDecimal.valueOf(0), SavingsAccount.getMinBalance());
    }

    @Test
    void withdraw() {
        savingsAccount.withdraw(BigDecimal.valueOf(3000));
        assertEquals(new BigDecimal("7000.00"), savingsAccount.getBalance());
    }

    @Test
    void withdrawUnsuccessfulBecauseOfDailyLimit() {
        assertFalse(savingsAccount.withdraw(BigDecimal.valueOf(6000)));
    }

    @Test
    void withdrawWhenAmountIsBiggerThanBalance() {
        savingsAccount.withdraw(BigDecimal.valueOf(16000));
        assertEquals(new BigDecimal("10000.00"), savingsAccount.getBalance());
    }

    @Test
    void withdrawWhenAmountIsBiggerThanMinimalBalance() {
        SavingsAccount.setMinBalance(BigDecimal.valueOf(1000));
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
import accounts.StandardAccount;
import authentication.Role;
import currencies.CurrencyCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Person;
import users.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    StandardAccount account;
    User user;
    Address address;
    Person person;

    @BeforeEach
    void createAccountObject() {
        address = new Address("Kawowa 72", "Warsaw", "Poland", "27-856");
        person = new Person("Michał", "Lipa", "1986-04-24", address, "mila@inrt.pl", "906 656 567");
        user = new User(person, Role.ACCOUNT_OWNER);
        account = new StandardAccount(user, CurrencyCodes.PLN, "20000");
    }

    @Test
    void setOwnerFirstName() {
        account.setOwnerFirstName("Bartosz");
        assertEquals("Bartosz", account.getOwnerFirstName());
    }

    @Test
    void setOwnerLastName() {
        account.setOwnerLastName("Rokla");
        assertEquals("Rokla", account.getOwnerLastName());
    }

    @Test
    void setBalance() {
        account.setBalance("500");
        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }

    @Test
    void deposit() {
        account.deposit(BigDecimal.valueOf(1000));
        assertEquals(new BigDecimal("21000.00"), account.getBalance());
    }

    @Test
    void depositWhenAmountIsNegativeValue() {
        account.deposit(BigDecimal.valueOf(-100));
        assertEquals(new BigDecimal("20000.00"), account.getBalance());
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
        assertEquals(new BigDecimal("19900.00"), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIsNegativeValue() {
        account.withdraw(BigDecimal.valueOf(-100));
        assertEquals(new BigDecimal("20000.00"), account.getBalance());
    }

    @Test
    void withdrawWhenAmountIs0() {
        account.withdraw(BigDecimal.valueOf(0));
        assertEquals(new BigDecimal("20000.00"), account.getBalance());
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
    void withdrawUnsuccessfulBecauseOfDailyLimit() {
        assertFalse(account.withdraw(new BigDecimal("10000")));
    }

    @Test
    void getBalance() {
        assertEquals(new BigDecimal("20000.00"), account.getBalance());
    }
}
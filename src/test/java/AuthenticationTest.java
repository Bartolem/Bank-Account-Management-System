import authentication.Authentication;
import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Person;
import users.User;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {
    Authentication authentication;
    String ID;
    String password;

    @BeforeEach
    void initialize() {
        authentication = Authentication.getInstance();
        ID = "testID";
        password = "testPassword91";
    }
    @Test
    void addUserCredentials() {
        authentication.addUserCredentials(ID, password);
        assertNotNull(authentication.getUserCredentials().get(ID));
        assertTrue(authentication.getUserCredentials().containsKey(ID));
    }

    @Test
    void authenticateUser() {
        Bank bank = Bank.getInstance();
        Address address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        Person person = new Person(ID,"Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        User user = new User(person);

        bank.addUser(user);

        authentication.addUserCredentials(ID, password);

        User authenticateUser = authentication.authenticateUser(ID, password);

        assertNotNull(authenticateUser);
        assertEquals(bank.getUser(ID), authenticateUser);
    }
}
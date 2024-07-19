import authentication.Authentication;
import authentication.Role;
import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import users.Address;
import users.Person;
import users.User;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {
    Authentication authentication;
    String ID;
    String password;
    File file;

    @TempDir
    Path tempDir;

    @BeforeEach
    void initialize() {
        Authentication.resetInstance(); // Reset singleton instance
        file = new File(tempDir.toFile(), "credentials.csv");
        authentication = Authentication.getInstance(file.getAbsolutePath());
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
        Person person = new Person(ID, "Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        User user = new User(person, Role.ACCOUNT_OWNER);

        bank.addUser(user);

        authentication.addUserCredentials(ID, password);

        boolean authenticateUser = authentication.authenticateUser(ID, password);

        assertTrue(authenticateUser);
    }

    @Test
    void saveAndLoadUserCredentials() {
        authentication.addUserCredentials(ID, password);

        // Create a new instance to simulate loading from file
        Authentication newAuthInstance = Authentication.getInstance(file.getAbsolutePath());
        assertNotNull(newAuthInstance.getUserCredentials().get(ID));
        assertEquals(authentication.getUserCredentials().get(ID), newAuthInstance.getUserCredentials().get(ID));
    }

    @Test
    void authenticateUserWithIncorrectPassword() {
        Bank bank = Bank.getInstance();
        Address address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        Person person = new Person(ID, "Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        User user = new User(person, Role.ACCOUNT_OWNER);

        bank.addUser(user);

        authentication.addUserCredentials(ID, password);

        boolean authenticateUser = authentication.authenticateUser(ID, "wrongPassword");

        assertFalse(authenticateUser);
    }

    @Test
    void authenticateNonExistentUser() {
        boolean authenticateUser = authentication.authenticateUser("nonExistentID", password);
        assertFalse(authenticateUser);
    }
}

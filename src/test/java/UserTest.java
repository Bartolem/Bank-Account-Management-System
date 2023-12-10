import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    Address address;
    Person person;
    User user;

    @BeforeEach
    void createUserObject() {
        address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        person = new Person("Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
        user = new User(person);
    }

    @Test
    void getPerson() {
        assertEquals(person, user.getPerson());
    }
}
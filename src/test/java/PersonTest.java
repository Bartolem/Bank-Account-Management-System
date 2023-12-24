import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;
import users.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    Address address;
    Person person;

    @BeforeEach
    void createPersonObject() {
        address = new Address("trollop 32", "Lisbon", "Spain", "21-556");
        person = new Person("Lorola", "Eropla", "1956-10-04", address, "eolpor@inrt.ole", "5506 656 567");
    }

    @Test
    void getID() {
        assertNotNull(person.getID());
    }

    @Test
    void getFirstName() {
        assertEquals("Lorola", person.getFirstName());
    }

    @Test
    void setFirstName() {
        person.setFirstName("Elinor");
        assertEquals("Elinor", person.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Eropla", person.getLastName());
    }

    @Test
    void setLastName() {
        person.setLastName("Piwo");
        assertEquals("Piwo", person.getLastName());
    }

    @Test
    void getFullName() {
        assertEquals("Lorola Eropla", person.getFullName());
    }

    @Test
    void getDateOfBirth() {
        assertEquals("1956-10-04", person.getDateOfBirth());
    }

    @Test
    void getFormattedDateOfBirth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        LocalDate date = LocalDate.parse(person.getDateOfBirth());
        assertEquals(date.format(formatter), person.getFormattedDateOfBirth());
    }

    @Test
    void getAddress() {
        assertEquals(address, person.getAddress());
    }

    @Test
    void setAddress() {
        person.getAddress().setCity("Madrid");
        assertEquals("Madrid", person.getAddress().getCity());

        person.getAddress().setStreetAddress("Aplo 4");
        assertEquals("Aplo 4", person.getAddress().getStreetAddress());

        person.getAddress().setCountry("England");
        assertEquals("England", person.getAddress().getCountry());

        person.getAddress().setZipCode("32-112");
        assertEquals("32-112", person.getAddress().getZipCode());
    }

    @Test
    void getEmail() {
        assertEquals("eolpor@inrt.ole", person.getEmail());
    }

    @Test
    void setEmail() {
        person.setEmail("milo12@com");
        assertEquals("milo12@com", person.getEmail());
    }

    @Test
    void getPhone() {
        assertEquals("5506 656 567", person.getPhone());
    }

    @Test
    void setPhone() {
        person.setPhone("557 776 990");
        assertEquals("557 776 990", person.getPhone());
    }
}
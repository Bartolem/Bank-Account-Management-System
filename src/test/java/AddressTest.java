import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Address;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    Address address;

    @BeforeEach
    void createAddressObject() {
        address = new Address("Kawowa 72", "Warsaw", "Poland", "27-856");
    }

    @Test
    void getStreetAddress() {
        assertEquals("Kawowa 72", address.getStreetAddress());
    }

    @Test
    void setStreetAddress() {
        address.setStreetAddress("Akacjowa 12");
        assertEquals("Akacjowa 12", address.getStreetAddress());
    }

    @Test
    void getCity() {
        assertEquals("Warsaw", address.getCity());
    }

    @Test
    void setCity() {
        address.setCity("Cracow");
        assertEquals("Cracow", address.getCity());
    }

    @Test
    void getCountry() {
        assertEquals("Poland", address.getCountry());
    }

    @Test
    void setCountry() {
        address.setCountry("Norway");
        assertEquals("Norway", address.getCountry());
    }

    @Test
    void getZipCode() {
        assertEquals("27-856", address.getZipCode());
    }

    @Test
    void setZipCode() {
        address.setZipCode("21-228");
        assertEquals("21-228", address.getZipCode());
    }
}
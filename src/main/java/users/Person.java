package users;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Person {
    private String ID;
    private String firstName;
    private String lastName;
    private final LocalDate dateOfBirth;
    private Address address;
    private String email;
    private String phone;

    public Person(String firstName, String lastName, String dateOfBirth, Address address, String email, String phone) {
        this.ID = generateUniqueId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public Person(String ID, String firstName, String lastName, String dateOfBirth, Address address, String email, String phone) {
        this(firstName, lastName, dateOfBirth, address, email, phone);
        this.ID = ID;
    }

    private String generateUniqueId() {
        return String.valueOf(UUID.randomUUID());
    }

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    public String getFormattedDateOfBirth() {
        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ID: " + getID() +
                "\nFirst name: " + firstName +
                "\nLast name: " + lastName +
                "\nDay of birth: " + getFormattedDateOfBirth() +
                "\nE-mail: " + email +
                "\nPhone number: " + phone;
    }
}

package file_manipulation;

import bank.Bank;
import logging.LoggerConfig;
import users.Address;
import users.Admin;
import users.Person;
import users.User;

import java.io.*;
import java.util.Collection;
import java.util.logging.Logger;

import static authentication.Role.ACCOUNT_OWNER;
import static authentication.Role.ADMIN;

public class UsersCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(Collection<User> users, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("ID,First name,Last name,Date of birth,Street Address,City,Country,Zip code,E-mail,Phone number\n");

            // Write user details
            for (User user : users) {
                Person person = user.getPerson();
                Address address = person.getAddress();
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        person.getID(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getDateOfBirth(),
                        address.getStreetAddress(),
                        address.getCity(),
                        address.getCountry(),
                        address.getZipCode(),
                        person.getEmail(),
                        person.getPhone());

                writer.write(line);
            }

            writer.close();
            LOGGER.finest("Users successfully saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save users to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    public static void read(Bank bank, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                if (line.contains("ID")) {
                    continue;
                }
                // Read user detail
                String[] fileContent = line.split(",");
                String ID = fileContent[0];
                Person person = getPerson(fileContent, ID);

                if (ID.equals(Admin.getInstance().getPerson().getID())) {
                    bank.addUser(new User(person, ADMIN));
                }

                bank.addUser(new User(person, ACCOUNT_OWNER));
            }
            LOGGER.finest("Users successfully loaded from " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load users from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private static Person getPerson(String[] fileContent, String ID) {
        String firstName = fileContent[1];
        String lastName = fileContent[2];
        String dateOfBirth = fileContent[3];
        String street = fileContent[4];
        String city = fileContent[5];
        String country = fileContent[6];
        String zipCode = fileContent[7];
        String email = fileContent[8];
        String phone = fileContent[9].replaceAll("[^0-9]", "");

        // Create users
        Address address = new Address(street, city, country, zipCode);
        return new Person(ID, firstName, lastName, dateOfBirth, address, email, phone);
    }
}

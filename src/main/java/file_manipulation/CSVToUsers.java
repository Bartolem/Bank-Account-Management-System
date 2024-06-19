package file_manipulation;

import bank.Bank;
import users.Address;
import users.Admin;
import users.Person;
import users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static authentication.Role.*;

public class CSVToUsers {
    public static void read(Bank bank, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new InputStreamReader(Objects.requireNonNull(CSVToUsers.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                if (line.contains("ID")) {
                    continue;
                }
                // Read user detail
                String[] fileContent = line.split(",");
                String ID = fileContent[0];
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
                Person person = new Person(ID, firstName, lastName, dateOfBirth, address, email, phone);

                if (ID.equals(Admin.getInstance().getPerson().getID())) {
                    bank.addUser(new User(person, ADMIN));
                }

                bank.addUser(new User(person, ACCOUNT_OWNER));
            }
            System.out.println("Users successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

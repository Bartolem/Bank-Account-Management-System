import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVToUsers {
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
                String firstName = fileContent[1];
                String lastName = fileContent[2];
                String dateOfBirth = fileContent[3];
                String street = fileContent[4];
                String city = fileContent[5];
                String country = fileContent[6];
                String zipCode = fileContent[7];
                String email = fileContent[8];
                String phone = fileContent[9];

                // Create users
                Address address = new Address(street, city, country, zipCode);
                Person person = new Person(ID, firstName, lastName, dateOfBirth, address, email, phone);
                bank.addUser(new User(person));
            }
            System.out.println("Users successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

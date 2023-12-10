import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UsersToCSV {
    public static void write(ArrayList<User> users, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
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
            System.out.println("Users successfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

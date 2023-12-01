import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AccountsToCSV {
    public static void write(ArrayList<Account> accounts, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write headers
            writer.write("Account type,Account number,Balance,Owner name,Creation date\n");

            // Write account details
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s\n",
                        account.getType(),
                        account.getACCOUNT_NUMBER(),
                        account.getBalance(),
                        account.getOwnerName(),
                        account.getDATE());

                writer.write(line);
            }

            System.out.println("Accounts saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

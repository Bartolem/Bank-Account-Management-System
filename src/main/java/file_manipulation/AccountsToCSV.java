package file_manipulation;

import accounts.Account;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AccountsToCSV {
    public static void write(ArrayList<Account> accounts, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write headers
            writer.write("accounts.Account type,accounts.Account number,Owner ID,Owner personal name,Currency code,Balance,Creation date\n");

            // Write account details
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s,%s,%s\n",
                        account.getType(),
                        account.getAccountNumber(),
                        account.getUser().getPerson().getID(),
                        account.getOwnerName(),
                        account.getCurrencyCode(),
                        account.getBalance(),
                        account.getDate());

                writer.write(line);
            }
            writer.close();
            System.out.println("Accounts successfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

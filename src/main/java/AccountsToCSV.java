import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AccountsToCSV {
    public static void write(ArrayList<Account> accounts, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write headers
            writer.write("Account type,Account number,Owner name,Currency code,Balance,Creation date\n");

            // Write account details
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s,%s\n",
                        account.getType(),
                        account.getAccountNumber(),
                        account.getOwnerName(),
                        account.getCurrencyCode(),
                        account.getBalance(),
                        account.getDate());

                writer.write(line);

                // Save account numbers in accounts array list from AccountNumber class
//                AccountNumber.getAccountNumbers().add(account.getAccountNumber());
            }
            // Save account numbers in account_numbers.csv file
//            AccountNumberToCSV.write(AccountNumber.getAccountNumbers(), "account_numbers.csv");
            writer.close();
            System.out.println("Accounts successfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

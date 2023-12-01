import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVToAccounts {
    public static void read(Bank bank, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                if (line.contains("Account type")) {
                    continue;
                }
                // Read account detail
                String[] fileContent = line.split(",");
                String accountType = fileContent[0];
                int accountNumber = Integer.parseInt(fileContent[1]);
                String ownerName = fileContent[2];
                String balance = fileContent[3];
                String date = fileContent[4];

                // Create accounts based on types
                switch (accountType) {
                    case "Standard" -> bank.add(new Account(accountNumber, ownerName, balance, date));
                    case "Current" -> bank.add(new CurrentAccount(accountNumber, ownerName, balance, date));
                    case "Savings" -> bank.add(new SavingsAccount(accountNumber, ownerName, balance, date));
                }

                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

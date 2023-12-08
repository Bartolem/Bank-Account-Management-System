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
                String currencyCode = fileContent[3];
                String balance = fileContent[4];
                String date = fileContent[5];

                // Create accounts based on type
                switch (accountType) {
                    case "Standard" -> bank.add(accountNumber, new Account(accountNumber, ownerName, currencyCode, balance, date));
                    case "Current" -> bank.add(accountNumber, new CurrentAccount(accountNumber, ownerName, currencyCode, balance, date));
                    case "Savings" -> bank.add(accountNumber, new SavingsAccount(accountNumber, ownerName, currencyCode, balance, date));
                }
            }
            System.out.println("Accounts successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

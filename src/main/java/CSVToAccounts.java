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
                String ownerID = fileContent[2];
                CurrencyCodes currencyCode = CurrencyCodes.valueOf(fileContent[4]);
                String balance = fileContent[5];
                String date = fileContent[6];

                // Create accounts based on type
                switch (accountType) {
                    case "STANDARD" -> bank.addAccount(accountNumber, new Account(accountNumber, bank.getUser(ownerID), currencyCode, balance, date), Admin.getInstance());
                    case "CURRENT" -> bank.addAccount(accountNumber, new CurrentAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date), Admin.getInstance());
                    case "SAVINGS" -> bank.addAccount(accountNumber, new SavingsAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date), Admin.getInstance());
                }
            }
            System.out.println("Accounts successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

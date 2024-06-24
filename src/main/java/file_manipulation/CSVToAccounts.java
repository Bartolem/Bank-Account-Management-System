package file_manipulation;

import accounts.*;
import bank.Bank;
import currencies.CurrencyCodes;
import users.Admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class CSVToAccounts {
    public static void read(Bank bank, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                //Ignores the headers
                if (line.contains("Account type")) {
                    continue;
                }
                // Read account detail
                String[] fileContent = line.split(",");
                AccountTypes accountType = AccountTypes.valueOf(fileContent[0].toUpperCase());
                int accountNumber = Integer.parseInt(fileContent[1]);
                String status = fileContent[2].toUpperCase();
                String ownerID = fileContent[3];
                CurrencyCodes currencyCode = CurrencyCodes.valueOf(fileContent[5]);
                String balance = fileContent[6];
                String date = fileContent[7];
                String dailyLimit = fileContent[8];
                String monthlyLimit = fileContent[9];
                String dailyUsage = fileContent[10];
                String monthlyUsage = fileContent[11];
                boolean blocked = status.equals(AccountStatus.BLOCKED.toString());

                // Create accounts based on type
                switch (accountType) {
                    case STANDARD -> bank.addAccount(accountNumber, new StandardAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status, dailyLimit, monthlyLimit, dailyUsage, monthlyUsage), Admin.getInstance());
                    case CURRENT -> bank.addAccount(accountNumber, new CurrentAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status, dailyLimit, monthlyLimit, dailyUsage, monthlyUsage), Admin.getInstance());
                    case SAVINGS -> bank.addAccount(accountNumber, new SavingsAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status, dailyLimit, monthlyLimit, dailyUsage, monthlyUsage), Admin.getInstance());
                }
            }
            System.out.println("Accounts successfully loaded from " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

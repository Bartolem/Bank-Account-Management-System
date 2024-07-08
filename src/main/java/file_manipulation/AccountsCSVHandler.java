package file_manipulation;

import accounts.*;
import bank.Bank;
import currencies.CurrencyCodes;
import users.Admin;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AccountsCSVHandler {
    private static final Logger LOGGER = Logger.getLogger(AccountNumberCSVHandler.class.getName());

    public static void write(ArrayList<Account> accounts, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("Account type,Account number,Status,Owner ID,Owner personal name,Currency code,Balance,Creation date,Daily limit,Monthly limit,Daily usage,Monthly usage\n");

            // Write account details
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        account.getType(),
                        account.getAccountNumber(),
                        account.getStatus(),
                        account.getUser().getPerson().getID(),
                        account.getOwnerName(),
                        account.getCurrencyCode(),
                        account.getBalance(),
                        account.getCreationDate(),
                        account.getDailyLimit(),
                        account.getMonthlyLimit(),
                        account.getDailyUsage(LocalDate.now()),
                        account.getMonthlyUsage(YearMonth.now()));

                writer.write(line);
            }
            writer.close();
            LOGGER.info("Accounts successfully saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save accounts to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

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
            LOGGER.info("Accounts successfully loaded from " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load account from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

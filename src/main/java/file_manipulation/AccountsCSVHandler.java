package file_manipulation;

import accounts.*;
import bank.Bank;
import currencies.CurrencyCodes;
import logging.LoggerConfig;
import users.Admin;

import java.io.*;
import java.util.Collection;
import java.util.logging.Logger;

public class AccountsCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(Collection<Account> accounts, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("Account type,Account number,Status,Owner ID,Owner personal name,Currency code,Balance,Creation date\n");

            // Write account details
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s,%s,%s,%s\n",
                        account.getType(),
                        account.getAccountNumber(),
                        account.getStatus(),
                        account.getUser().getPerson().getID(),
                        account.getOwnerName(),
                        account.getCurrencyCode(),
                        account.getBalance(),
                        account.getCreationDate());
                writer.write(line);
            }
            writer.close();
            LOGGER.finest("Accounts successfully saved to " + fileName);
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
                boolean blocked = status.equals(AccountStatus.BLOCKED.toString());

                // Create accounts based on type
                switch (accountType) {
                    case STANDARD -> bank.addAccount(accountNumber, new StandardAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status), Admin.getInstance());
                    case CURRENT -> bank.addAccount(accountNumber, new CurrentAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status), Admin.getInstance());
                    case SAVINGS -> bank.addAccount(accountNumber, new SavingsAccount(accountNumber, bank.getUser(ownerID), currencyCode, balance, date, blocked, status), Admin.getInstance());
                }
            }
            LOGGER.finest("Accounts successfully loaded from " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load account from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

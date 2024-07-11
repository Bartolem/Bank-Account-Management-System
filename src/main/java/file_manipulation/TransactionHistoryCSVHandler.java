package file_manipulation;

import bank.Bank;
import currencies.CurrencyCodes;
import logging.LoggerConfig;
import transaction.Transaction;
import transaction.TransactionTypes;
import user_interface.UserInterface;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class TransactionHistoryCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(List<Transaction> transactions, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("Account number,Type,Date,Amount,Currency\n");

            // Write transaction details
            for (Transaction transaction : transactions) {
                String line = String.format("%d,%s,%s,%s,%s\n",
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        transaction.getFormattedDate(),
                        transaction.getAmount(),
                        transaction.getCurrencyCode());

                writer.write(line);
            }
            writer.close();
            LOGGER.finest("Transactions successfully saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save transactions to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void read(String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Type")) {
                    continue;
                }
                // Read transaction details
                String[] fileContent = line.split(",");
                int accountNumber = Integer.parseInt(fileContent[0]);
                TransactionTypes type = TransactionTypes.valueOf(fileContent[1]);
                LocalDateTime date = LocalDateTime.parse(fileContent[2], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                BigDecimal amount = new BigDecimal(fileContent[3]);
                CurrencyCodes currency = CurrencyCodes.valueOf(fileContent[4]);
                Bank.getInstance().getAccount(accountNumber).getTransactionManager().addTransaction(new Transaction(accountNumber, type, date, amount, currency));
            }
            LOGGER.finest("Transactions successfully loaded from  " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load transactions from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

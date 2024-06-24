package file_manipulation;

import bank.Bank;
import currencies.CurrencyCodes;
import transaction.Transaction;
import transaction.TransactionTypes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVToTransactionHistory {
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
                Bank.getInstance().getAccount(accountNumber).addTransaction(new Transaction(accountNumber, type, date, amount, currency));
            }
            System.out.println("Transaction history successfully loaded from " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

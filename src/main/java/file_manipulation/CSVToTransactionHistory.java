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
import java.util.List;

public class CSVToTransactionHistory {
    public static void read(List<Transaction> transaction, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                if (line.contains("Type")) {
                    continue;
                }
                // Read transaction details
                String[] fileContent = line.split(",");
                TransactionTypes type = TransactionTypes.valueOf(fileContent[0]);
                LocalDateTime date = LocalDateTime.parse(fileContent[1], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                BigDecimal amount = new BigDecimal(fileContent[2]);
                CurrencyCodes currency = CurrencyCodes.valueOf(fileContent[3]);
                Bank.getInstance().getAccount(Integer.parseInt(fileName.substring(20)))
                        .addTransaction(new Transaction(type, date, amount, currency));
            }
            System.out.println("Account numbers successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

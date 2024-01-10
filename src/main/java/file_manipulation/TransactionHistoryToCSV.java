package file_manipulation;

import accounts.Account;
import transaction.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TransactionHistoryToCSV {
    public static void write(List<Transaction> transactions, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write headers
            writer.write("Type,Date,Amount,Currency\n");

            // Write transaction details
            for (Transaction transaction : transactions) {
                String line = String.format("%s,%s,%s,%s\n",
                        transaction.getType(),
                        transaction.getDate(),
                        transaction.getAmount(),
                        transaction.getCurrencyCode());

                writer.write(line);
            }
            writer.close();
            System.out.println("Transactions successfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

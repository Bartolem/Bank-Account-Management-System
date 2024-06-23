package file_manipulation;

import transaction.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TransactionHistoryToCSV {
    public static void write(List<Transaction> transactions, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("Account number,Type,Date,Amount,Currency\n");

            // Write transaction details
            for (Transaction transaction : transactions) {
                String line = String.format("%d,%s,%s,%s,%s\n",
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        transaction.getDate(),
                        transaction.getAmount(),
                        transaction.getCurrencyCode());

                writer.write(line);
            }
            writer.close();
            System.out.println("Transactions successfully saved to " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

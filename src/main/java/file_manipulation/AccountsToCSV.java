package file_manipulation;

import accounts.Account;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class AccountsToCSV {
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
            System.out.println("Accounts successfully saved to " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

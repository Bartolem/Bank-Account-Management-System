package file_manipulation;

import accounts.Account;
import accounts.LimitManager;
import accounts.TransactionLimitDateRange;
import logging.LoggerConfig;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.logging.Logger;

public class TransactionLimitsCSVHandler {
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public static void write(Account account, String fileName, TransactionLimitDateRange transactionLimit) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write headers
            writer.write("Date,Limit amount,Usage amount\n");
            LocalDate date = LocalDate.now();
            BigDecimal limitAmount = BigDecimal.ZERO;
            BigDecimal usageAmount = BigDecimal.ZERO;

            switch (transactionLimit) {
                case DAILY -> {
                    limitAmount = account.getDailyLimit();
                    usageAmount = account.getDailyUsage(date);
                }
                case MONTHLY -> {
                    limitAmount = account.getMonthlyLimit();
                    usageAmount = account.getMonthlyUsage(YearMonth.from(date));
                }
            }

            String line = String.format("%s,%s,%s\n",
                    date,
                    limitAmount,
                    usageAmount);
            writer.write(line);
            writer.close();

            LOGGER.finest("Transaction limits successfully saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save transaction limits to " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void read(Account account, String fileName, TransactionLimitDateRange transactionLimit) {
        try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
            LimitManager limitManager = account.getLimitManager();
            String line = "";

            while ((line = reader.readLine()) != null) {
                //Ignores the headers
                if (line.contains("Date")) {
                    continue;
                }
                String[] fileContent = line.split(",");

                switch (transactionLimit) {
                    case DAILY -> {
                        limitManager.updateDailyUsage(LocalDate.parse(fileContent[0]), new BigDecimal(fileContent[2]));
                        limitManager.setDailyLimit(new BigDecimal(fileContent[1]));
                    }
                    case MONTHLY -> {
                        limitManager.updateMonthlyUsage(YearMonth.from(LocalDate.parse(fileContent[0])), new BigDecimal(fileContent[2]));
                        limitManager.setMonthlyLimit(new BigDecimal(fileContent[1]));
                    }
                }
            }
            LOGGER.finest("Transaction limits successfully loaded from " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to load transaction limits from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

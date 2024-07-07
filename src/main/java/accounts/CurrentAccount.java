package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class CurrentAccount extends Account {
    private static BigDecimal overdraftLimit = new BigDecimal(500);
    public static final AccountTypes TYPE = AccountTypes.CURRENT;

    public CurrentAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
    }

    public CurrentAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status, String dailyLimit, String monthlyLimit, String dailyUsage, String monthlyUsage) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status, dailyLimit, monthlyLimit, dailyUsage, monthlyUsage);
    }

    @Override
    public AccountTypes getType() {
        return TYPE;
    }

    public static BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public static void setOverdraftLimit(BigDecimal overdraftLimit) {
        if (overdraftLimit.compareTo(BigDecimal.valueOf(300)) >= 0
                && overdraftLimit.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            CurrentAccount.overdraftLimit = overdraftLimit;
        }
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);

        if (amount.compareTo(availableBalance) < 1) {
            if (checkDailyLimit(amount)) {
                System.out.println("Daily limit exceeded.");
                return false;
            }
            if (checkMonthlyLimit(amount)) {
                System.out.println("Monthly limit exceeded.");
                return false;
            }

            LocalDate today = LocalDate.now();

            updateDailyUsage(today, amount);
            updateMonthlyUsage(YearMonth.from(today), amount);
            setBalance(getBalance().subtract(amount).toString());
            super.getTransactionHistory().add(new Transaction(getAccountNumber(), TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, getCurrencyCode()));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(getAccountNumber())) {
                TransactionHistoryToCSV.write(getTransactionHistory(), new File("transactions/transaction_history_" + getAccountNumber() + ".csv").getAbsolutePath());
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + TYPE + ")" + " (" + getStatus() + ")" +
                "\nOverdraft limit: " + getOverdraftLimit();
    }
}

package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import file_manipulation.TransactionHistoryCSVHandler;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class SavingsAccount extends Account {
    private static BigDecimal interestRate = new BigDecimal("4.0");
    private static BigDecimal minBalance = new BigDecimal("1000");
    public static final AccountTypes TYPE = AccountTypes.SAVINGS;

    public SavingsAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
    }

    public SavingsAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status, String dailyLimit, String monthlyLimit, String dailyUsage, String monthlyUsage) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status, dailyLimit, monthlyLimit, dailyUsage, monthlyUsage);
    }

    @Override
    public AccountTypes getType() {
        return TYPE;
    }

    public void calculateInterestRate() {
        setBalance(getBalance().add(getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)).toString());
    }

    public static BigDecimal getInterestRate() {
        return interestRate;
    }

    public static void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(BigDecimal.valueOf(0.3)) >= 0
                && interestRate.compareTo(BigDecimal.valueOf(8)) <= 0) {
            SavingsAccount.interestRate = interestRate;
        }
    }

    public static BigDecimal getMinBalance() {
        return minBalance;
    }

    public static void setMinBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) >= 0
                && amount.compareTo(BigDecimal.valueOf(25000)) <= 0) {
            SavingsAccount.minBalance = amount;
        }
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if ((getBalance().subtract(amount).compareTo(SavingsAccount.minBalance) >= 0)) {
            if (getLimitManager().checkDailyLimit(amount)) {
                System.out.println("Daily limit exceeded");
                return false;
            }
            if (getLimitManager().checkMonthlyLimit(amount)) {
                System.out.println("Monthly limit exceeded");
                return false;
            }

            LocalDate today = LocalDate.now();

            getLimitManager().updateDailyUsage(today, amount);
            getLimitManager().updateMonthlyUsage(YearMonth.from(today), amount);
            setBalance(getBalance().subtract(amount).toString());
            super.getTransactionHistory().add(new Transaction(getAccountNumber(), TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, getCurrencyCode()));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(getAccountNumber())) {
                TransactionHistoryCSVHandler.write(getTransactionHistory(), new File("transactions/transaction_history_" + getAccountNumber() + ".csv").getAbsolutePath());
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + TYPE + ")" + " (" + getStatus() + ")" +
                "\nInterest rate: " + getInterestRate() + "%" +
                "\nMinimal balance allowed: " + getMinBalance();
    }
}

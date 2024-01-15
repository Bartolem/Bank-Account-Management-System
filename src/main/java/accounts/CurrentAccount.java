package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrentAccount extends Account {
    private static BigDecimal overdraftLimit = new BigDecimal(500);
    private static final AccountTypes TYPE = AccountTypes.CURRENT;

    public CurrentAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
    }

    public CurrentAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status);
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

    public static AccountTypes getType() {
        return CurrentAccount.TYPE;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);

        if (amount.compareTo(availableBalance) < 1) {
            setBalance(getBalance().subtract(amount).toString());
            super.getTransactionHistory().add(new Transaction(TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, getCurrencyCode()));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(getAccountNumber())) {
                TransactionHistoryToCSV.write(getTransactionHistory(), "transaction_history_" + getAccountNumber());
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft limit: " + getOverdraftLimit();
    }
}

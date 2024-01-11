package accounts;

import currencies.CurrencyCodes;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrentAccount extends Account {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = AccountTypes.CURRENT;
    }

    public CurrentAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = AccountTypes.CURRENT;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);

        if (amount.compareTo(availableBalance) < 1) {
            setBalance(getBalance().subtract(amount).toString());
            super.getTransactionHistory().add(new Transaction(TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, getCurrencyCode()));
            TransactionHistoryToCSV.write(super.getTransactionHistory(), "transaction_history_" + getAccountNumber());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft limit: " + getOverdraftLimit();
    }
}

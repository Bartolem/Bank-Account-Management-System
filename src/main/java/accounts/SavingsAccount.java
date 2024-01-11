package accounts;

import currencies.CurrencyCodes;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingsAccount extends Account {
    private final BigDecimal interestRate;
    private BigDecimal minBalance;

    public SavingsAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
        this.interestRate = new BigDecimal("0.3");
        this.type = AccountTypes.SAVINGS;
        this.minBalance = new BigDecimal(0);
    }

    public SavingsAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status);
        this.interestRate = new BigDecimal("0.3");
        this.type = AccountTypes.SAVINGS;
        this.minBalance = new BigDecimal(0);
    }

    public void calculateInterestRate() {
        setBalance(getBalance().add(getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100))).toString());
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            this.minBalance = amount;
        }
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if ((getBalance().subtract(amount).compareTo(minBalance) > -1)) {
            setBalance(getBalance().subtract(amount).toString());
            super.getTransactionHistory().add(new Transaction(TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, getCurrencyCode()));
            TransactionHistoryToCSV.write(super.getTransactionHistory(), "transaction_history_" + getAccountNumber());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "\nInterest rate: " + getInterestRate() + "%" +
                "\nMinimal balance allowed: " + getMinBalance();
    }
}

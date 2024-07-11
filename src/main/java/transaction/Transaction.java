package transaction;

import currencies.CurrencyCodes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Comparable<Transaction> {
    private final int accountNumber;
    private final TransactionTypes type;
    private final LocalDateTime date;
    private final BigDecimal amount;
    private final CurrencyCodes currencyCode;

    public Transaction(int accountNumber, TransactionTypes type, LocalDateTime date, BigDecimal amount, CurrencyCodes currencyCode) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public TransactionTypes getType() {
        return type;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return "%s\nDate: %s\nAmount: %s\nCurrency: %s".formatted(type, getFormattedDate(), amount, currencyCode);
    }

    @Override
    public int compareTo(Transaction other) {
        return this.date.compareTo(other.date);
    }
}

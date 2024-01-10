package transaction;

import currencies.CurrencyCodes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final TransactionTypes type;
    private final LocalDateTime date;
    private final BigDecimal amount;
    private final CurrencyCodes currencyCode;

    public Transaction(TransactionTypes type, LocalDateTime date, BigDecimal amount, CurrencyCodes currencyCode) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public TransactionTypes getType() {
        return type;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return "%s\nDate: %s\nAmount: %s\nCurrency: %s".formatted(type, getDate(), amount, currencyCode);
    }
}

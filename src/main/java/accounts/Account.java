package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import currencies.CurrencyFormatter;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final CurrencyCodes currencyCode;
    private final int accountNumber;
    protected AccountTypes type;
    private BigDecimal balance;
    private final User user;
    private final LocalDateTime date;
    private final List<Transaction> transactionHistory;

    public Account(User user, CurrencyCodes currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.accountNumber = AccountNumber.getNumber();
        this.type = AccountTypes.STANDARD;
        this.balance = new BigDecimal(balance);
        this.user = user;
        this.date = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        user.addOwnedAccount(this);
    }

    public Account(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date) {
        this.currencyCode = currencyCode;
        this.accountNumber = accountNumber;
        this.user = user;
        this.type = AccountTypes.STANDARD;
        this.balance = new BigDecimal(balance);
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.transactionHistory = new ArrayList<>();
        user.addOwnedAccount(this);
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
    }

    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
    }

    public AccountTypes getType() {
        return type;
    }

    public String getOwnerName() {
        return user.getPerson().getFullName();
    }

    public String getOwnerFirstName() {
        return user.getPerson().getFirstName();
    }

    public void setOwnerFirstName(String firstName) {
        user.getPerson().setFirstName(firstName);
    }

    public String getOwnerLastName() {
        return user.getPerson().getLastName();
    }

    public void setOwnerLastName(String lastName) {
        user.getPerson().setLastName(lastName);
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    public String getFormattedBalanceWithCurrency() {
        return CurrencyFormatter.getFormat(currencyCode, getBalance());
    }

    public void setBalance(String amount) {
        this.balance = new BigDecimal(amount);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public boolean deposit(BigDecimal amount) {
        if (isPositiveAmount(amount)) {
            setBalance(getBalance().add(amount).toString());
            addTransaction(new Transaction(TransactionTypes.DEPOSIT, LocalDateTime.now(), amount, currencyCode));
            TransactionHistoryToCSV.write(transactionHistory, "transaction_history_" + accountNumber);
            return true;
        }
        return false;
    }

    public boolean withdraw(BigDecimal amount) {
        if (isPositiveAmount(amount) && isPositiveAmount(getBalance().subtract(amount))) {
            setBalance(getBalance().subtract(amount).toString());
            addTransaction(new Transaction(TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, currencyCode));
            TransactionHistoryToCSV.write(transactionHistory, "transaction_history_" + accountNumber);
            return true;
        }
        return false;
    }

    public boolean transfer(BigDecimal amount, int accountNumber) {
        if (isPositiveAmount(amount) && isPositiveAmount(getBalance().subtract(amount))) {
            Account receiver = Bank.getInstance().getAccount(accountNumber);
            if (receiver != null && !receiver.equals(this) && receiver.getCurrencyCode().equals(this.getCurrencyCode())) {
                receiver.deposit(amount);
                withdraw(amount);
                addTransaction(new Transaction(TransactionTypes.TRANSFER, LocalDateTime.now(), amount, currencyCode));
                TransactionHistoryToCSV.write(transactionHistory, "transaction_history_" + this.accountNumber);
                return true;
            }
        }
        return false;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public String toString() {
        return  "(" + type + ")" +
                "\nAccount number: " + accountNumber +
                "\nOwner name: " + getOwnerName() +
                "\nBalance: " + balance;
    }
}

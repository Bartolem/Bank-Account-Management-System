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

public abstract class Account {
    private final CurrencyCodes currencyCode;
    private int accountNumber;
    private BigDecimal balance;
    private final User user;
    private LocalDateTime creationDate;
    private List<Transaction> transactionHistory;
    private boolean blocked;
    private AccountStatus status;

    public Account(User user, CurrencyCodes currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.accountNumber = AccountNumber.getNumber();
        this.balance = new BigDecimal(balance);
        this.user = user;
        this.creationDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        this.blocked = false;
        this.status = AccountStatus.ACTIVE;
        user.addOwnedAccount(this);
    }

    public Account(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        this(user, currencyCode, balance);
        this.accountNumber = accountNumber;
        this.creationDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.transactionHistory = new ArrayList<>();
        this.blocked = blocked;
        this.status = AccountStatus.valueOf(status);
        user.addOwnedAccount(this);
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
    }

    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
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

    public String getCreationDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void block() {
        this.blocked = true;
        setStatus(AccountStatus.BLOCKED);
    }

    public void unlock() {
        this.blocked = false;
        setStatus(AccountStatus.ACTIVE);
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public abstract AccountTypes getType();

    public boolean deposit(BigDecimal amount) {
        if (isPositiveAmount(amount)) {
            setBalance(getBalance().add(amount).toString());
            addTransaction(new Transaction(accountNumber, TransactionTypes.DEPOSIT, LocalDateTime.now(), amount, currencyCode));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(accountNumber)) {
                saveTransactionHistoryToFile();
            }
            return true;
        }
        return false;
    }

    public boolean withdraw(BigDecimal amount) {
        if (isPositiveAmount(amount) && isPositiveAmount(getBalance().subtract(amount))) {
            setBalance(getBalance().subtract(amount).toString());
            addTransaction(new Transaction(accountNumber, TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, currencyCode));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(accountNumber)) {
                saveTransactionHistoryToFile();
            }
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
                addTransaction(new Transaction(accountNumber, TransactionTypes.TRANSFER, LocalDateTime.now(), amount, currencyCode));
                // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
                if (Bank.getInstance().contains(accountNumber)) {
                    saveTransactionHistoryToFile();
                }
                return true;
            }
        }
        return false;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    protected void saveTransactionHistoryToFile() {
        TransactionHistoryToCSV.write(transactionHistory, "transactions/transaction_history_" + this.accountNumber + ".csv");
    }

    @Override
    public String toString() {
        return "\nAccount number: " + accountNumber +
                "\nOwner name: " + getOwnerName() +
                "\nBalance: " + balance;
    }
}

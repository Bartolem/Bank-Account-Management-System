package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import currencies.CurrencyFormatter;
import file_manipulation.FileManipulator;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Account {
    private final CurrencyCodes currencyCode;
    private int accountNumber;
    private BigDecimal balance;
    private final User user;
    private LocalDate creationDate;
    private boolean blocked;
    private AccountStatus status;
    private final TransactionManager transactionManager;
    private final LimitManager limitManager;

    public Account(User user, CurrencyCodes currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.accountNumber = AccountNumber.getNumber();
        this.balance = new BigDecimal(balance);
        this.user = user;
        this.creationDate = LocalDate.now();
        this.blocked = false;
        this.status = AccountStatus.ACTIVE;
        this.transactionManager = new TransactionManager();
        this.limitManager = new LimitManager(new BigDecimal("5000"), new BigDecimal("30000"));
        user.addOwnedAccount(this);
    }

    public Account(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        this(user, currencyCode, balance);
        this.accountNumber = accountNumber;
        this.creationDate = LocalDate.parse(date);
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public List<Transaction> getTransactionHistory() {
        return transactionManager.getTransactionHistory();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public BigDecimal getDailyLimit() {
        return limitManager.getDailyLimit();
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        limitManager.setDailyLimit(dailyLimit);
    }

    public BigDecimal getMonthlyLimit() {
        return limitManager.getMonthlyLimit();
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        limitManager.setMonthlyLimit(monthlyLimit);
    }

    public BigDecimal getDailyUsage(LocalDate date) {
        return limitManager.getDailyUsage(date);
    }

    public BigDecimal getMonthlyUsage(YearMonth month) {
        return limitManager.getMonthlyUsage(month);
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
            transactionManager.addTransaction(new Transaction(accountNumber, TransactionTypes.DEPOSIT, LocalDateTime.now(), amount, currencyCode));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(accountNumber)) {
                FileManipulator.saveTransactionHistoryToFile(transactionManager.getTransactionHistory(), accountNumber);
            }
            return true;
        }
        return false;
    }

    public LimitManager getLimitManager() {
        return limitManager;
    }

    public boolean withdraw(BigDecimal amount) {
        if (isPositiveAmount(amount) && isPositiveAmount(getBalance().subtract(amount))) {
            if (limitManager.checkDailyLimit(amount)) {
                System.out.println("Daily limit exceeded");
                return false;
            }
            if (limitManager.checkMonthlyLimit(amount)) {
                System.out.println("Monthly limit exceeded");
                return false;
            }

            LocalDate today = LocalDate.now();

            limitManager.updateDailyUsage(today, amount);
            limitManager.updateMonthlyUsage(YearMonth.from(today), amount);
            setBalance(getBalance().subtract(amount).toString());
            transactionManager.addTransaction(new Transaction(accountNumber, TransactionTypes.WITHDRAW, LocalDateTime.now(), amount, currencyCode));
            // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
            if (Bank.getInstance().contains(accountNumber)) {
                FileManipulator.saveTransactionHistoryToFile(transactionManager.getTransactionHistory(), accountNumber);
            }
            return true;
        }
        return false;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public boolean transfer(BigDecimal amount, int accountNumber) {
        if (isPositiveAmount(amount) && isPositiveAmount(getBalance().subtract(amount))) {
            Account receiver = Bank.getInstance().getAccount(accountNumber);
            if (receiver != null && !receiver.equals(this) && receiver.getCurrencyCode().equals(this.getCurrencyCode())) {
                receiver.deposit(amount);
                withdraw(amount);
                transactionManager.addTransaction(new Transaction(accountNumber, TransactionTypes.TRANSFER, LocalDateTime.now(), amount, currencyCode));
                // Checks if the account exist in bank. Accounts created by unit testing are not included, so we don't need to save their transaction history.
                if (Bank.getInstance().contains(accountNumber)) {
                    FileManipulator.saveTransactionHistoryToFile(transactionManager.getTransactionHistory(), accountNumber);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nAccount number: " + accountNumber +
                "\nOwner name: " + getOwnerName() +
                "\nBalance: " + balance;
    }
}

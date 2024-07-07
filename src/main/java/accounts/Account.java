package accounts;

import bank.Bank;
import currencies.CurrencyCodes;
import currencies.CurrencyFormatter;
import file_manipulation.TransactionHistoryToCSV;
import transaction.Transaction;
import transaction.TransactionComparators;
import transaction.TransactionDateRanges;
import transaction.TransactionTypes;
import users.User;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Account {
    private final CurrencyCodes currencyCode;
    private int accountNumber;
    private BigDecimal balance;
    private final User user;
    private LocalDateTime creationDate;
    private List<Transaction> transactionHistory;
    private boolean blocked;
    private AccountStatus status;
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
    private final Map<LocalDate, BigDecimal> dailyUsage;
    private final Map<YearMonth, BigDecimal> monthlyUsage;

    public Account(User user, CurrencyCodes currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.accountNumber = AccountNumber.getNumber();
        this.balance = new BigDecimal(balance);
        this.user = user;
        this.creationDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        this.blocked = false;
        this.status = AccountStatus.ACTIVE;
        this.dailyLimit = new BigDecimal("5000");
        this.monthlyLimit = new BigDecimal("30000");
        this.dailyUsage = new HashMap<>();
        this.monthlyUsage = new HashMap<>();
        user.addOwnedAccount(this);
    }

    public Account(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status, String dailyLimit, String monthlyLimit, String dailyUsage, String monthlyUsage) {
        this(user, currencyCode, balance);
        this.accountNumber = accountNumber;
        this.creationDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.transactionHistory = new ArrayList<>();
        this.blocked = blocked;
        this.status = AccountStatus.valueOf(status);
        this.dailyLimit = new BigDecimal(dailyLimit);
        this.monthlyLimit = new BigDecimal(monthlyLimit);
        updateDailyUsage(LocalDate.now(), new BigDecimal(dailyUsage));
        updateMonthlyUsage(YearMonth.now(), new BigDecimal(monthlyUsage));
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

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        if (dailyLimit.compareTo(BigDecimal.valueOf(0)) >= 0) this.dailyLimit = dailyLimit;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        if (monthlyLimit.compareTo(BigDecimal.valueOf(0)) >= 0) this.monthlyLimit = monthlyLimit;
    }

    public BigDecimal getDailyUsage(LocalDate date) {
        return dailyUsage.getOrDefault(date, BigDecimal.ZERO);
    }

    public BigDecimal getMonthlyUsage(YearMonth month) {
        return monthlyUsage.getOrDefault(month, BigDecimal.ZERO);
    }

    protected void updateDailyUsage(LocalDate date, BigDecimal amount) {
        dailyUsage.put(date, getDailyUsage(date).add(amount));
    }

    protected void updateMonthlyUsage(YearMonth month, BigDecimal amount) {
        monthlyUsage.put(month, getMonthlyUsage(month).add(amount));
    }

    public boolean checkDailyLimit(BigDecimal amount) {
        LocalDate today = LocalDate.now();
        return getDailyUsage(today).add(amount).compareTo(dailyLimit) > 0;
    }

    public boolean checkMonthlyLimit(BigDecimal amount) {
        YearMonth currentMonth = YearMonth.now();
        return getMonthlyUsage(currentMonth).add(amount).compareTo(monthlyLimit) > 0;
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
            if (checkDailyLimit(amount)) {
                System.out.println("Daily limit exceeded");
                return false;
            }
            if (checkMonthlyLimit(amount)) {
                System.out.println("Monthly limit exceeded");
                return false;
            }

            LocalDate today = LocalDate.now();

            updateDailyUsage(today, amount);
            updateMonthlyUsage(YearMonth.from(today), amount);
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
        TransactionHistoryToCSV.write(transactionHistory, new File("transactions/transaction_history_" + this.accountNumber + ".csv").getAbsolutePath());
    }

    public List<Transaction> getTransactionsSortedByDate(List<Transaction> transactions) {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        Collections.sort(sortedTransactions);
        return sortedTransactions;
    }

    public List<Transaction> getTransactionsSortedByAmount(List<Transaction> transactions) {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort(TransactionComparators.byAmount());
        return sortedTransactions;
    }

    public List<Transaction> getTransactionsSortedByType(List<Transaction> transactions) {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort(TransactionComparators.byType());
        return sortedTransactions;
    }

    public List<Transaction> filterTransactionsByType(TransactionTypes type) {
        return transactionHistory.stream()
                .filter(transaction -> transaction.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Transaction> filterTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> !transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount, List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(minAmount) >= 0 && transaction.getAmount().compareTo(maxAmount) <= 0)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsForDay(LocalDate date, List<Transaction> transactions) {
        LocalDateTime[] range = TransactionDateRanges.getDayRange(date);
        return filterTransactionsByDateRange(range[0], range[1], transactions);
    }

    public List<Transaction> getTransactionsForWeek(LocalDate date, List<Transaction> transactions) {
        LocalDateTime[] range = TransactionDateRanges.getWeekRange(date);
        return filterTransactionsByDateRange(range[0], range[1], transactions);
    }

    public List<Transaction> getTransactionsForMonth(LocalDate date, List<Transaction> transactions) {
        LocalDateTime[] range = TransactionDateRanges.getMonthRange(date);
        return filterTransactionsByDateRange(range[0], range[1], transactions);
    }

    public List<Transaction> getTransactionsForYear(LocalDate date, List<Transaction> transactions) {
        LocalDateTime[] range = TransactionDateRanges.getYearRange(date);
        return filterTransactionsByDateRange(range[0], range[1], transactions);
    }

    @Override
    public String toString() {
        return "\nAccount number: " + accountNumber +
                "\nOwner name: " + getOwnerName() +
                "\nBalance: " + balance;
    }
}

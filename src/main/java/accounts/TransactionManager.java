package accounts;

import transaction.Transaction;
import transaction.TransactionComparators;
import transaction.TransactionDateRanges;
import transaction.TransactionTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager {
    private final List<Transaction> transactionHistory;

    public TransactionManager() {
        this.transactionHistory = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
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

    public LocalDateTime getLastTransactionDate(TransactionTypes type) {
        List<Transaction> filteredTransactionsByType = filterTransactionsByType(type);

        if (filteredTransactionsByType.isEmpty()) {
            return null;
        }

        return filteredTransactionsByType.get(filteredTransactionsByType.size() - 1).getDate();
    }
}

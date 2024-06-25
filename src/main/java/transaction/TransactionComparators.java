package transaction;

import java.util.Comparator;

public class TransactionComparators {
    public static Comparator<Transaction> byAmount() {
        return Comparator.comparing(Transaction::getAmount);
    }

    public static Comparator<Transaction> byType() {
        return Comparator.comparing(Transaction::getType);
    }
}

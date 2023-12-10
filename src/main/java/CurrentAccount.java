import java.math.BigDecimal;

public class CurrentAccount extends Account {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(User user, String currencyCode, String balance) {
        super(user, currencyCode, balance);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = "Current";
    }

    public CurrentAccount(int accountNumber, User user, String currencyCode, String balance, String date) {
        super(accountNumber, user, currencyCode, balance, date);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = "Current";
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);

        if (amount.compareTo(availableBalance) < 1) {
            setBalance(getBalance().subtract(amount).toString());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft limit: " + getOverdraftLimit();
    }
}

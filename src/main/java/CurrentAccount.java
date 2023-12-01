import java.math.BigDecimal;

public class CurrentAccount extends Account {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(String ownerName) {
        super(ownerName);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = "Current";
    }

    public CurrentAccount(int accountNumber, String ownerName, String balance, String date) {
        super(accountNumber, ownerName, balance, date);
        this.overdraftLimit = new BigDecimal(5000);
        this.type = "Current";
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);

        if (amount.compareTo(availableBalance) <= availableBalance.doubleValue()) {
            setBalance(getBalance().subtract(amount));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft limit: " + getOverdraftLimit();
    }
}

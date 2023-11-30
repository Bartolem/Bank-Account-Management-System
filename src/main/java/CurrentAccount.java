import java.math.BigDecimal;

public class CurrentAccount extends Account {
    private final BigDecimal OVERDRAFT_LIMIT;

    public CurrentAccount(String ownerName) {
        super(ownerName);
        this.OVERDRAFT_LIMIT = new BigDecimal(5000);
        this.type = "Current";
    }

    public CurrentAccount(String ownerName, String balance) {
        super(ownerName, balance);
        this.OVERDRAFT_LIMIT = new BigDecimal(5000);
        this.type = "Current";
    }

    public BigDecimal getOVERDRAFT_LIMIT() {
        return OVERDRAFT_LIMIT;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(OVERDRAFT_LIMIT);

        if (amount.compareTo(availableBalance) <= availableBalance.doubleValue()) {
            setBalance(getBalance().subtract(amount));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft limit: " + getOVERDRAFT_LIMIT();
    }
}

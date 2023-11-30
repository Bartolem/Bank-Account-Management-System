import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal INTEREST_RATE;
    private BigDecimal minBalance;

    public SavingsAccount(String ownerName) {
        super(ownerName);
        this.INTEREST_RATE = new BigDecimal("0.3");
        this.minBalance = new BigDecimal(0);
    }

    public SavingsAccount( String ownerName, String balance, String minBalance) {
        super(ownerName, balance);
        this.INTEREST_RATE = new BigDecimal("0.3");
        this.minBalance = new BigDecimal(minBalance);
    }

    public void calculateInterestRate() {
        setBalance(getBalance().add(getBalance().multiply(INTEREST_RATE).divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal getINTEREST_RATE() {
        return INTEREST_RATE;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            this.minBalance = amount;
        }
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if ((getBalance().subtract(amount).compareTo(minBalance) >= minBalance.intValue())) {
            setBalance(getBalance().subtract(amount));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(Savings Account) \n" + super.toString() +
                "\nInterest rate: " + getINTEREST_RATE() + "%" +
                "\nMinimal balance allowed: " + getMinBalance();
    }
}

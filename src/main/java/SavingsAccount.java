import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal interestRate;
    private BigDecimal minBalance;

    public SavingsAccount(String ownerName) {
        super(ownerName);
        this.interestRate = new BigDecimal("0.3");
        this.type = "Savings";
    }

    public SavingsAccount(int accountNumber, String ownerName, String balance, String date) {
        super(accountNumber, ownerName, balance, date);
        this.interestRate = new BigDecimal("0.3");
        this.type = "Savings";
    }

    public void calculateInterestRate() {
        setBalance(getBalance().add(getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100))));
    }

    public BigDecimal getInterestRate() {
        return interestRate;
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
        return  super.toString() +
                "\nInterest rate: " + getInterestRate() + "%" +
                "\nMinimal balance allowed: " + getMinBalance();
    }
}

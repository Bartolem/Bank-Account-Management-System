import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal interestRate;
    private BigDecimal minBalance;

    public SavingsAccount(User user, String currencyCode, String balance) {
        super(user, currencyCode, balance);
        this.interestRate = new BigDecimal("0.3");
        this.type = "Savings";
        this.minBalance = new BigDecimal(0);
    }

    public SavingsAccount(int accountNumber, User user, String currencyCode, String balance, String date) {
        super(accountNumber, user, currencyCode, balance, date);
        this.interestRate = new BigDecimal("0.3");
        this.type = "Savings";
        this.minBalance = new BigDecimal(0);
    }

    public void calculateInterestRate() {
        setBalance(getBalance().add(getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100))).toString());
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
        if ((getBalance().subtract(amount).compareTo(minBalance) > -1)) {
            setBalance(getBalance().subtract(amount).toString());
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

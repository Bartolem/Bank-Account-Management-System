public class SavingsAccount extends Account {
    private final double INTEREST_RATE;
    private int minBalance;

    public SavingsAccount(String ownerName) {
        super(ownerName);
        this.INTEREST_RATE = 0.3;
        this.minBalance = 0;
    }

    public SavingsAccount( String ownerName, double balance, int minBalance) {
        super(ownerName, balance);
        this.INTEREST_RATE = 0.3;
        this.minBalance = minBalance;
    }

    public void calculateInterestRate() {
        setBalance(getBalance() + (getBalance() * INTEREST_RATE / 100));
    }

    public double getINTEREST_RATE() {
        return INTEREST_RATE;
    }

    public int getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(int amount) {
        if (amount > 0) {
            this.minBalance = amount;
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if ((getBalance() - amount >= minBalance)) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(Savings Account) \n" + super.toString();
    }
}

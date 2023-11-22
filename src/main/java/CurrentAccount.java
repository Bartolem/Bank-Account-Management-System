public class CurrentAccount extends Account {
    private final int OVERDRAFT_LIMIT;

    public CurrentAccount(String ownerName) {
        super(ownerName);
        this.OVERDRAFT_LIMIT = 5000;
    }

    @Override
    public boolean withdraw(double amount) {
        double availableBalance = getBalance() + OVERDRAFT_LIMIT;

        if (amount <= availableBalance) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(Current Account) \n" + super.toString();
    }
}

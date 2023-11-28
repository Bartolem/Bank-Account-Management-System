import java.util.Random;

public class Account {
    private final int ACCOUNT_NUMBER;
    private double balance;

    private String ownerName;
    private final Random random;

    public Account(String ownerName) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.balance = 0;
        this.ownerName = ownerName;
    }

    public Account(String ownerName, double balance) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= 0) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }

    private int generateRandomAccountNumber() {
        return random.nextInt(1_000_000, 10_000_000);
    }

    @Override
    public String toString() {
        return "Account number: " + ACCOUNT_NUMBER +
                "\nOwner name: " + ownerName +
                "\nBalance: " + balance;
    }
}

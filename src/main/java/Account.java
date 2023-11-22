import java.util.Random;

public class Account {
    private int accountNumber;
    private double balance;
    private String ownerName;
    private Random random;

    public Account(String ownerName) {
        this.random = new Random();
        this.accountNumber = generateRandomAccountNumber();
        this.balance = 0;
        this.ownerName = ownerName;
    }

    public Account(double balance, String ownerName) {
        this.random = new Random();
        this.accountNumber = generateRandomAccountNumber();
        this.balance = balance;
        this.ownerName = ownerName;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= 0) {
            setBalance(amount);
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
        return "Account number: " + accountNumber +
                "\nOwner name: " + ownerName +
                "\nBalance: " + balance;
    }
}

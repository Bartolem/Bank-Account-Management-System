public class Account {
    private int accountNumber;
    private double balance;
    private String ownerName;

    public Account(int accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.ownerName = ownerName;
    }

    public Account(int accountNumber, double balance, String ownerName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerName = ownerName;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) > 0) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account number: " + accountNumber +
                "\nOwner name: " + ownerName +
                "\nBalance: " + balance;
    }
}

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Account {
    private final int ACCOUNT_NUMBER;
    private BigDecimal balance;

    private String ownerName;
    private final Random random;

    public Account(String ownerName) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.balance = new BigDecimal("0");
        this.ownerName = ownerName;
    }

    public Account(String ownerName, String balance) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.ownerName = ownerName;
        this.balance = new BigDecimal(balance);
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
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

    public void setBalance(BigDecimal amount) {
        this.balance = amount;
    }

    public boolean deposit(BigDecimal amount) {
        if (isPositiveAmount(amount)) {
            setBalance(getBalance().add(amount));
            return true;
        }
        return false;
    }

    public boolean withdraw(BigDecimal amount) {
        if (isPositiveAmount(amount) && isPositiveAmount(balance.subtract(amount))) {
            setBalance(getBalance().subtract(amount));
            return true;
        }
        return false;
    }

    public BigDecimal getBalance() {
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

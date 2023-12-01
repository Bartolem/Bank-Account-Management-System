import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Account {
    private final Random random;
    private final int ACCOUNT_NUMBER;
    protected String type;
    private BigDecimal balance;
    private String ownerName;
    private final LocalDateTime DATE;

    public Account(String ownerName) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.type = "Standard";
        this.balance = new BigDecimal("0");
        this.ownerName = ownerName;
        this.DATE = LocalDateTime.now();
    }

    public Account(String ownerName, String balance) {
        this.random = new Random();
        this.ACCOUNT_NUMBER = generateRandomAccountNumber();
        this.type = "Standard";
        this.ownerName = ownerName;
        this.balance = new BigDecimal(balance);
        this.DATE = LocalDateTime.now();
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
    }

    public String getType() {
        return type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    public int getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public String getDATE() {
        return DATE.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
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

    private int generateRandomAccountNumber() {
        return random.nextInt(1_000_000, 10_000_000);
    }

    @Override
    public String toString() {
        return  "(" + type + ")" +
                "\nAccount number: " + ACCOUNT_NUMBER +
                "\nOwner name: " + ownerName +
                "\nBalance: " + balance;
    }
}

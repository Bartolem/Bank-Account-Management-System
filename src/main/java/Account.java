import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Account {
    private final String currencyCode;
    private final Random random;
    private final int accountNumber;
    protected String type;
    private BigDecimal balance;
    private String ownerName;
    private final LocalDateTime date;
    private final DateTimeFormatter dateTimeFormatter;

    public Account(String ownerName, String currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.random = new Random();
        this.accountNumber = generateRandomAccountNumber();
        this.type = "Standard";
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.balance = new BigDecimal(balance);
        this.ownerName = ownerName;
        this.date = LocalDateTime.now();
    }

    public Account(int accountNumber, String ownerName, String currencyCode, String balance, String date) {
        this.currencyCode = currencyCode;
        this.random = new Random();
        this.accountNumber = accountNumber;
        this.type = "Standard";
        this.ownerName = ownerName;
        this.balance = new BigDecimal(balance);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = LocalDateTime.parse(date, dateTimeFormatter);
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
    }

    public String getCurrencyCode() {
        return currencyCode;
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

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getDate() {
        return date.format(dateTimeFormatter);
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    public void setBalance(String amount) {
        this.balance = new BigDecimal(amount);
    }

    public boolean deposit(BigDecimal amount) {
        if (isPositiveAmount(amount)) {
            setBalance(getBalance().add(amount).toString());
            return true;
        }
        return false;
    }

    public boolean withdraw(BigDecimal amount) {
        if (isPositiveAmount(amount) && isPositiveAmount(balance.subtract(amount))) {
            setBalance(getBalance().subtract(amount).toString());
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
                "\nAccount number: " + accountNumber +
                "\nOwner name: " + ownerName +
                "\nBalance: " + balance;
    }
}

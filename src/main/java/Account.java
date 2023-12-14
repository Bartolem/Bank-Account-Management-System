import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private final CurrencyCodes currencyCode;
    private final int accountNumber;
    protected AccountTypes type;
    private BigDecimal balance;
    private final User user;
    private final LocalDateTime date;

    public Account(User user, CurrencyCodes currencyCode, String balance) {
        this.currencyCode = currencyCode;
        this.accountNumber = AccountNumber.getNumber();
        this.type = AccountTypes.STANDARD;
        this.balance = new BigDecimal(balance);
        this.user = user;
        this.date = LocalDateTime.now();
        user.addOwnedAccount(this);
    }

    public Account(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date) {
        this.currencyCode = currencyCode;
        this.accountNumber = accountNumber;
        this.type = AccountTypes.STANDARD;
        this.user = user;
        this.balance = new BigDecimal(balance);
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        user.addOwnedAccount(this);
    }

    public boolean isPositiveAmount(BigDecimal amount) {
        return amount.doubleValue() > 0;
    }

    public CurrencyCodes getCurrencyCode() {
        return currencyCode;
    }

    public AccountTypes getType() {
        return type;
    }

    public String getOwnerName() {
        return user.getPerson().getFullName();
    }

    public String getOwnerFirstName() {
        return user.getPerson().getFirstName();
    }

    public void setOwnerFirstName(String firstName) {
        user.getPerson().setFirstName(firstName);
    }

    public String getOwnerLastName() {
        return user.getPerson().getLastName();
    }

    public void setOwnerLastName(String lastName) {
        user.getPerson().setLastName(lastName);
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalance(String amount) {
        this.balance = new BigDecimal(amount);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
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

    @Override
    public String toString() {
        return  "(" + type + ")" +
                "\nAccount number: " + accountNumber +
                "\nOwner name: " + getOwnerName() +
                "\nBalance: " + balance;
    }
}

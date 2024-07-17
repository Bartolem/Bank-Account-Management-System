package accounts;

import currencies.CurrencyCodes;
import users.User;

public class StandardAccount extends Account {
    public static final AccountTypes TYPE = AccountTypes.STANDARD;

    public StandardAccount(User user, CurrencyCodes currencyCode, String balance) {
        super(user, currencyCode, balance);
    }

    public StandardAccount(int accountNumber, User user, CurrencyCodes currencyCode, String balance, String date, boolean blocked, String status) {
        super(accountNumber, user, currencyCode, balance, date, blocked, status);
    }

    @Override
    public AccountTypes getType() {
        return TYPE;
    }
}

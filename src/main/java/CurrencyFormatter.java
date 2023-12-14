import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class CurrencyFormatter {
    public static String getFormat(CurrencyCodes currencyCode, BigDecimal number) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        currencyFormatter.setCurrency(Currency.getInstance(currencyCode.toString()));
        return currencyFormatter.format(number);
    }
}

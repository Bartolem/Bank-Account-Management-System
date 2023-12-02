import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class CurrencyFormatter {
    public static String getFormat(String currencyCode, BigDecimal number) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        currencyFormatter.setCurrency(Currency.getInstance(currencyCode));
        return currencyFormatter.format(number);
    }
}

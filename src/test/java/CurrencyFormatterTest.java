import currencies.CurrencyCodes;
import currencies.CurrencyFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyFormatterTest {

    @Test
    void getFormat() {
        assertEquals("3 000,52 €", CurrencyFormatter.getFormat(CurrencyCodes.EUR, new BigDecimal("3000.52")));
    }
}
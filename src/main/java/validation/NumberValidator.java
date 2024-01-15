package validation;

import java.math.BigDecimal;

public class NumberValidator {
    public static boolean validate(String number) {
        try {
            new BigDecimal(number);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Enter only numbers.");
            return false;
        }
    }
}

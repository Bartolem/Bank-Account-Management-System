package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public boolean validateDateOfBirth(String Date) {
        Pattern pattern = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = pattern.matcher(Date);
        return matcher.matches();
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        // Allow country code prefix (+1-4 digits), 9-10 digits and optional whitespace, dots, or hyphens (-) between the numbers
        Pattern pattern = Pattern.compile("^\\+\\d{1,4} [\\d\\s\\-._]+");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean validateEmail(String email) {
        // Regular expression for email validation provided by the RFC 5322 standards
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

import java.util.ArrayList;
import java.util.Random;

public class AccountNumber {
    private static final ArrayList<Integer> accountNumbers = new ArrayList<>();

    private static int generateRandomAccountNumber() {
        return new Random().nextInt(10_000_000, 100_000_000);
    }

    private static boolean isUnique (int number) {
        if (!accountNumbers.contains(number)) {
            return true;
        } else isUnique(generateRandomAccountNumber());
        return false;
    }

    public static int getUniqueNumber( int randomNumber) {
        int accountNumber = 0;

        if (isUnique(randomNumber)) {
            accountNumber = randomNumber;
            accountNumbers.add(accountNumber);
        }

        return accountNumber;
    }

    public static int getNumber() {
        return getUniqueNumber(generateRandomAccountNumber());
    }

    public static ArrayList<Integer> getAccountNumbers() {
        return accountNumbers;
    }
}

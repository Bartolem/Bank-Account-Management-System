package accounts;

import bank.Bank;

import java.util.Random;

public class AccountNumber {
    private static int generateRandomAccountNumber() {
        return new Random().nextInt(10_000_000, 100_000_000);
    }

    private static boolean isUnique (int number) {
        if (!Bank.getInstance().getAccountNumbers().contains(number)) {
            return true;
        } else isUnique(generateRandomAccountNumber());
        return false;
    }

    private static int getUniqueNumber(int randomNumber) {
        int accountNumber = 0;

        if (isUnique(randomNumber)) {
            accountNumber = randomNumber;
        }

        return accountNumber;
    }

    public static int getNumber() {
        return getUniqueNumber(generateRandomAccountNumber());
    }
}

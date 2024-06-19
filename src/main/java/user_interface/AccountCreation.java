package user_interface;

import accounts.*;
import currencies.CurrencyCodes;
import users.User;
import validation.NumberValidator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static accounts.AccountTypes.*;

public class AccountCreation {
    private final Scanner scanner;

    public AccountCreation(Scanner scanner) {
        this.scanner = scanner;
    }

    private void printCursor() {
        System.out.print("> ");
    }

    protected Account createAccount(User user) {
        AccountTypes accountType = chooseAccountType();
        CurrencyCodes currencyCode = chooseCurrency();
        String initialBalance = String.valueOf(setInitialBalance());

        switch (accountType) {
            case STANDARD -> {
                return new StandardAccount(user, currencyCode, initialBalance);
            }
            case SAVINGS -> {
                return new SavingsAccount(user, currencyCode, initialBalance);
            }
            case CURRENT -> {
                return new CurrentAccount(user, currencyCode,initialBalance);
            }
        }

        return null;
    }
    private AccountTypes chooseAccountType() {
        AccountTypes accountType;
        System.out.println("There are several types of accounts to choose from: " + Arrays.toString(AccountTypes.values()));

        loop: while (true) {
            System.out.println("Which one do you choose?");

            for (AccountTypes type : AccountTypes.values()) {
                System.out.println("("+ (type.ordinal() + 1) + ") " + type.getName());
            }

            printCursor();
            String answer = scanner.nextLine();

            switch (answer) {
                case "1":
                    accountType = STANDARD;
                    break loop;
                case "2":
                    accountType = SAVINGS;
                    break loop;
                case "3":
                    accountType = CURRENT;
                    break loop;
            }
        }

        return accountType;
    }

    private CurrencyCodes chooseCurrency() {
        List<CurrencyCodes> currencyCodes = Arrays.asList(CurrencyCodes.values());

        System.out.println("Currently supported currencies: " + currencyCodes);
        CurrencyCodes currencyCode;

        while (true) {
            System.out.println("In which currency?");
            printCursor();
            String answer = scanner.nextLine().toUpperCase();

            try {
                if (currencyCodes.contains(CurrencyCodes.valueOf(answer))) {
                    currencyCode = CurrencyCodes.valueOf(answer);
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("There is no \"" + answer + "\" in list of supported currencies.");
            }
        }
        return currencyCode;
    }

    private BigDecimal setInitialBalance() {
        BigDecimal initialBalance = new BigDecimal(0);
        System.out.println("Do you want to provide an initial balance? (y/n)");
        printCursor();

        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            while (true) {
                System.out.println("Initial balance: ");
                printCursor();

                String input = scanner.nextLine();
                if (NumberValidator.validate(input)) {
                    initialBalance = new BigDecimal(input);
                    break;
                }
            }
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            return initialBalance;
        }

        return initialBalance;
    }
}

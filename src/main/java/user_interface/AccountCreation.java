package user_interface;

import accounts.*;
import bank.Bank;
import currencies.CurrencyCodes;
import file_manipulation.FileManipulator;
import users.Admin;
import users.User;
import validation.Validation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static accounts.AccountTypes.*;
import static user_interface.UserInterface.printCursor;

public class AccountCreation {
    private final Scanner scanner;
    private final UserInterface userInterface;
    private final UserCreation userCreation;
    private final RegistrationService registration;
    private final Bank bank;

    public AccountCreation(Scanner scanner, UserInterface userInterface) {
        this.scanner = scanner;
        this.userInterface = userInterface;
        this.userCreation = new UserCreation(scanner, userInterface);
        this.registration = new RegistrationService();
        this.bank = Bank.getInstance();
    }

    public void register() {
        System.out.println("Have you used our bank's services before? (y/n)");
        printCursor();
        String answer = scanner.nextLine();

        User user;

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            System.out.println("Enter your user ID: ");
            printCursor();

            String ID = scanner.nextLine();
            user = bank.getUser(ID);

            if (user == null) {
                System.out.println("There is no user with provided ID.");
                userInterface.start();
            } else if (user.getNumberOfOwnedAccounts() == User.MAX_NUMBER_OF_ACCOUNTS) {
                System.out.println("The limit for the maximum number of accounts to be created has been reached");
                userInterface.start();
            }
        } else {
            user = userCreation.createUser();
            bank.addUser(user);
            System.out.println(user);
        }
        Account account = createAccount(user);
        if (registration.register(user, account)) {
            addAccountToBank(account);
        } else System.out.println("Registration process failed.");
    }

    private Account createAccount(User user) {
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
                if (Validation.validateNumber(input)) {
                    initialBalance = new BigDecimal(input);
                    break;
                }
            }
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            return initialBalance;
        }

        return initialBalance;
    }

    private void addAccountToBank(Account account) {
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        FileManipulator.saveTransactionHistoryToFile(account.getTransactionHistory(), account.getAccountNumber());
        FileManipulator.saveTransactionLimitToFile(account);
        FileManipulator.saveDataToFile();
    }
}

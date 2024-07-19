package user_interface;

import accounts.Account;
import accounts.TransactionManager;
import authentication.Authentication;
import currencies.CurrencyFormatter;
import file_manipulation.FileManipulator;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.Address;
import users.Person;
import users.PersonDetail;
import users.User;
import validation.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static transaction.TransactionTypes.*;

public class AccountOwnerPanel extends UserPanel {
    private final User user;
    private final Account account;
    private final UserCreation userCreation;
    private final Authentication authentication;
    private List<Transaction> transactions;
    private final UserInterface userInterface;
    private final TransactionManager transactionManager;

    public AccountOwnerPanel(String ID, Scanner scanner, int accountNumber, UserCreation userCreation, UserInterface userInterface) {
        super(ID, scanner);
        this.user = getUser();
        this.account = getBank().getAccount(accountNumber);
        this.userCreation = userCreation;
        this.authentication = Authentication.getInstance(Authentication.DEFAULT_CREDENTIALS_PATH);
        this.transactions = account.getTransactionHistory();
        this.userInterface = userInterface;
        this.transactionManager = account.getTransactionManager();
    }

    public void initialize() {
        loadFromFile();
        checkDailyUsage();
        checkMonthlyUsage();
        greetings();
        start();
    }

    @Override
    public void start() {
        printInfoMessage();
        printStartMessage();
        UserInterface.printCursor();
        String action = getScanner().nextLine();
        switch (action) {
            case "1" -> deposit();
            case "2" -> withdraw();
            case "3" -> transfer();
            case "4" -> viewHistory();
            case "5" -> settings();
            case "x", "X" -> logout();
        }
    }

    private void printInfoMessage() {
        UserInterface.printBorder();
        System.out.printf("Account number: %32d \n", account.getAccountNumber());
        UserInterface.printBorder();
        System.out.printf("Balance: %40s \n", account.getFormattedBalanceWithCurrency());
        UserInterface.printBorder();
        System.out.printf("Daily usage: %30s/%s\n", account.getDailyUsage(LocalDate.now()), account.getDailyLimit());
        UserInterface.printBorder();
        System.out.printf("Monthly usage: %28s/%s\n", account.getMonthlyUsage(YearMonth.now()), account.getMonthlyLimit());
        UserInterface.printBorder();
    }

    private void logout() {
        // Back to the start panel of application
        userInterface.start();
    }

    private void printStartMessage() {
        System.out.println();
        System.out.println("\nChoose action");
        System.out.println("(1) Deposit");
        System.out.println("(2) Withdraw");
        System.out.println("(3) Transfer");
        System.out.println("(4) View transactions history");
        System.out.println("(5) Settings");
        System.out.println("(X) Log out");
    }

    private void deposit() {
        String action = DEPOSIT.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (input.equalsIgnoreCase("x")) start();
        else if (Validation.validateNumber(input)) {
            amount = new BigDecimal(input);
        } else deposit();
        if (account.deposit(amount)) {
            printBalanceAfterTransaction(DEPOSIT.getOperator(), amount);
            saveToFile();
            start();
        } else {
            printFailedMessage(action);
            start();
        }
    }

    private void withdraw() {
        String action = WITHDRAW.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (input.equalsIgnoreCase("x")) start();
        else if (Validation.validateNumber(input)) {
            amount = new BigDecimal(input);
        } else withdraw();
        if (account.withdraw(amount)) {
            printBalanceAfterTransaction(WITHDRAW.getOperator(), amount);
            saveToFile();
            start();
        } else {
            printFailedMessage(action);
            start();
        }
    }

    private void transfer() {
        String action = TRANSFER.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (input.equalsIgnoreCase("x")) start();
        else if (Validation.validateNumber(input)) {
            amount = new BigDecimal(input);
        } else transfer();

        System.out.print("Enter the account number to which you want to make the transfer: ");
        String accountNumber = getScanner().nextLine();

        if (checkAccountNumber(accountNumber) || accountNumber.length() != 8) {
            transfer();
        }
        if (account.transfer(amount, Integer.parseInt(accountNumber))) {
            printBalanceAfterTransaction(TRANSFER.getOperator(), amount);
            saveToFile();
            start();
        } else {
            printFailedMessage(action);
            start();
        }
    }

    private void settings() {
        System.out.println("(1) Change daily limit");
        System.out.println("(2) Change monthly limit");
        System.out.println("(3) Update personal information");
        System.out.println("(4) Change password");
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> {
                changeDailyLimit();
                start();
            }
            case "2" -> {
                changeMonthlyLimit();
                start();
            }
            case "3" -> {
                updatePersonalInformation();
                start();
            }
            case "4" -> {
                changePassword();
                start();
            }
            case "x", "X" -> start();
            default -> {
                printWrongInputMessage();
                settings();
            }
        }
    }

    private void changeMonthlyLimit() {
        System.out.println("Current monthly transfer limit: " + account.getMonthlyLimit());
        System.out.print("Provide new monthly limit: ");

        String input = getScanner().nextLine();

        if (input.isEmpty()) {
            System.out.print("Provided new monthly limit is empty! Try again.");
            settings();
        }
        if (Validation.validateNumber(input)) account.setMonthlyLimit(new BigDecimal(input));
    }

    private void changeDailyLimit() {
        System.out.println("Current daily transfer limit: " + account.getDailyLimit());
        System.out.print("Provide new daily limit: ");

        String input = getScanner().nextLine();

        if (input.isEmpty()) {
            System.out.print("Provided new daily limit is empty! Try again.");
            settings();
        }
        if (Validation.validateNumber(input)) account.setDailyLimit(new BigDecimal(input));
    }

    private void checkDailyUsage() {
        Map<LocalDate, BigDecimal> dailyUsage = account.getLimitManager().getDailyUsage();
        if (!dailyUsage.isEmpty() && !dailyUsage.containsKey(LocalDate.now())) {
            account.getLimitManager().resetDailyUsage();
            saveToFile();
        }
    }

    private void checkMonthlyUsage() {
        Map<YearMonth, BigDecimal> monthlyUsage = account.getLimitManager().getMonthlyUsage();
        if (!monthlyUsage.isEmpty() && !monthlyUsage.containsKey(YearMonth.now())) {
            account.getLimitManager().resetMonthlyUsage();
            saveToFile();
        }
    }

    private void updatePersonalInformation() {
        Person person = user.getPerson();
        Address address = person.getAddress();
        PersonDetail personDetail = userCreation.changePersonDetail();
        String validatedPersonalDetail = userCreation.validatePersonDetails(personDetail);
        String oldDetail = "";

        switch (personDetail) {
            case FIRST_NAME -> {
                oldDetail = person.getFirstName();
                if (!validatedPersonalDetail.equals(oldDetail)) person.setFirstName(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case LAST_NAME -> {
                oldDetail = person.getLastName();
                if (!validatedPersonalDetail.equals(oldDetail)) person.setLastName(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case STREET_ADDRESS -> {
                oldDetail = address.getStreetAddress();
                if (!validatedPersonalDetail.equals(oldDetail)) address.setStreetAddress(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case CITY -> {
                oldDetail = address.getCity();
                if (!validatedPersonalDetail.equals(oldDetail)) address.setCity(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case COUNTRY -> {
                oldDetail = address.getCountry();
                if (!validatedPersonalDetail.equals(oldDetail)) address.setCountry(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case ZIP_CODE -> {
                oldDetail = address.getZipCode();
                if (!validatedPersonalDetail.equals(oldDetail)) address.setZipCode(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case EMAIL -> {
                oldDetail = person.getEmail();
                if (!validatedPersonalDetail.equals(oldDetail)) person.setEmail(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
            case PHONE_NUMBER -> {
                oldDetail = person.getPhone();
                if (!validatedPersonalDetail.equals(oldDetail)) person.setPhone(validatedPersonalDetail);
                else {
                    printSameDetailMessage();
                    start();
                }
            }
        }

        saveToFile();
        System.out.println(oldDetail + " changed to -> " + validatedPersonalDetail);
    }

    private void selectTimeFrame() {
        System.out.println("Select time frame");
        System.out.println("(1) Day");
        System.out.println("(2) Week");
        System.out.println("(3) Month");
        System.out.println("(4) Year");
        System.out.println("(5) All");
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        LocalDate date = LocalDate.now();

        switch (getScanner().nextLine()) {
            case "1" -> {
                transactionManager.getTransactionsForDay(date, transactions).forEach(transaction -> {
                    UserInterface.printBorder();
                    System.out.println(transaction);
                });
                UserInterface.printBorder();
                viewHistory();
            }
            case "2" -> {
                transactionManager.getTransactionsForWeek(date, transactions).forEach(transaction -> {
                    UserInterface.printBorder();
                    System.out.println(transaction);
                });
                UserInterface.printBorder();
                viewHistory();
            }
            case "3" -> {
                transactionManager.getTransactionsForMonth(date, transactions).forEach(transaction -> {
                    UserInterface.printBorder();
                    System.out.println(transaction);
                });
                UserInterface.printBorder();
                viewHistory();
            }
            case "4" -> {
                transactionManager.getTransactionsForYear(date, transactions).forEach(transaction -> {
                    UserInterface.printBorder();
                    System.out.println(transaction);
                });
                UserInterface.printBorder();
                viewHistory();
            }
            case "5" -> {
                account.getTransactionHistory().forEach(transaction -> {
                    UserInterface.printBorder();
                    System.out.println(transaction);
                });
                UserInterface.printBorder();
                viewHistory();
            }
            case "x", "X" -> start();
        }
    }

    private void viewHistory() {
        System.out.println("(1) Select time frame");
        System.out.println("(2) Sort by amount");
        System.out.println("(3) Sort by type");
        System.out.println("(4) Filter by amount range");
        System.out.println("(5) Filter by type");
        System.out.println("(6) Reset to default (removes filters and change sorting method to default - by date)");
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> selectTimeFrame();
            case "2" -> {
                this.transactions = transactionManager.getTransactionsSortedByAmount(transactions);
                start();
            }
            case "3" -> {
                this.transactions = transactionManager.getTransactionsSortedByType(transactions);
                start();
            }
            case "4" -> {
                this.transactions = getTransactionHistoryFilteredByAmountRange();
                start();
            }
            case "5" -> {
                this.transactions = getTransactionHistoryFilteredByType();
                start();
            }
            case "6" -> {
                this.transactions = account.getTransactionHistory();
                start();
            }
            case "x", "X" -> start();
            default -> {
                printWrongInputMessage();
                viewHistory();
            }
        }
    }

    private List<Transaction> getTransactionHistoryFilteredByType() {
        System.out.println("Choose transaction type: ");
        Arrays.stream(values())
                .forEach(value -> System.out.printf("(%d) %s\n", value.ordinal()+1, value.getName()));
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        TransactionTypes type = null;

        switch (getScanner().nextLine()) {
            case "1" -> type = DEPOSIT;
            case "2" -> type = WITHDRAW;
            case "3" -> type = TRANSFER;
            case "x", "X" -> start();
            default -> {
                printWrongInputMessage();
                getTransactionHistoryFilteredByType();
            }
        }

        return transactionManager.filterTransactionsByType(type);
    }

    private void printWrongInputMessage() {
        System.out.println("Wrong input.");
    }

    private List<Transaction> getTransactionHistoryFilteredByAmountRange() {
        System.out.print("Provide the start of amount range: ");
        String startOfRange = getScanner().nextLine();
        String endOfRange = "";

        if (Validation.validateNumber(startOfRange)) {
            System.out.print("Provide the end of amount range: ");
            endOfRange = getScanner().nextLine();
            if (!Validation.validateNumber(endOfRange)) getTransactionHistoryFilteredByAmountRange();
            else if (new BigDecimal(startOfRange).compareTo(new BigDecimal(endOfRange)) >-1) System.out.println("Start of the amount range must be smaller than end of the amount range.");
        } else getTransactionHistoryFilteredByAmountRange();

        return transactionManager.filterTransactionsByAmountRange(new BigDecimal(startOfRange), new BigDecimal(endOfRange), transactions);
    }

    private void changePassword() {
        char[] oldPassword = System.console().readPassword("Enter your password: ");

        if (authentication.authenticateUser(user.getPerson().getID(), Arrays.toString(oldPassword))) {
            char[] newPassword = System.console().readPassword("Enter new password: ");

            if (newPassword.length < RegistrationService.MIN_ALLOWED_PASSWORD_LENGTH) {
                System.out.println("The new password should consist of at least 6 characters.");
            } else if (Arrays.equals(oldPassword, newPassword)) {
                System.out.println("The new password should be different from the old one.");
            } else {
                char[] repeatedPassword = System.console().readPassword("Repeat new password: ");
                if (Arrays.equals(repeatedPassword, newPassword)) {
                    authentication.addUserCredentials(user.getPerson().getID(), Arrays.toString(newPassword));
                    System.out.println("Password successfully changed.");
                } else System.out.println("The repeated password should be the same.");
            }
        } else {
            System.out.println("Wrong password.");
        }
    }

    private void greetings() {
        System.out.println("\nWelcome " + user.getPerson().getFullName() + ".");
    }

    private void provideAmountMessage(String action) {
        System.out.print("Provide the amount you want to " + action + ": (Type X to cancel) ");
    }

    private void printBalanceAfterTransaction(char operator, BigDecimal amount) {
        System.out.println(operator + " " + CurrencyFormatter.getFormat(account.getCurrencyCode(), amount));
        System.out.println("Balance after transaction: " + account.getFormattedBalanceWithCurrency());
    }

    private void printFailedMessage(String action) {
        System.out.println(action + " process failed.");
    }

    private void printSameDetailMessage() {
        System.out.println("Provided new detail is the same as the current one. No changes has been made.");
    }

    private void loadFromFile() {
        FileManipulator.loadTransactionHistoryFromFile(account.getAccountNumber());
        FileManipulator.loadTransactionLimitFromFile(account);
    }

    private void saveToFile() {
         FileManipulator.saveTransactionHistoryToFile(transactions, account.getAccountNumber());
         FileManipulator.saveTransactionLimitToFile(account);
    }
}

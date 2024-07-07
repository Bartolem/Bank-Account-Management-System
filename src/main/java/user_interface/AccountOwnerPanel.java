package user_interface;

import accounts.*;
import authentication.Authentication;
import currencies.CurrencyFormatter;
import file_manipulation.AccountsToCSV;
import file_manipulation.CSVToTransactionHistory;
import transaction.Transaction;
import transaction.TransactionTypes;
import users.Address;
import users.Person;
import users.PersonDetail;
import users.User;
import validation.NumberValidator;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static transaction.TransactionTypes.*;

public class AccountOwnerPanel extends UserPanel {
    private final User user;
    private final Account account;
    private final UserCreation userCreation;
    private final Authentication authentication;
    private List<Transaction> transactions;
    private final UserInterface userInterface;

    public AccountOwnerPanel(String ID, Scanner scanner, int accountNumber, UserCreation userCreation, UserInterface userInterface) {
        super(ID, scanner);
        this.user = getUser();
        this.account = getBank().getAccount(accountNumber);
        this.userCreation = userCreation;
        this.authentication = Authentication.getInstance();
        this.transactions = account.getTransactionHistory();
        this.userInterface = userInterface;
    }

    @Override
    public void start() {
        loadFromFile();
        greetings();
        System.out.println();
        UserInterface.printBorder();
        System.out.printf("Account number: %32d \n", account.getAccountNumber());
        UserInterface.printBorder();
        System.out.printf("Balance: %40s \n", account.getFormattedBalanceWithCurrency());
        UserInterface.printBorder();
        System.out.println("\nChoose action");
        System.out.println("(1) Deposit");
        System.out.println("(2) Withdraw");
        System.out.println("(3) Transfer");
        System.out.println("(4) View transactions history");
        System.out.println("(5) Settings");
        System.out.println("(X) Log out");
        UserInterface.printCursor();
        String action = getScanner().nextLine();
        switch (action) {
            case "1" -> deposit();
            case "2" -> withdraw();
            case "3" -> transfer();
            case "4" -> viewHistory();
            case "5" -> settings();
            case "x", "X" -> userInterface.start();
        }
    }

    private void deposit() {
        String action = DEPOSIT.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (NumberValidator.validate(input)) {
            amount = new BigDecimal(input);
        } else {
            deposit();
        }
        if (account.deposit(amount)) {
            printBalanceAfterTransaction(DEPOSIT.getOperator(), amount);
            saveToFile();
        } else printFailedMessage(action);
    }

    private void withdraw() {
        String action = WITHDRAW.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (NumberValidator.validate(input)) {
            amount = new BigDecimal(input);
        } else {
            withdraw();
        }
        if (account.withdraw(amount)) {
            printBalanceAfterTransaction(WITHDRAW.getOperator(), amount);
            saveToFile();
        } else printFailedMessage(action);
    }

    private void transfer() {
        String action = TRANSFER.getName();
        provideAmountMessage(action);
        String input = getScanner().nextLine();
        BigDecimal amount = new BigDecimal(0);

        if (NumberValidator.validate(input)) {
            amount = new BigDecimal(input);
        } else {
            transfer();
        }

        System.out.print("Enter the account number to which you want to make the transfer: ");
        String accountNumber = getScanner().nextLine();

        if (checkAccountNumber(accountNumber) || accountNumber.length() != 8) {
            transfer();
        }
        if (account.transfer(amount, Integer.parseInt(accountNumber))) {
            printBalanceAfterTransaction(TRANSFER.getOperator(), amount);
            saveToFile();
        } else printFailedMessage(action);
    }

    private void settings() {
        System.out.println("(1) Change daily limit");
        System.out.println("(2) Change monthly limit");
        System.out.println("(3) Update personal information");
        System.out.println("(4) Change password");
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        switch (getScanner().nextLine()) {
            case "1" -> changeDailyLimit();
            case "2" -> changeMonthlyLimit();
            case "3" -> updatePersonalInformation();
            case "4" -> changePassword();
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
        if (NumberValidator.validate(input)) account.setMonthlyLimit(new BigDecimal(input));
    }

    private void changeDailyLimit() {
        System.out.println("Current daily transfer limit: " + account.getDailyLimit());
        System.out.print("Provide new daily limit: ");

        String input = getScanner().nextLine();

        if (input.isEmpty()) {
            System.out.print("Provided new daily limit is empty! Try again.");
            settings();
        }
        if (NumberValidator.validate(input)) account.setDailyLimit(new BigDecimal(input));
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
                person.setFirstName(validatedPersonalDetail);
            }
            case LAST_NAME -> {
                oldDetail = person.getLastName();
                person.setLastName(validatedPersonalDetail);
            }
            case STREET_ADDRESS -> {
                oldDetail = address.getStreetAddress();
                address.setStreetAddress(validatedPersonalDetail);
            }
            case CITY -> {
                oldDetail = address.getCity();
                address.setCity(validatedPersonalDetail);
            }
            case COUNTRY -> {
                oldDetail = address.getCountry();
                address.setCountry(validatedPersonalDetail);
            }
            case ZIP_CODE -> {
                oldDetail = address.getZipCode();
                address.setZipCode(validatedPersonalDetail);
            }
            case EMAIL -> {
                oldDetail = person.getEmail();
                person.setEmail(validatedPersonalDetail);
            }
            case PHONE_NUMBER -> {
                oldDetail = person.getPhone();
                person.setPhone(validatedPersonalDetail);
            }
        }

        saveToFile();
        System.out.println(oldDetail + " changed to -> " + validatedPersonalDetail);
    }

    private void selectTimeFrame(List<Transaction> transactions) {
        System.out.println("Select time frame");
        System.out.println("(1) Day");
        System.out.println("(2) Week");
        System.out.println("(3) Month");
        System.out.println("(4) Year");
        System.out.println("(5) All");
        System.out.println("(X) Exit");
        UserInterface.printCursor();

        LocalDate date = LocalDate.now();

        while (true) {
            switch (getScanner().nextLine()) {
                case "1" -> {
                    account.getTransactionsForDay(date, transactions).forEach(transaction -> {
                        UserInterface.printBorder();
                        System.out.println(transaction);
                    });
                    UserInterface.printBorder();
                    viewHistory();
                }
                case "2" -> {
                    account.getTransactionsForWeek(date, transactions).forEach(transaction -> {
                        UserInterface.printBorder();
                        System.out.println(transaction);
                    });
                    UserInterface.printBorder();
                    viewHistory();
                }
                case "3" -> {
                    account.getTransactionsForMonth(date, transactions).forEach(transaction -> {
                        UserInterface.printBorder();
                        System.out.println(transaction);
                    });
                    UserInterface.printBorder();
                    viewHistory();
                }
                case "4" -> {
                    account.getTransactionsForYear(date, transactions).forEach(transaction -> {
                        UserInterface.printBorder();
                        System.out.println(transaction);
                    });
                    UserInterface.printBorder();
                    viewHistory();
                }
                case "5" -> {
                    transactions.forEach(transaction -> {
                        UserInterface.printBorder();
                        System.out.println(transaction);
                    });
                    UserInterface.printBorder();
                    viewHistory();
                }
                case "x", "X" -> {
                    return;
                }
            }
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
            case "1" -> selectTimeFrame(transactions);
            case "2" -> {
                this.transactions = account.getTransactionsSortedByAmount(transactions);
                start();
            }
            case "3" -> {
                this.transactions = account.getTransactionsSortedByType(transactions);
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

        return account.filterTransactionsByType(type);
    }

    private void printWrongInputMessage() {
        System.out.println("Wrong input.");
    }

    private List<Transaction> getTransactionHistoryFilteredByAmountRange() {
        System.out.print("Provide the start of amount range: ");
        String startOfRange = getScanner().nextLine();
        String endOfRange = "";

        if (NumberValidator.validate(startOfRange)) {
            System.out.print("Provide the end of amount range: ");
            endOfRange = getScanner().nextLine();
            if (!NumberValidator.validate(endOfRange)) getTransactionHistoryFilteredByAmountRange();
            else if (new BigDecimal(startOfRange).compareTo(new BigDecimal(endOfRange)) >-1) System.out.println("Start of the amount range must be smaller than end of the amount range.");
        } else getTransactionHistoryFilteredByAmountRange();

        return account.filterTransactionsByAmountRange(new BigDecimal(startOfRange), new BigDecimal(endOfRange), transactions);
    }

    private void changePassword() {
        char[] oldPassword = System.console().readPassword("Enter your password: ");

        if (authentication.authenticateUser(user.getPerson().getID(), Arrays.toString(oldPassword))) {
            char[] newPassword = System.console().readPassword("Enter new password: ");

            if (Arrays.equals(oldPassword, newPassword)) {
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
        System.out.print("Provide the amount you want to " + action + ": ");
    }

    private void printBalanceAfterTransaction(char operator, BigDecimal amount) {
        System.out.println(operator + " " + CurrencyFormatter.getFormat(account.getCurrencyCode(), amount));
        System.out.println("Balance after transaction: " + account.getFormattedBalanceWithCurrency());
    }

    private void printFailedMessage(String action) {
        System.out.println(action + " process failed.");
    }

    private void loadFromFile() {
        CSVToTransactionHistory.read(new File("transactions/transaction_history_" + account.getAccountNumber() + ".csv").getAbsolutePath());
    }

    private void saveToFile() {
        AccountsToCSV.write(getBank().getAllAccounts(), "accounts.csv");
    }
}

import java.util.*;

public class UserInterface {
    private final Bank bank;
    private final Scanner scanner;
    private final Validation validation;

    public UserInterface(Scanner scanner, Validation validation) {
        this.bank = Bank.getInstance();
        this.scanner = scanner;
        this.validation = validation;
    }

    public void start() {
        loadDataFromFile();
        System.out.println();
        System.out.println("Welcome to Online Banking Application.");

        loop: while (true) {
            printStartingMessage();
            printCursor();
            switch (scanner.nextLine()) {
                case "1":
                    // Log in to the system
                    break;

                case "2":
                    // Create new account
                    User user = createUser();
                    Account account = createAccount(user);
                    addUserAndAccountToBank(user, account);
                    break;
                case "X":
                case "x":
                    // Exit
                    break loop;
            }
        }

        scanner.close();
        System.exit(0);
    }

    private void printCursor() {
        System.out.print("> ");
    }

    private AccountTypes chooseAccountType() {
        AccountTypes accountType;
        System.out.println("There are several types of accounts to choose from: " + Arrays.toString(AccountTypes.values()));

        loop: while (true) {
            System.out.println("Which one do you choose?");

            for (AccountTypes type : AccountTypes.values()) {
                System.out.println("("+ (type.ordinal() + 1) + ") " + type);
            }

            printCursor();
            String answer = scanner.nextLine();

            switch (answer) {
                case "1":
                    accountType = AccountTypes.STANDARD;
                    break loop;
                case "2":
                    accountType = AccountTypes.SAVINGS;
                    break loop;
                case "3":
                    accountType = AccountTypes.CURRENT;
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

    private void addUserAndAccountToBank(User user, Account account) {
        bank.addUser(user);
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        saveDataToFile();
    }

    private double setInitialBalance() {
        double initialBalance = 0;
        System.out.println("Do you want to provide an initial balance? (y/n)");
        printCursor();

        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            while (true) {
                System.out.println("Initial balance: ");
                printCursor();

                try {
                    initialBalance = Double.parseDouble(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input. Enter only numbers.");
                }
            }
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            return initialBalance;
        }

        return initialBalance;
    }

    private Account createAccount(User user) {
        AccountTypes accountType = chooseAccountType();
        CurrencyCodes currencyCode = chooseCurrency();
        String initialBalance = String.valueOf(setInitialBalance());

        switch (accountType) {
            case STANDARD -> {
                return new Account(user, currencyCode, initialBalance);
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



    private String validatePersonDetails(PersonDetail detail) {
        String input = "";

        while (true) {
            if (detail.equals(PersonDetail.DATE_OF_BIRTH)) {
                System.out.println("Valid date format: yyyy-MM-dd");
            }

            System.out.print(detail + ": ");

            input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Enter the " + detail);
            } else {
                switch (detail) {
                    case DATE_OF_BIRTH -> {
                        if (validation.validateDateOfBirth(input)) return input;
                        else {
                            System.out.println("Invalid " + detail);
                        }
                    }
                    case EMAIL -> {
                        if (validation.validateEmail(input)) return input;
                        else System.out.println("Invalid " + detail);
                    }
                    case PHONE_NUMBER -> {
                        if (validation.validatePhoneNumber(input)) return input;
                        else System.out.println("Invalid " + detail);
                    }
                    default -> {
                        return input;
                    }
                }
            }
        }
    }

    private User createUser() {
        Address address;
        Person person;
        User user;
        ArrayList<String> personDetails = new ArrayList<>();

        printCreateUserMessage();

        for (PersonDetail personDetail : PersonDetail.values()) {
            personDetails.add(validatePersonDetails(personDetail));
        }

        address = new Address(personDetails.get(3), personDetails.get(4), personDetails.get(5), personDetails.get(6));
        person = new Person(personDetails.get(0), personDetails.get(1), personDetails.get(2), address, personDetails.get(7), personDetails.get(8));

        System.out.println("Are you confirm the provided details? (y/n)");
        printCursor();
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            user = new User(person);
            return user;
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            System.out.println("(1) Quit\n(2) Start again\n(3) Correct some details");
            String secondAnswer = scanner.nextLine();

            switch (secondAnswer) {
                case "1":
                    break;
                case "2":
                    createUser();
                    break;
                case "3":
                    System.out.print("Which details you want to correct?: ");
                    // Implement details correcting logic
                    break;
            }
        }

        return null;
    }

    private void printCreateUserMessage() {
        System.out.println("To create new account, you need to provide your details.");
        System.out.println("Such as first name, last name, date of birth, address, e-mail and phone number");
    }

    private void printStartingMessage() {
        System.out.println();
        System.out.println("Choose right option.");
        System.out.println("(1) Open account that is already created.");
        System.out.println("(2) Create an account.");
        System.out.println("(X) Exit.");
    }

    private void printLoginMessage() {
        System.out.println("Enter your user ID and password, to log in.");
    }

    private void loadDataFromFile() {
        CSVToUsers.read(bank, "users.csv");
        CSVToAccounts.read(bank, "accounts.csv");
        CSVToAccountNumber.read(bank.getAccountNumbers(), "account_numbers.csv");
    }

    private void saveDataToFile() {
        UsersToCSV.write(bank.getAllUsers(), "users.csv");
        AccountsToCSV.write(bank.getAllAccounts(), "accounts.csv");
        AccountNumberToCSV.write(bank.getAccountNumbers(), "account_numbers.csv");
    }
}

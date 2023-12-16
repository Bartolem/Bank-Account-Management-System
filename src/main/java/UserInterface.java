import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final Bank bank;
    private final Scanner scanner;

    public UserInterface(Scanner scanner) {
        this.bank = Bank.getInstance();
        this.scanner = scanner;
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
                case "3":
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
        System.out.println("There are several types of accounts to choose from.");
        System.out.println(Arrays.toString(AccountTypes.values()));

        loop: while (true) {
            System.out.println("Which one do you choose?");
            printCursor();
            String answer = scanner.nextLine();

            switch (answer.toUpperCase()) {
                case "STANDARD":
                    accountType = AccountTypes.STANDARD;
                    break loop;
                case "SAVINGS":
                    accountType = AccountTypes.SAVINGS;
                    break loop;
                case "CURRENT":
                    accountType = AccountTypes.CURRENT;
                    break loop;
            }
        }

        return accountType;
    }

    private CurrencyCodes chooseCurrency() {
        CurrencyCodes currencyCode;
        List<CurrencyCodes> currencyCodes = Arrays.asList(CurrencyCodes.values());

        System.out.println("Currently supported currencies: ");
        System.out.println(currencyCodes);
        System.out.println("In which currency?");

        while (true) {
            printCursor();
            String answer = scanner.nextLine();

            if (currencyCodes.contains(CurrencyCodes.valueOf(answer))) {
                currencyCode = CurrencyCodes.valueOf(answer);
                break;
            }
        }
        return currencyCode;
    }

    private void addUserAndAccountToBank(User user, Account account) {
        bank.addUser(user);
        bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        saveDataToFile();
    }

    private Account createAccount(User user) {
        AccountTypes accountType = chooseAccountType();
        CurrencyCodes currencyCode = chooseCurrency();
        String openingBalance = "0";

        switch (accountType) {
            case STANDARD -> {
                return new Account(user, currencyCode, openingBalance);
            }
            case SAVINGS -> {
                return new SavingsAccount(user, currencyCode, openingBalance);
            }
            case CURRENT -> {
                return new CurrentAccount(user, currencyCode,openingBalance);
            }
        }

        return null;
    }

    private String validateDetails(String detail) {
        String input = "";

        while (true) {
            System.out.print(detail + ": ");
            input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Enter the " + detail);
            } else return input;
        }
    }

    private User createUser() {
        Address address;
        Person person;
        User user;
        ArrayList<String> personDetails = new ArrayList<>();
        ArrayList<String> addressDetails = new ArrayList<>();

        printCreateUserMessage();
        personDetails.add(validateDetails("First name"));
        personDetails.add(validateDetails("Last name"));
        personDetails.add(validateDetails("Date of birth"));
        addressDetails.add(validateDetails("Street address"));
        addressDetails.add(validateDetails("City"));
        addressDetails.add(validateDetails("Country"));
        addressDetails.add(validateDetails("Zip code"));
        personDetails.add(validateDetails("E-mail"));
        personDetails.add(validateDetails("Phone number"));

        address = new Address(addressDetails.get(0), addressDetails.get(1), addressDetails.get(2), addressDetails.get(3));
        person = new Person(personDetails.get(0), personDetails.get(1), personDetails.get(2), address, personDetails.get(3), personDetails.get(4));

        System.out.print("Are you confirm the provided details? (y/n) ");
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
        System.out.println("(3) Exit.");
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

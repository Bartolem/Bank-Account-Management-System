import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
        loop: while (true) {
            printStartingMessage();
            printCursor();
            switch (scanner.nextLine()) {
                case "1":
                    // Log int to the system
                    break loop;

                case "2":
                    // Create new account
                    createAccount(createUser());
                    break loop;
            }
        }
    }

    private void printCursor() {
        System.out.print("> ");
    }

    private void createAccount(User user) {
        Account account;
        String answer;
        AccountTypes accountType;
        System.out.println("There are several types of accounts to choose from.");
        System.out.println(Arrays.toString(AccountTypes.values()));

        loop: while (true) {
            System.out.println("Which one do you choose?");
            printCursor();
            answer = scanner.nextLine();

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

        CurrencyCodes currencyCode = CurrencyCodes.EUR;
        String openingBalance = "0";

        switch (accountType) {
            case STANDARD -> account = new Account(user, currencyCode, openingBalance);
            case SAVINGS -> account = new SavingsAccount(user, currencyCode, openingBalance);
        }

        bank.addUser(user);
        //bank.addAccount(account.getAccountNumber(), account, Admin.getInstance());
        saveDataToFile();
    }

    private User createUser() {
        Address address;
        Person person;
        User user;
        ArrayList<String> personDetails = new ArrayList<>();
        ArrayList<String> addressDetails = new ArrayList<>();

        loop: while (true) {
            printCreateUserMessage();
            System.out.print("First name: ");
            personDetails.add(scanner.nextLine());

            System.out.print("Last name: ");
            personDetails.add(scanner.nextLine());

            System.out.print("Date of birth: ");
            personDetails.add(scanner.nextLine());

            System.out.print("Street address: ");
            addressDetails.add(scanner.nextLine());

            System.out.print("City: ");
            addressDetails.add(scanner.nextLine());

            System.out.print("Country: ");
            addressDetails.add(scanner.nextLine());

            System.out.print("Zip code: ");
            addressDetails.add(scanner.nextLine());

            System.out.print("E-mail: ");
            personDetails.add(scanner.nextLine());

            System.out.print("Phone number: ");
            personDetails.add(scanner.nextLine());

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
                        break loop;
                    case "2":
                        break;
                    case "3":
                        System.out.print("Which details you want to correct?: ");
                }
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
        System.out.println("Welcome to Online Banking Application.");
        System.out.println("Choose right option.");
        System.out.println("(1) To open account that is already created.");
        System.out.println("(2) To create an account.");
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

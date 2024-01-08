package user_interface;

import users.Address;
import users.Person;
import users.PersonDetail;
import users.User;
import validation.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static users.PersonDetail.*;

public class UserCreation {
    private final Scanner scanner;
    private final Validation validation;

    public UserCreation(Scanner scanner, Validation validation) {
        this.scanner = scanner;
        this.validation = validation;
    }

    private void printCursor() {
        System.out.print("> ");
    }

    private void printCreateUserMessage() {
        System.out.println("To create new account, you need to provide your details.");
        System.out.println(Arrays.toString(PersonDetail.values()));
    }

    protected User createUser() {
        ArrayList<String> personDetails = new ArrayList<>();

        printCreateUserMessage();

        for (PersonDetail personDetail : PersonDetail.values()) {
            personDetails.add(validatePersonDetails(personDetail));
        }

        return confirmDetails(personDetails);
    }

    private User confirmDetails(ArrayList<String> personDetails) {
        System.out.println("Are you confirm the provided details? (y/n)");
        printCursor();
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            Address address = new Address(personDetails.get(3), personDetails.get(4), personDetails.get(5), personDetails.get(6));
            return new User(new Person(personDetails.get(0), personDetails.get(1), personDetails.get(2), address, personDetails.get(7), personDetails.get(8)));
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            System.out.println("(1) Correct some details\n(2) Start again\n(X) Quit");
            printCursor();
            String secondAnswer = scanner.nextLine();

            switch (secondAnswer) {
                case "1":
                    // Implement details correcting logic
                    PersonDetail correctedDetail = changePersonDetail();
                    int index = correctedDetail.ordinal();
                    String validatedAndCorrectedDetail = validatePersonDetails(correctedDetail);

                    System.out.println("Old value: \"" + personDetails.get(index) + "\" -> " + "New value: \"" + validatedAndCorrectedDetail + "\"");
                    personDetails.set(index, validatedAndCorrectedDetail);
                    confirmDetails(personDetails);
                    break;
                case "2":
                    createUser();
                    break;
                case "X":
                case "x":
                    break;
            }
        }

        return null;
    }

    protected PersonDetail changePersonDetail() {
        System.out.println("Which details you want to change?");
        for (PersonDetail detail : PersonDetail.values()) {
            System.out.println("(" + (detail.ordinal() + 1) + ") " + detail);
        }

        while (true) {
            printCursor();
            String answer = scanner.nextLine();

            switch (answer) {
                case "1" -> {
                    return FIRST_NAME;
                }
                case "2" -> {
                    return LAST_NAME;
                }
                case "3" -> {
                    return DATE_OF_BIRTH;
                }
                case "4" -> {
                    return STREET_ADDRESS;
                }
                case "5" -> {
                    return CITY;
                }
                case "6" -> {
                    return COUNTRY;
                }
                case "7" -> {
                    return ZIP_CODE;
                }
                case "8" -> {
                    return EMAIL;
                }
                case "9" -> {
                    return PHONE_NUMBER;
                }
            }
        }
    }

    protected String validatePersonDetails(PersonDetail detail) {
        while (true) {
            if (detail.equals(DATE_OF_BIRTH)) {
                System.out.println("Valid date format: yyyy-MM-dd");
            }

            System.out.print(detail + ": ");

            String input = scanner.nextLine();

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
                        if (validation.validatePhoneNumber(input)) return input.replaceAll("[^0-9]", "");
                        else System.out.println("Invalid " + detail);
                    }
                    default -> {
                        return input;
                    }
                }
            }
        }
    }
}

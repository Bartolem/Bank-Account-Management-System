package user_interface;

import authentication.Role;
import users.Address;
import users.Person;
import users.PersonDetail;
import users.User;
import validation.Validation;

import java.util.ArrayList;
import java.util.Scanner;

import static users.PersonDetail.*;

public class UserCreation {
    private final Scanner scanner;
    private final UserInterface userInterface;

    public UserCreation(Scanner scanner, UserInterface userInterface) {
        this.scanner = scanner;
        this.userInterface = userInterface;
    }

    private void printCreateUserMessage() {
        System.out.println("To create new account, you need to provide your details. Type (X) to exit.");
    }

    protected User createUser() {
        ArrayList<String> personDetails = new ArrayList<>();

        printCreateUserMessage();

        for (PersonDetail personDetail : PersonDetail.values()) {
            String detail = validatePersonDetails(personDetail);

            if (detail.equalsIgnoreCase("x")) {
                return null;
            } else personDetails.add(detail);
        }

        return confirmDetails(personDetails);
    }

    private User confirmDetails(ArrayList<String> personDetails) {
        System.out.println("Are you confirm the provided details? (y/n)");
        UserInterface.printCursor();
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            Address address = new Address(personDetails.get(3), personDetails.get(4), personDetails.get(5), personDetails.get(6));
            return new User(new Person(personDetails.get(0), personDetails.get(1), personDetails.get(2), address, personDetails.get(7), personDetails.get(8)), Role.ACCOUNT_OWNER);
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            System.out.println("(1) Correct some details\n(2) Start again\n(X) Quit");
            UserInterface.printCursor();
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
            System.out.println("(" + (detail.ordinal() + 1) + ") " + detail.getName());
        }

        while (true) {
            UserInterface.printCursor();
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
            String detailName = detail.getName();
            if (detail.equals(DATE_OF_BIRTH)) {
                System.out.println("Valid date format: yyyy-MM-dd");
            }

            System.out.print(detailName + ": ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("x")) {
                userInterface.start();
            }

            if (input.isEmpty()) {
                System.out.println("Enter the " + detailName);
            } else {
                switch (detail) {
                    case DATE_OF_BIRTH -> {
                        if (Validation.validateDateOfBirth(input)) return input;
                        else {
                            System.out.println("Invalid " + detailName);
                        }
                    }
                    case EMAIL -> {
                        if (Validation.validateEmail(input)) return input;
                        else System.out.println("Invalid " + detailName);
                    }
                    case PHONE_NUMBER -> {
                        if (Validation.validatePhoneNumber(input)) return input.replaceAll("[^0-9]", "");
                        else System.out.println("Invalid " + detailName);
                    }
                    default -> {
                        return input;
                    }
                }
            }
        }
    }
}

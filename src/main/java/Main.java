import user_interface.AccountCreation;
import user_interface.Registration;
import user_interface.UserCreation;
import user_interface.UserInterface;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Registration registration = new Registration();
        UserCreation userCreation = new UserCreation(scanner);
        AccountCreation accountCreation = new AccountCreation(scanner);
        UserInterface userInterface = new UserInterface(scanner, registration, userCreation, accountCreation);

        // Starts the program
        userInterface.start();
    }
}
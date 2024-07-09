import user_interface.UserInterface;

public class Main {
    public static void main(String[] args) {
        // Check for the "-log" argument to enable detailed logging
        boolean loggingEnabled = args.length > 0 && args[0].equals("-log");

        // Initialize user interface and starts the program
        new UserInterface(loggingEnabled).initialize();
    }
}
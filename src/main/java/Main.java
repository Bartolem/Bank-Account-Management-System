import file_manipulation.FileManipulator;
import user_interface.UserInterface;

public class Main {
    public static void main(String[] args) {
        // Check for the "-setup" argument to properly set up the program
        if (args.length > 0 && args[0].equals("-setup")) FileManipulator.createNecessaryDirectoriesAndFiles();

        // Check for the "-log" argument to enable detailed logging
        boolean loggingEnabled = args.length > 0 && args[0].equals("-log");

        // Initialize user interface and starts the program
        new UserInterface(loggingEnabled).initialize();
    }
}
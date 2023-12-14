import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Authentication {
    private final HashMap<String, String> userCredentials;
    private static Authentication authentication;
    private final String fileName;

    private Authentication() {
        this.userCredentials = new HashMap<>();
        this.fileName = "user_credentials.csv";
        loadUserCredentialsFromCSV(fileName);
    }

    public static Authentication getInstance() {
        if (authentication == null) {
            authentication = new Authentication();
        }
        return authentication;
    }

    public void addUserCredentials(String ID, String password) {
        userCredentials.put(ID, hashPassword(password));
        saveUserCredentialsToCSV(userCredentials, fileName);
    }

    public User authenticateUser(String ID, String password) {
        User user = Bank.getInstance().getUser(ID);
        // Checks if user with provided ID exist
        if (user != null) {
            if (userCredentials.get(ID).equals(hashPassword(password))) {
                return user;
            }
        }
        return null;
    }

    // Hashing method for password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // Convert byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveUserCredentialsToCSV(HashMap<String, String> userCredentials, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (HashMap.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
            System.out.println("User credentials saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserCredentialsFromCSV(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

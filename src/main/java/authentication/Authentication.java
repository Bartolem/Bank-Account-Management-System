package authentication;

import bank.Bank;
import logging.LoggerConfig;
import users.User;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Logger;

public class Authentication {
    private static final Logger LOGGER = LoggerConfig.getLogger();
    public static final String DEFAULT_CREDENTIALS_PATH = new File("users/user_credentials.csv").getAbsolutePath();
    private final HashMap<String, String> userCredentials;
    private static Authentication authentication;
    private final String filePath;

    private Authentication(String filePath) {
        this.userCredentials = new HashMap<>();
        this.filePath = filePath;
        loadUserCredentialsFromCSV(filePath);
    }

    // Added for testing purposes
    public static void resetInstance() {
        authentication = null;
    }

    public static Authentication getInstance(String filePath) {
        if (authentication == null) {
            authentication = new Authentication(filePath);
        }
        return authentication;
    }

    public HashMap<String, String> getUserCredentials() {
        return userCredentials;
    }

    public void addUserCredentials(String ID, String password) {
        userCredentials.put(ID, hashPassword(password));
        saveUserCredentialsToCSV(userCredentials, filePath);
    }

    public boolean authenticateUser(String ID, String password) {
        User user = Bank.getInstance().getUser(ID);
        // Checks if user with provided ID exists
        if (user != null) {
            return userCredentials.get(ID).equals(hashPassword(password));
        }
        return false;
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
            throw new RuntimeException(e.getMessage());
        }
    }

    private void saveUserCredentialsToCSV(HashMap<String, String> userCredentials, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (HashMap.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
            LOGGER.finest("User credentials saved to " + fileName);
        } catch (IOException e) {
            LOGGER.severe("Failed to save user credentials to " + fileName + ":" + e.getMessage());
            throw new RuntimeException(e.getMessage());
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
            LOGGER.finest("User credentials successfully loaded from " + fileName);
        } catch (FileNotFoundException e) {
            LOGGER.warning("File not found: " + fileName + ". This may be expected during setup.");
        } catch (IOException e) {
            LOGGER.severe("Failed to load user credentials from " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

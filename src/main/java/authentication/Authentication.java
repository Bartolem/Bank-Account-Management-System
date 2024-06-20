package authentication;

import bank.Bank;
import file_manipulation.LogoLoader;
import users.User;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

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

    public HashMap<String, String> getUserCredentials() {
        return userCredentials;
    }

    public void addUserCredentials(String ID, String password) {
        userCredentials.put(ID, hashPassword(password));
        saveUserCredentialsToCSV(userCredentials, "src/main/resources/" + fileName);
    }

    public boolean authenticateUser(String ID, String password) {
        User user = Bank.getInstance().getUser(ID);
        // Checks if user with provided ID exist
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
            System.out.println("User credentials saved to " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void loadUserCredentialsFromCSV(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Authentication.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

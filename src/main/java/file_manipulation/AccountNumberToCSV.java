package file_manipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AccountNumberToCSV {
    public static void write(ArrayList<Integer> accountNumbers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int number : accountNumbers) {
                String formattedNumber = String.format("%s,", number);
                if (accountNumbers.indexOf(number) == accountNumbers.size()-1) {
                    formattedNumber = String.format("%s", number);
                }
                writer.write(formattedNumber);
            }

            writer.close();
            System.out.println("Account numbers successfully saved to " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

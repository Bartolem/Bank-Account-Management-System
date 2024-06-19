package file_manipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class CSVToAccountNumber {
    public static void read(ArrayList<Integer> accountNumbers, String fileName) {
        try (BufferedReader reader =  new BufferedReader(new InputStreamReader(Objects.requireNonNull(CSVToAccountNumber.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                // Read account numbers detail
                String[] fileContent = line.split(",");

                for (String number : fileContent) {
                    accountNumbers.add(Integer.valueOf(number));
                }
            }
            System.out.println("Account numbers successfully loaded from " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

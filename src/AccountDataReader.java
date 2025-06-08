import java.io.BufferedReader;  // مش مفهوم لسا
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class AccountDataReader {

    public static List<Account_data> readOperationsFromFile(String filename) {
        List<Account_data> operations = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                String accNum = parts[0].trim();
                String type = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                LocalDate date = LocalDate.parse(parts[3].trim(), formatter);

                operations.add(new Account_data(accNum, type, amount, date));
            }

            br.close();
        } catch (Exception e) {
            System.out.println("خطأ أثناء قراءة الملف: " + e.getMessage());
        }

        return operations;
    }

}
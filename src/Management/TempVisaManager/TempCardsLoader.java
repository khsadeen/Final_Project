package Management.TempVisaManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TempCardsLoader {

    public static List<TempVisaCard> loadCards(String filePath) {
        List<TempVisaCard> cards = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String cardNumber = parts[0];
                    String pin = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    boolean isActive = Boolean.parseBoolean(parts[3]);

                    cards.add(new TempVisaCard(cardNumber, pin, balance, isActive));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading temp visa cards: " + e.getMessage());
        }

        return cards;
    }
}


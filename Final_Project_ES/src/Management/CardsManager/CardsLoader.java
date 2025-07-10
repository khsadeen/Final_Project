package Management.CardsManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardsLoader {
    public List<Cards> cardsListing() {
        List<Cards> cards = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/DataBase/cards.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 13) {
                    String cardNumber = parts[0].trim();
                    String cvv = parts[1].trim();
                    String expiryDate = parts[2].trim();
                    String accountNumber = parts[3].trim();
                    String password = parts[4].trim();
                    double balance = Double.parseDouble(parts[5].trim());
                    String ownerName = parts[6].trim();
                    String email = parts[7].trim();
                    String gmailPassword = parts[8].trim();
                    String phone = parts[9].trim();
                    double overDraftLimit = Double.parseDouble(parts[10].trim());
                    String bankBranch = parts[11].trim();
                    String unLocked = parts[12].trim();

                    Cards card = new Cards(cardNumber, cvv, expiryDate, accountNumber,
                            password, balance, ownerName, email, phone, overDraftLimit,gmailPassword,bankBranch,unLocked);
                    cards.add(card);
                }
            }
            reader.close();

            //
            for (Cards c : cards) {
                c.printCardInfo();
            }

        } catch (IOException e) {
            System.out.println("⚠! Error reading the file: " + e.getMessage());
        }
        return cards;
    }
}


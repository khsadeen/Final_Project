package Management.CardsManager;

import java.io.*;

public class CardsUpdater {
    public static void updateCard(Cards updatedCard) {
        File inputFile = new File("src/DataBase/cards.txt");
        File tempFile = new File("src/DataBase/cards_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 13 && parts[0].trim().equals(updatedCard.cardNumber)) {
                    // same card, writing the data again with the changes
                    writer.println(updatedCard.cardNumber + "," +
                            updatedCard.cvv + "," +
                            updatedCard.expiryDate + "," +
                            updatedCard.accountNumber + "," +
                            updatedCard.password + "," +
                            updatedCard.balance + "," +
                            updatedCard.ownerName + "," +
                            updatedCard.email + "," +
                            updatedCard.gmailPassword + "," +
                            updatedCard.phone + "," +
                            updatedCard.overDraftLimit+","+updatedCard.bankBranch+","+updatedCard.unLocked
                    );
                } else {
                    // to write the line as it was
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error updating the file: " + e.getMessage());
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            System.out.println("⚠! Could not replace original file.");
        }
    }
}

package Management.TempVisaManager;

import GUIjavaSwing.Elements;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;
import Management.CardsManager.Cards;
import Management.SignInManager.StandardLoginGUI;
import Management.SignInManager.VisaCardInput;

import javax.imageio.ImageIO; //g2d
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;//draw on image
import java.io.*;  // BR/W, FR/W
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class TempVisaCardCreator {
    public static JFrame frame;
    public JTextField pinField;
    public JLabel statusLabel;

    private List<String> existingCardNumbers = new ArrayList<>();
    private List<String> existingPins = new ArrayList<>();
    private String filename = "src/DataBase/temporary_visa_cards.txt";
    public int attemptsLeft = 3;

    public TempVisaCardCreator() {
        loadExistingCards();
        frame = new JFrame("Create Temporary Visa Card");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Elements.BEIGE);

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image img = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setBorder(BorderFactory.createEmptyBorder(15,0,40,0));
        } catch (Exception e) {
            imageLabel.setText("⚠️ Logo");
            imageLabel.setForeground(Elements.SEAWEED);
        }

        JLabel titleLabel = new JLabel("Generated Visa Card");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel pinLabel = new JLabel("Enter PIN (6 digits):");
        pinLabel.setFont(new Font("Ariel",Font.PLAIN,16));
        pinLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinLabel.setForeground(Elements.SEAWEED);

        pinField = new JTextField(12);
        pinField.setMaximumSize(new Dimension(200, 30));
        pinField.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createButton = new JButton("Create Card 🟦");
        createButton.setFont(new Font("Ariel",Font.BOLD,15));
        createButton.setBorder(new RoundedBorder(12));
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setFocusPainted(false);
        createButton.setBackground(Elements.SEAWEED);
        createButton.setForeground(Elements.CHAMPAGNE);
        createButton.addActionListener(e -> handlePinEntry());


        JButton backButton = new JButton("Back",Elements.resizedBackIcon);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);


        backButton.setFont(new Font("Ariel",Font.BOLD,14));
        backButton.setBorder(new RoundedBorder(12));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(Elements.SOFT_GREEN);
        backButton.setForeground(Elements.CHAMPAGNE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            frame.dispose();
            MainMenu.showTempVisaOptions();
        });

        statusLabel = new JLabel("Fill in your PIN to generate card");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setBackground(Elements.BEIGE);
        statusLabel.setForeground(Elements.SEAWEED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        mainPanel.add(imageLabel);
        mainPanel.add(titleLabel);
        mainPanel.add(pinLabel);
        mainPanel.add(pinField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(backButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void handlePinEntry() {
        String enteredPin = pinField.getText().trim();

        if (!enteredPin.matches("\\d{6}")) { // if doesn't match, -attempt
            attemptsLeft--;
            statusLabel.setText("X Invalid format. Must be 6 digits. Attempts left: " + attemptsLeft);
        } else if (existingPins.contains(enteredPin)) { //if already exists, -attempt
            attemptsLeft--;
            statusLabel.setText("X PIN already exists. Attempts left: " + attemptsLeft);
        } else {
            createCard(enteredPin);
            return;
        }

        if (attemptsLeft <= 0) { //we generate one, if attempts = 0
            String generatedPin = generateUniquePin();
            statusLabel.setText("YOUR GENERATED PIN: " + generatedPin + " | ATTEMPTS: 0");
            JOptionPane.showMessageDialog(frame,
                    "YOUR GENERATED PIN: " + generatedPin + "\nATTEMPTS: 0",
                    "PIN Generated",
                    JOptionPane.INFORMATION_MESSAGE);
            createCard(generatedPin);
        }
    }
//===============================================================================================
    private void createCard(String pin) {
        String cardNumber = generateUniqueCardNumber();
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(cardNumber + "," + pin + ",0.0,false\n");
            existingCardNumbers.add(cardNumber);
            existingPins.add(pin);
            statusLabel.setText("√ Card created successfully!");

            statusLabel.setBackground(Elements.BEIGE);
            statusLabel.setForeground(Elements.SEAWEED);

            TempVisaCard tempCard = new TempVisaCard(cardNumber, pin, 0.0, false);
            showPaymentOptions(tempCard);
        } catch (IOException e) {
            statusLabel.setText("X Failed to save card.");
        }
    }
    public static void deleteCardLineFromFile(String cardNumberToDelete) { //edit by replacement
        String filePath = "src/DataBase/temporary_visa_cards.txt";
        File inputFile = new File(filePath);
        File tempFile = new File(filePath + "_temp");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(cardNumberToDelete + ",")) {
                    found = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

            if (!found) {
                System.out.println("⚠️ Card number not found: " + cardNumberToDelete);
            }

        } catch (IOException e) {
            System.out.println("❌ Error deleting line: " + e.getMessage());
            return;
        }
        if (inputFile.delete() && tempFile.renameTo(inputFile)) {
            System.out.println("✅ Line deleted successfully.");
        } else {
            System.out.println("❌ Failed to replace original file.");
        }
    }

//==================================================================================================
    public static void showCardReadyScreen(String cardNumber) {
        JFrame readyFrame = new JFrame("Card Ready");
        readyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        readyFrame.setSize(400, 500);
        readyFrame.setLocationRelativeTo(null);

        BufferedImage finalImage = generateCardImageWithNumber(cardNumber);
        if (finalImage == null) {
            JOptionPane.showMessageDialog(frame, "⚠️ Failed to load card image.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Image scaled = finalImage.getScaledInstance(510, 360, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel commentLabel = new JLabel("YOUR CARD IS READY!");
        commentLabel.setBackground(Elements.BEIGE);
        commentLabel.setFont(new Font("Arial", Font.BOLD, 30));
        commentLabel.setForeground(Elements.SEAWEED);
        commentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        commentLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(imageLabel);
        panel.add(commentLabel);
        panel.add(Box.createVerticalGlue());

        readyFrame.add(panel);
        readyFrame.setVisible(true);

        final int amplitude = 15;
        final int delay = 30;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            long time = System.currentTimeMillis();
            int offsetY = (int) (amplitude * Math.sin(time / 200.0));
            imageLabel.setBorder(BorderFactory.createEmptyBorder(offsetY + amplitude, 0, 0, 0));
            panel.revalidate();
            panel.repaint();
        });
        timer.start();
    }

    public static BufferedImage generateCardImageWithNumber(String cardNumber) {
        Color GOLD = new Color(0xBDA868);
        try {
            BufferedImage original = ImageIO.read(new File("src/assets/VisaCard.png"));
            BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = copy.createGraphics();

            g2d.drawImage(original, 0, 0, null);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = new Font("SansSerif", Font.BOLD, 24);
            g2d.setFont(font);
            g2d.setColor(GOLD);
            //card number
            String formatted = cardNumber.replaceAll(".{4}(?!$)", "$0 ");//it replaces the $0 with the card digits
            FontMetrics metrics = g2d.getFontMetrics(font);
            int textWidth = metrics.stringWidth(formatted);
            int textHeight = metrics.getHeight();
            //where we want the card number in the screen
            int x = (copy.getWidth() - 2 - textWidth) / 2;
            int y = (copy.getHeight() - 2 - textHeight) / 2 + metrics.getAscent() + 20;

            g2d.drawString(formatted, x, y);//card num
            //same as card num for balance>0
            String additionalText = "Balance > 0";
            Font smallFont = new Font("SansSerif", Font.BOLD, 15);
            g2d.setFont(smallFont);
            g2d.setColor(GOLD);

            FontMetrics smallMetrics = g2d.getFontMetrics(smallFont);
            int addTextWidth = smallMetrics.stringWidth(additionalText);
            int addX = (copy.getWidth() - addTextWidth) / 2 + 36;
            int addY = y + 27;

            g2d.drawString(additionalText, addX, addY);

            g2d.dispose();
            return copy; //the image after drawing on it...
        } catch (IOException e) {
            System.out.println("❌ Error generating image: " + e.getMessage());
            return null; //if the path isn't right...
        }
    }

    private void loadExistingCards() { //temp cards loader... to check if card pass exists/ make unique card num and pin
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    existingCardNumbers.add(parts[0]); //add to array list
                    existingPins.add(parts[1]); //add to pins array list
                }
            }
        } catch (IOException e) {
            System.out.println("File loading error: " + e.getMessage());
        }
    }
//==================================================================================================
    private String generateUniqueCardNumber() { //using do while to make sure it's un CN
        String cardNumber;
        do {
            cardNumber = generateRandomDigits(16);
        } while (existingCardNumbers.contains(cardNumber));
        return cardNumber;
    }

    private String generateUniquePin() {//same as card num
        String pin;
        do {
            pin = generateRandomDigits(6);
        } while (existingPins.contains(pin));
        return pin;
    }

    private String generateRandomDigits(int length) {//length = number of digits
        Random rand = new Random(); //gives us a random number
        StringBuilder sb = new StringBuilder();//to build the string char by char
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10)); //number between 0-9
        }
        return sb.toString();//from string builder to string
    }
//===================================================================================================
    private void showPaymentOptions(TempVisaCard Card) {
        String[] options = {"Visa Card Input 💳", "Standard Sign In 🔐"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose a way to transfer money to your Visa card 💳",
                "Payment Method",
                JOptionPane.DEFAULT_OPTION, //in the small screen__> make buttons by default
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) { // trans from real card by two ways(visa card input 0, standard sign in 1)
            frame.dispose();
            Cards sourceCard = VisaCardInput.showVisaInput();
            if (sourceCard != null) {
                new TransferToTempCard(sourceCard,Card.cardNumber);
            }
        } else if (choice == 1) {
            frame.dispose();
            Cards sourceCard = StandardLoginGUI.showStandardLogin();
            if (sourceCard != null) {
                new TransferToTempCard(sourceCard,Card.cardNumber);
            }
        }
    }
//=============================================================================================================
public static void archiveZeroBalanceCard(TempVisaCard tempCard) {
    String archivePath = "src/DataBase/temporary_visa_cards_history.txt";

    try (
            FileWriter fw = new FileWriter(archivePath, true); // append mode> to the end of the file-->last line
            BufferedWriter bw = new BufferedWriter(fw) //line by line
    ) {
        String cardNumber = tempCard.cardNumber; //no changes
        String pin = tempCard.pin;

        String line = cardNumber + "," + pin + ",0.0,false";
        bw.write(line);
        bw.newLine();
        System.out.println("✅ Archived zero-balance card: " + cardNumber);
    } catch (IOException e) {
        System.out.println("❌ Failed to archive card: " + e.getMessage());
    }
}
}
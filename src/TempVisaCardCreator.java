import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class TempVisaCardCreator {

    private static JFrame frame;
    private JTextField pinField;
    private JLabel statusLabel;

    private Set<String> existingCardNumbers = new HashSet<>();
    private Set<String> existingPins = new HashSet<>();
    private String filename = "temporary_visa_cards.txt";
    private int attemptsLeft = 3;

    public TempVisaCardCreator() {
        loadExistingCards();

        frame = new JFrame("Create Temporary Visa Card");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 520);
        frame.setLocationRelativeTo(null);

        Color darkBrown = new Color(68, 16, 10);
        Color gold = new Color(155, 124, 68);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(darkBrown);

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("⚠️ Logo");
            imageLabel.setForeground(Color.WHITE);
        }

        JLabel titleLabel = new JLabel("Generated Visa Card 🧾");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(gold);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel pinLabel = new JLabel("Enter PIN (6 digits):");
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinLabel.setForeground(Color.WHITE);

        pinField = new JTextField(12);
        pinField.setMaximumSize(new Dimension(200, 30));
        pinField.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createButton = new JButton("Create Card 🟦");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setFocusPainted(false);
        createButton.setBackground(gold);
        createButton.setForeground(darkBrown);
        createButton.addActionListener(e -> handlePinEntry());

        JButton backButton = new JButton("⬅ Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(darkBrown);
        backButton.setForeground(gold);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            frame.dispose();
            MainMenu.showTempVisaOptions();
        });

        statusLabel = new JLabel("Fill in your PIN to generate card");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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

        if (!enteredPin.matches("\\d{6}")) {
            attemptsLeft--;
            statusLabel.setText("❌ Invalid format. Must be 6 digits. Attempts left: " + attemptsLeft);
        } else if (existingPins.contains(enteredPin)) {
            attemptsLeft--;
            statusLabel.setText("❌ PIN already exists. Attempts left: " + attemptsLeft);
        } else {
            createCard(enteredPin);
            return;
        }

        if (attemptsLeft <= 0) {
            String generatedPin = generateUniquePin();
            statusLabel.setText("<html><b>YOUR GENERATED PIN:</b> " + generatedPin + "<br>ATTEMPTS: 0</html>");
            JOptionPane.showMessageDialog(frame,
                    "YOUR GENERATED PIN: " + generatedPin + "\nATTEMPTS: 0",
                    "PIN Generated",
                    JOptionPane.INFORMATION_MESSAGE);
            createCard(generatedPin);
        }
    }

    private void createCard(String pin) {
        String cardNumber = generateUniqueCardNumber();
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(cardNumber + "," + pin + ",0.0,true\n");
            existingCardNumbers.add(cardNumber);
            existingPins.add(pin);
            statusLabel.setText("✅ Card created successfully!");
        } catch (IOException e) {
            statusLabel.setText("❌ Failed to save card.");
        }

        showPaymentOptions(cardNumber);
    }

    public static void showCardReadyScreen(String cardNumber) {
        JFrame readyFrame = new JFrame("Card Ready");
        readyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        readyFrame.setSize(400, 400);
        readyFrame.setLocationRelativeTo(null);

        BufferedImage finalImage = generateCardImageWithNumber(cardNumber);
        if (finalImage == null) {
            JOptionPane.showMessageDialog(frame, "⚠️ Failed to load card image.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Image scaled = finalImage.getScaledInstance(345, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel commentLabel = new JLabel("YOUR CARD IS READY!");
        commentLabel.setFont(new Font("Arial", Font.BOLD, 30));
        commentLabel.setForeground(new Color(119, 3, 3));
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
        try {
            BufferedImage original = ImageIO.read(new File("src/project.Images/Flexi ATM Gold Credit Card.png"));
            BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = copy.createGraphics();

            g2d.drawImage(original, 0, 0, null);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = new Font("SansSerif", Font.BOLD, 56);
            g2d.setFont(font);
            g2d.setColor(new Color(219, 190, 124));

            String formatted = cardNumber.replaceAll(".{4}(?!$)", "$0 ");
            FontMetrics metrics = g2d.getFontMetrics(font);
            int textWidth = metrics.stringWidth(formatted);
            int textHeight = metrics.getHeight();

            int x = (copy.getWidth() - 2 - textWidth) / 2;
            int y = (copy.getHeight() - 2 - textHeight) / 2 + metrics.getAscent() + 30;

            g2d.drawString(formatted, x, y);

            String additionalText = "Balance > 0";
            Font smallFont = new Font("SansSerif", Font.PLAIN, 40);
            g2d.setFont(smallFont);
            g2d.setColor(new Color(219, 190, 124));

            FontMetrics smallMetrics = g2d.getFontMetrics(smallFont);
            int addTextWidth = smallMetrics.stringWidth(additionalText);
            int addX = (copy.getWidth() - addTextWidth) / 2 + 60;
            int addY = y + 85;

            g2d.drawString(additionalText, addX, addY);

            g2d.dispose();
            return copy;
        } catch (IOException e) {
            System.out.println("❌ Error generating image: " + e.getMessage());
            return null;
        }
    }

    private void loadExistingCards() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    existingCardNumbers.add(parts[0]);
                    existingPins.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("File loading error: " + e.getMessage());
        }
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateRandomDigits(16);
        } while (existingCardNumbers.contains(cardNumber));
        return cardNumber;
    }

    private String generateUniquePin() {
        String pin;
        do {
            pin = generateRandomDigits(6);
        } while (existingPins.contains(pin));
        return pin;
    }

    private String generateRandomDigits(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private void showPaymentOptions(String tempCardNumber) {
        String[] options = {"Visa Card Input 💳", "Standard Sign In 🔐"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose a way to transfer money to your Visa card 💳",
                "Payment Method",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            frame.dispose();
            Cards sourceCard = VisaCardInput.showVisaInput();
            if (sourceCard != null) {
                new TransferToTempCard(sourceCard, tempCardNumber);
            }
        } else if (choice == 1) {
            frame.dispose();
            Cards sourceCard = StandardLoginGUI.showStandardLogin();
            if (sourceCard != null) {
                new TransferToTempCard(sourceCard, tempCardNumber);
            }
        }
    }

    public void removeZeroBalanceCards() {
        File file = new File(filename);
        File tempFile = new File("temp_" + filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        double balance = Double.parseDouble(parts[2]);
                        if (balance > 0.0) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }

            if (file.delete() && tempFile.renameTo(file)) {
                System.out.println("✅ Zero-balance cards removed.");
            } else {
                System.out.println("❌ Failed to update the card file.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error processing the card file: " + e.getMessage());
        }
    }

    public static void TempVisaCardShow() {
        SwingUtilities.invokeLater(TempVisaCardCreator::new);
    }
}

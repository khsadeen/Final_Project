package Features;

import GUIjavaSwing.Elements;
import GUIjavaSwing.RoundedBorder;
import Management.TempVisaManager.TempVisaCard;
import Main.MainMenu;
import Management.TempVisaManager.TempVisaCardCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TempVisaWithDraw {

    public static void show(TempVisaCard card) {
        JFrame frame = new JFrame("Withdraw from Temporary Visa");
        frame.setSize(400, 620);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("src/assets/logo.png");
        Image scaled = icon.getImage().getScaledInstance(180, 160, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel status = new JLabel("Choose or enter amount to withdraw", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 16));
        status.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Buttons for quick amounts
        JButton btn100 = createStyledButton("100₪", Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn200 = createStyledButton("200₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn500 = createStyledButton("500₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn1000 = createStyledButton("1000₪",Elements.SEAWEED,Elements.CHAMPAGNE);

        ActionListener withdrawFixedAmount = e -> {
            String text = ((JButton) e.getSource()).getText().replace("₪", "").trim();
            double amount = Double.parseDouble(text);
            processWithdrawal(card, amount, status);
            frame.dispose();
            MainMenu.showTempVisaOptions();
        };

        btn100.addActionListener(withdrawFixedAmount);
        btn200.addActionListener(withdrawFixedAmount);
        btn500.addActionListener(withdrawFixedAmount);
        btn1000.addActionListener(withdrawFixedAmount);

        // Custom input
        JTextField amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton withdrawBtn = createStyledButton("Withdraw",Elements.SEAWEED,Elements.CHAMPAGNE);
        withdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                processWithdrawal(card, amount, status);
                frame.dispose();
                MainMenu.screen();
            } catch (NumberFormatException ex) {
                status.setText("❌ Invalid amount.");
            }
        });

        JButton backBtn = createStyledButton("Back",Elements.SEAWEED,Elements.CHAMPAGNE);
        ImageIcon backIcon = new ImageIcon("src/assets/back.png");
        Image scaledBackImg = backIcon.getImage().getScaledInstance(50, 25, Image.SCALE_SMOOTH);
        ImageIcon resizedBackIcon = new ImageIcon(scaledBackImg);
        backBtn.setIcon(resizedBackIcon);
        backBtn.setVerticalTextPosition(SwingConstants.CENTER);
        backBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        backBtn.addActionListener(e -> {
            frame.dispose();
            MainMenu.showTempVisaOptions();
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(Elements.BEIGE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        buttonPanel.add(btn100);
        buttonPanel.add(btn200);
        buttonPanel.add(btn500);
        buttonPanel.add(btn1000);

        // Main layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Elements.BEIGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel amountLabel = new JLabel("Or enter amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 15));
        amountLabel.setForeground(Color.BLACK);
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logo);
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(amountLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(amountField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(withdrawBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backBtn);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(Elements.BEIGE);
        frame.setVisible(true);
    }

    private static void processWithdrawal(TempVisaCard card, double amount, JLabel status) {
        if (amount <= 0) {
            status.setText("❌ Enter positive amount.");
        } else if (amount > card.balance) {
            status.setText("❌ Insufficient balance.");
        } else {
            card.balance -= amount;
            if(card.balance == 0){
                card.isActive = false;
                updateCardInFile(card);
                TempVisaCardCreator.archiveZeroBalanceCard(card);
                TempVisaCardCreator.deleteCardLineFromFile(card.cardNumber);
            }
            else {
                updateCardInFile(card);
            }
            status.setText("✅ Withdrawal successful.");
            JOptionPane.showMessageDialog(null,
                    "You withdrew: ₪" + amount + "\nRemaining: ₪" + card.balance+
                    "\n"+"Success!");
        }
    }

    private static void updateCardInFile(TempVisaCard updatedCard) {
        File file = new File("src/DataBase/temporary_visa_cards.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(updatedCard.cardNumber)) {
                    updatedCard.cardNumber = parts[0];
                    updatedCard.pin=parts[1];
                    updatedLines.add(updatedCard.cardNumber + "," + updatedCard.pin + "," + updatedCard.balance + "," + updatedCard.isActive);
                } else {
                    updatedLines.add(line);
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("❌ Error updating card file: " + e.getMessage());
        }
    }

    public static JButton createStyledButton(String name, Color bg, Color fg) {
        JButton button = new JButton(name);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new RoundedBorder(12));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(150, 40));
        return button;
    }
}

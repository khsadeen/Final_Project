import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TransferToTempCard {

    public TransferToTempCard(Cards sourceCard, String tempCardNumber) {
        Color darkBrown = new Color(244, 250, 255);
        Color gold = new Color(151, 23, 23);

        JFrame frame = new JFrame("💳 Transfer to Temporary Visa Card");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(darkBrown);

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoPanel.setBackground(darkBrown);

        try {
            ImageIcon icon = new ImageIcon("src/project.Images/" +
                    "Welcome.png");
            Image img = icon.getImage().getScaledInstance(350, 235, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(imgLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel fallbackLabel = new JLabel("⚠️ Logo not found");
            fallbackLabel.setForeground(darkBrown);
            fallbackLabel.setForeground(gold);
            fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(fallbackLabel, BorderLayout.CENTER);
        }

        frame.add(logoPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(darkBrown);

        JLabel label = new JLabel("Choose transfer amount:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(gold);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setBackground(darkBrown);
        statusLabel.setForeground(gold);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton selectAmountBtn = new JButton("💰 Select Amount");
        JButton cancelBtn = new JButton("❌ Cancel");

        for (JButton btn : new JButton[]{selectAmountBtn, cancelBtn}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            btn.setPreferredSize(new Dimension(200, 40));
        }

        selectAmountBtn.setBackground(gold);
        selectAmountBtn.setForeground(darkBrown);

        cancelBtn.setBackground(darkBrown);
        cancelBtn.setForeground(gold);
        cancelBtn.setBorder(BorderFactory.createLineBorder(gold, 2));

        selectAmountBtn.addActionListener(e -> {
            new WithDraw(frame, amount -> {
                if (amount <= 0) {
                    statusLabel.setText("❌ Invalid amount.");
                    return;
                }

                double available = sourceCard.balance + sourceCard.overDraftLimit;
                if (amount > available) {
                    JOptionPane.showMessageDialog(frame, "⚠️ Not enough balance.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Transfer $" + amount + " to your temporary Visa card?\nThis cannot be undone.",
                        "Confirm Transfer", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                sourceCard.balance -= amount;
                CardsUpdater.updateCard(sourceCard);

                try {
                    File file = new File("temporary_visa_cards.txt");
                    List<String> lines = new ArrayList<>();
                    boolean found = false;

                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4 && parts[0].equals(tempCardNumber)) {
                            double oldBalance = Double.parseDouble(parts[2]);
                            double newBalance = oldBalance + amount;
                            lines.add(parts[0] + "," + parts[1] + "," + newBalance + "," + parts[3]);
                            found = true;
                        } else {
                            lines.add(line);
                        }
                    }
                    reader.close();

                    if (!found) {
                        JOptionPane.showMessageDialog(frame, "❌ Temporary card not found.");
                        return;
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    for (String l : lines) {
                        writer.write(l);
                        writer.newLine();
                    }
                    writer.close();

                    JOptionPane.showMessageDialog(frame, "✅ Transfer successful!");
                    frame.dispose();
                    TempVisaCardCreator.showCardReadyScreen(tempCardNumber);

                } catch (IOException ex) {
                    statusLabel.setText("❌ File error.");
                }
            });
        });

        cancelBtn.addActionListener(e -> frame.dispose());

        centerPanel.add(label);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(selectAmountBtn);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(cancelBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(statusLabel);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

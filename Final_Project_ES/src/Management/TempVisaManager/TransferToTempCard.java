package Management.TempVisaManager;

import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Management.CardsManager.CardsUpdater;
import Features.WithDraw;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
//to transfer money to a temp card while creating
public class TransferToTempCard {

    public TransferToTempCard(Cards sourceCard, String tempCardNumber) {

        JFrame frame = new JFrame("💳 Transfer to Temporary Visa Card");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Elements.BEIGE); //the basic screen

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoPanel.setBackground(Elements.BEIGE);

        try {
            ImageIcon icon = new ImageIcon("src/assets/" +
                    "logo.png");
            Image img = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(imgLabel, BorderLayout.CENTER);
            logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        } catch (Exception e) {
            JLabel fallbackLabel = new JLabel("⚠! Logo not found");
            fallbackLabel.setForeground(Elements.CHAMPAGNE);
            fallbackLabel.setForeground(Elements.SEAWEED);
            fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(fallbackLabel, BorderLayout.CENTER);
        }

        frame.add(logoPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Elements.BEIGE);

        JLabel label = new JLabel("Choose transfer amount:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Elements.SEAWEED);
        label.setFont(new Font("Ariel", Font.BOLD, 18));

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setBackground(Elements.BEIGE);
        statusLabel.setForeground(Elements.SEAWEED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton selectAmountBtn = new JButton("Select Amount ₪");
        JButton cancelBtn = new JButton("Cancel X");

        for (JButton btn : new JButton[]{selectAmountBtn, cancelBtn}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Ariel", Font.BOLD, 16));
            btn.setPreferredSize(new Dimension(200, 40));
        }

        selectAmountBtn.setBackground(Elements.SEAWEED);
        selectAmountBtn.setForeground(Elements.CHAMPAGNE);

        cancelBtn.setBackground(Elements.SEAWEED);
        cancelBtn.setForeground(Elements.CHAMPAGNE);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Elements.SEAWEED, 2));

        selectAmountBtn.addActionListener(e -> {
            new WithDraw(frame, amount -> {
                if (amount <= 0) {
                    statusLabel.setText("X Invalid amount.");
                    return;
                }

                double available = sourceCard.balance + sourceCard.overDraftLimit;
                if (amount > available) {
                    JOptionPane.showMessageDialog(frame, "⚠! Not enough balance.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(frame, //INT(YES = 0, NO = 1, CANCEL = 2) //inside the small white window
                        "Transfer ₪" + amount + " to your temporary Visa card?\nThis cannot be undone.",
                        "Confirm Transfer", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return; //stop the process

                sourceCard.balance -= amount;
                CardsUpdater.updateCard(sourceCard);

                try {
                    File file = new File("src/DataBase/temporary_visa_cards.txt"); //edit inside the temp file
                    List<String> lines = new ArrayList<>();
                    boolean found = false;
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4 && parts[0].equals(tempCardNumber)) {
                            double oldBalance = Double.parseDouble(parts[2]);
                            double newBalance = oldBalance + amount;
                            boolean isActive = Boolean.parseBoolean(parts[3]);
                            if(newBalance>0){
                                isActive = true;
                            }
                            else {
                                TempVisaCardCreator.deleteCardLineFromFile(tempCardNumber);
                            }
                            lines.add(parts[0] + "," + parts[1] + "," + newBalance + "," + isActive);
                            found = true;
                        } else {
                            lines.add(line);
                        }
                    }
                    reader.close();

                    if (!found) {
                        JOptionPane.showMessageDialog(frame, "X Temporary card not found.");
                        return;
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    for (String l : lines) {
                        writer.write(l);
                        writer.newLine();
                    }
                    writer.close();

                    JOptionPane.showMessageDialog(frame, "√ Transfer successful!");
                    frame.dispose();
                    TempVisaCardCreator.showCardReadyScreen(tempCardNumber);

                } catch (IOException ex) {
                    statusLabel.setText("X File error.");
                }
            });
        });

        cancelBtn.addActionListener(e ->{ frame.dispose();
         TempVisaCardCreator.deleteCardLineFromFile(tempCardNumber);
        });

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

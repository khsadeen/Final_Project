package Management.TempVisaManager;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;
import GUIjavaSwing.Elements;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TempVisaLogin {
    private static TempVisaCard authenticatedCard = null;

    public static TempVisaCard showLogin() {
        List<TempVisaCard> cards = TempCardsLoader.loadCards("src/DataBase/temporary_visa_cards.txt");

        JDialog dialog = new JDialog((Frame) null, "Temporary Visa Login", true);
        dialog.setSize(420, 600);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // Components
        JTextField cardField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        JLabel status = new JLabel("Enter your card details", SwingConstants.CENTER);
        status.setForeground(Color.BLACK);
        status.setFont(new Font("Arial", Font.BOLD, 16));
        status.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton login = new JButton("Login");
        login.setBorder(new RoundedBorder(12));
        login.setBackground(Elements.SEAWEED);
        login.setForeground(Elements.CHAMPAGNE);
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.setMaximumSize(new Dimension(175, 50));
        login.setFont(new Font("Arial",Font.BOLD,16));


        JButton backButton = new JButton("Back",Elements.resizedBackIcon);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        backButton.setBorder(new RoundedBorder(12));


        backButton.setBackground(Elements.SOFT_GREEN);
        backButton.setForeground(Elements.CHAMPAGNE);
        backButton.setBorder(new RoundedBorder(12));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(170, 40));
        backButton.setFont(new Font("Arial",Font.BOLD,16));

        // Load image
        ImageIcon visaIcon = new ImageIcon("src/assets/logo.png");
        Image scaledImage = visaIcon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action listeners
        login.addActionListener(e -> {
            String card = cardField.getText();
            String pin = new String(pinField.getPassword());

            for (TempVisaCard c : cards) {
                if (c.cardNumber.equals(card) && c.pin.equals(pin)) {
                    authenticatedCard = c;
                    JOptionPane.showMessageDialog(dialog, "✅ Access granted!");
                    dialog.dispose();
                    return;
                }
            }
            status.setText("❌ Invalid card number or PIN, try again!");
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.showTempVisaOptions();
        });

        // Panel Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        panel.setBackground(Elements.BEIGE);

        // Card number input panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(Elements.BEIGE);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setForeground(Color.BLACK);
        cardLabel.setFont(new Font("Arial",Font.BOLD,15));
        cardLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        cardPanel.add(cardLabel, BorderLayout.NORTH);
        cardPanel.add(cardField, BorderLayout.CENTER);
        cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // PIN input panel
        JPanel pinPanel = new JPanel();
        pinPanel.setLayout(new BorderLayout());
        pinPanel.setBackground(Elements.BEIGE);
        pinPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setForeground(Color.BLACK);
        pinLabel.setFont(new Font("Arial",Font.BOLD,15));
        pinLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        pinPanel.add(pinLabel, BorderLayout.NORTH);
        pinPanel.add(pinField, BorderLayout.CENTER);
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Add all components to main panel
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cardPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(pinPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(login);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(backButton);

        dialog.getContentPane().setBackground(Elements.BEIGE);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(status, BorderLayout.SOUTH);
        dialog.setVisible(true);

        return authenticatedCard;
    }
}

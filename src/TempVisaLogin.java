import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TempVisaLogin {
    private static TempVisaCard authenticatedCard = null;

    public static TempVisaCard showLogin() {
        List<TempVisaCard> cards = TempCardsLoader.loadCards("temporary_visa_cards.txt");

        JDialog dialog = new JDialog((Frame) null, "Temporary Visa Login", true);
        dialog.setSize(400, 420);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // Colors
        Color darkBrown = new Color(68, 16, 10);
        Color gold = new Color(155, 124, 68);

        // Components
        JTextField cardField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        JLabel status = new JLabel("Enter card credentials", SwingConstants.CENTER);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton login = new JButton("Login");
        login.setBackground(gold);
        login.setForeground(darkBrown);

        JButton backButton = new JButton("🔙 Back");
        backButton.setBackground(darkBrown);
        backButton.setForeground(gold);

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
            status.setText("❌ Invalid card or PIN.");
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.showTempVisaOptions();
        });

        // Panel Layout
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        panel.setBackground(darkBrown);

        JLabel cardLabel = new JLabel("Card Number:");
        JLabel pinLabel = new JLabel("PIN:");

        cardLabel.setForeground(Color.WHITE);
        pinLabel.setForeground(Color.WHITE);

        panel.add(cardLabel);
        panel.add(cardField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(login);
        panel.add(backButton);

        dialog.getContentPane().setBackground(darkBrown);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(status, BorderLayout.SOUTH);
        dialog.setVisible(true);

        return authenticatedCard;
    }
}
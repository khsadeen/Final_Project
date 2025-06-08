import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StandardLoginGUI {
    private static Cards authenticatedCard = null;

    public static Cards showStandardLogin() {
        CardsLoader loader = new CardsLoader();
        List<Cards> cards = loader.cardsListing();

        JDialog dialog = new JDialog((Frame) null, "Standard Login", true);
        dialog.setSize(400, 500);
//        dialog.getContentPane().setBackground(new Color(68, 16, 10));

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Logo label with image
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
        Image image = icon.getImage().getScaledInstance(400, 240, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(image));
        //--------------------------

        JTextField cardField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JLabel status = new JLabel("Enter credit details", SwingConstants.CENTER);
        status.setForeground(new Color(155, 124, 68));
        JButton login = new JButton("Login");
        login.setBackground(new Color(151, 23, 23));
        login.setForeground(new Color(155, 124, 68));
        JButton backButton = new JButton("Back to main menu");
        backButton.setBackground(new Color(151, 23, 23));
        backButton.setForeground(new Color(155, 124, 68));
        JButton switchVisaInputButton = new JButton("Switch to visa Input");
        switchVisaInputButton.setBackground(new Color(151, 23, 23));
        switchVisaInputButton.setForeground(new Color(155, 124, 68));

        switchVisaInputButton.addActionListener(e -> {
            dialog.dispose();
            authenticatedCard = VisaCardInput.showVisaInput();
        });

        login.addActionListener(e -> {
            String card = cardField.getText();
            String pass = new String(passField.getPassword());

            for (Cards c : cards) {
                if (c.cardNumber.equals(card) && c.password.equals(pass)) {
                    authenticatedCard = c;
                    JOptionPane.showMessageDialog(dialog, "✅ Welcome, " + c.ownerName);
                    dialog.dispose();
//                        new FeaturesMenu(authenticatedCard);
                }
            }
            status.setText("❌ Invalid. Try again.");
        });

        backButton.addActionListener(e -> {
            dialog.dispose(); // Close current dialog
            MainMenu.screen();
        });

        JPanel inputPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        JLabel cardNum = new JLabel("Card Number:");
        inputPanel.add(cardNum);
        cardNum.setForeground(new Color(155, 124, 68));
        inputPanel.add(cardField);
        JLabel password = new JLabel("Password:");
        inputPanel.add(password);
        password.setForeground(new Color(155, 124, 68));
        inputPanel.add(passField);
        inputPanel.add(login);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        Color babyBlue = new Color(155, 124, 68);
        Color richRed = new Color(151, 23, 23);
        Color darkBrown = new Color(244, 250, 255);

        buttonPanel.setBackground(babyBlue);
        buttonPanel.setForeground(richRed);

        buttonPanel.add(switchVisaInputButton, BorderLayout.NORTH);
        buttonPanel.add(inputPanel, BorderLayout.CENTER);
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        dialog.setLayout(new BorderLayout());
        dialog.add(logoLabel, BorderLayout.NORTH);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.add(status, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null);

        dialog.getContentPane().setBackground(darkBrown);
        inputPanel.setBackground(darkBrown);
        buttonPanel.setBackground(darkBrown);
        status.setBackground(darkBrown);
        status.setForeground(Color.BLACK);

        cardField.setBackground(Color.WHITE);
        cardField.setForeground(darkBrown);
        passField.setBackground(Color.WHITE);
        passField.setForeground(darkBrown);

        dialog.getContentPane().setBackground(darkBrown);
        inputPanel.setBackground(darkBrown);
        buttonPanel.setBackground(darkBrown);
        status.setBackground(darkBrown);
        status.setForeground(Color.BLACK);
        cardField.setBackground(Color.WHITE);
        cardField.setForeground(darkBrown);
        passField.setBackground(Color.WHITE);
        passField.setForeground(darkBrown);
        dialog.setVisible(true); // Blocks until closed
        return authenticatedCard;
    }
}
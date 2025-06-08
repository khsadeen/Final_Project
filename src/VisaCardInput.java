import jakarta.mail.MessagingException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VisaCardInput {
    static Cards authenticatedCard = null;
    JDialog dialog;
    List<Cards> cardsDatabase;
    JTextField cardNumberField;
    JTextField codeField;
    JLabel statusLabel;
    JButton verifyButton;
    JButton resendButton;
    Cards currentCard;
    String currentCode;
    int attempts = 3;

    public final Font uiFont = new Font("Arial", Font.PLAIN, 14);

    public VisaCardInput(List<Cards> cardsDatabase, JFrame parentFrame) {
        this.cardsDatabase = cardsDatabase;

        Color darkBrown = new Color(244, 250, 255);
        Color gold = new Color(151, 23, 23);
        Font uniFont = new Font("Arial",Font.BOLD,14);

        dialog = new JDialog(parentFrame, "Visa Card Input", true);
        dialog.setSize(400, 500);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());

        // Logo image
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
        Image image = icon.getImage().getScaledInstance(400, 240, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(image));
        dialog.add(logoLabel, BorderLayout.NORTH);

        // Input fields
        cardNumberField = new JTextField();
        cardNumberField.setBackground(Color.WHITE);
        cardNumberField.setForeground(Color.BLACK);
        cardNumberField.setFont(uniFont);

        codeField = new JTextField();
        codeField.setBackground(Color.WHITE);
        codeField.setForeground(Color.BLACK);
        codeField.setFont(uniFont);

        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setForeground(gold);
        cardLabel.setFont(uniFont);

        JLabel codeLabel = new JLabel("Verification Code:");
        codeLabel.setForeground(gold);
        codeLabel.setFont(uniFont);

        statusLabel = new JLabel("Enter your Visa card number", SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setBackground(darkBrown);
        statusLabel.setFont(uniFont);

        verifyButton = createStyledButton("Verify Code", gold, darkBrown);
        verifyButton.setEnabled(true);
        verifyButton.addActionListener(e -> verifyCode());

        resendButton = createStyledButton("Resend Code", gold, darkBrown);
        resendButton.setEnabled(false);
        resendButton.addActionListener(e -> resendCode());

        JButton sendCodeButton = createStyledButton("Send Verification Code", gold, darkBrown);
        sendCodeButton.addActionListener(e -> sendCode());

        JButton backButton = createStyledButton("Back to main menu", gold, darkBrown);
        backButton.addActionListener(e ->{
            dialog.dispose();
            MainMenu.screen();});

        JButton switchToStandardLoginButton = createStyledButton("switch to standard login",gold,darkBrown);
        switchToStandardLoginButton.addActionListener(e -> {
            dialog.dispose();
            authenticatedCard = StandardLoginGUI.showStandardLogin();
        });

        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        inputPanel.setBackground(darkBrown);

        inputPanel.add(cardLabel);
        inputPanel.add(cardNumberField);
        inputPanel.add(sendCodeButton);
        inputPanel.add(codeLabel);
        inputPanel.add(codeField);
        inputPanel.add(verifyButton);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(darkBrown);

        buttonPanel.add(switchToStandardLoginButton, BorderLayout.NORTH);
        buttonPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(resendButton);
        bottomPanel.add(backButton);

        buttonPanel.add(bottomPanel, BorderLayout.SOUTH);


        dialog.getContentPane().setBackground(darkBrown);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.add(statusLabel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial",Font.BOLD,12));
        button.setBorder(BorderFactory.createLineBorder(fg));
        return button;
    }

    public void sendCode() {
        String inputAccount = cardNumberField.getText().trim();
        for (Cards card : cardsDatabase) {
            if (card.cardNumber.equals(inputAccount)) {
                currentCard = card;
                currentCode = Email.generateVerificationCode();
                try {
                    Email.sendVerificationCode(card.email, currentCode);
                    statusLabel.setText("📩 Code sent to: " + card.email);
                    verifyButton.setEnabled(true);
                    resendButton.setEnabled(true);
                } catch (MessagingException e) {
                    statusLabel.setText("❌ Failed to send code.");
                }
                return;
            }
        }
        statusLabel.setText("❌ Account not found.");
    }

    public void resendCode() {
        if (currentCard != null) {
            try {
                currentCode = Email.generateVerificationCode();
                Email.sendVerificationCode(currentCard.email, currentCode);
                statusLabel.setForeground(Color.BLACK);
                statusLabel.setText(">> New code sent to: " + currentCard.email);
            } catch (MessagingException e) {
                statusLabel.setText("? Failed to resend code.");
            }
        }
    }

    private void verifyCode() {
        if (codeField.getText().equals(currentCode)) {
            authenticatedCard = currentCard;
            JOptionPane.showMessageDialog(dialog, "✅ Signed in successfully!");
            dialog.dispose();
        }
        else {
            attempts--;
            if (attempts == 0) {
                JOptionPane.showMessageDialog(dialog, "❌ Too many incorrect attempts.");
                dialog.dispose();
            } else {
                statusLabel.setText("❌ Incorrect code. Attempts left: " + attempts);
                resendCode();
            }
        }
    }


    public static Cards showVisaInput() {
        CardsLoader loader = new CardsLoader();
        List<Cards> database = loader.cardsListing();
        JFrame dummy = new JFrame();
        new VisaCardInput(database, dummy);
        return authenticatedCard;
    }
}
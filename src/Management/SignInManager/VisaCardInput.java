package Management.SignInManager;

import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Management.CardsManager.CardsLoader;
import Emails.Email;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;
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

    public VisaCardInput(List<Cards> cardsDatabase, JFrame parentFrame) {
        this.cardsDatabase = cardsDatabase;

        dialog = new JDialog(parentFrame, "Visa Card Input", true);
        dialog.setSize(460, 640);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Elements.BEIGE);

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image image = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 110, 10, 110));
            logoLabel.setBackground(Elements.BEIGE);
        } catch (Exception e) {
            System.out.println("❌ Could not load logo: " + e.getMessage());
        }

        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(Elements.BEIGE);
        logoPanel.setOpaque(true);
        logoPanel.add(logoLabel);

        // Status Label
        statusLabel = new JLabel("Enter your Visa card number", SwingConstants.CENTER);
        statusLabel.setFont(Elements.FONT_BOLD_16);
        statusLabel.setForeground(Elements.SEAWEED);
        statusLabel.setBackground(Elements.BEIGE);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fields
        cardNumberField = new JTextField();
        cardNumberField.setFont(Elements.FONT_PLAIN_14);
        cardNumberField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        codeField = new JTextField();
        codeField.setFont(Elements.FONT_PLAIN_14);
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setForeground(Color.BLACK);
        cardLabel.setFont(Elements.FONT_BOLD_16);
        cardLabel.setBackground(Elements.BEIGE);
        cardLabel.setOpaque(true);

        JLabel codeLabel = new JLabel("Verification Code:");
        codeLabel.setForeground(Color.BLACK);
        codeLabel.setFont(Elements.FONT_BOLD_16);
        codeLabel.setBackground(Elements.BEIGE);
        codeLabel.setOpaque(true);

        // Buttons
        verifyButton = createStyledButton("Verify Code");
        verifyButton.setBorder(new RoundedBorder(10));
        verifyButton.setEnabled(true);
        verifyButton.addActionListener(e -> verifyCode());

        resendButton = createStyledButton("Resend Code");
        resendButton.setBorder(new RoundedBorder(10));
        resendButton.setEnabled(false);
        resendButton.addActionListener(e -> resendCode());

        JButton sendCodeButton = createStyledButton("Send Verification Code");
        sendCodeButton.addActionListener(e -> sendCode());
        sendCodeButton.setBorder(new RoundedBorder(10));

        // Input Layout
        Box inputBox = Box.createVerticalBox();
        inputBox.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        inputBox.setBackground(Elements.BEIGE);
        inputBox.setOpaque(true);

        inputBox.add(labelAndField(cardLabel, cardNumberField));
        inputBox.add(Box.createVerticalStrut(8));
        inputBox.add(centeredButton(sendCodeButton));
        inputBox.add(Box.createVerticalStrut(15));
        inputBox.add(labelAndField(codeLabel, codeField));
        inputBox.add(Box.createVerticalStrut(8));
        inputBox.add(centeredButton(verifyButton));
        inputBox.add(Box.createVerticalStrut(10));
        inputBox.add(centeredButton(resendButton));
        inputBox.add(Box.createVerticalStrut(20));

        // Bottom Buttons
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.setIcon(Elements.resizedBackIcon);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setFont(Elements.FONT_PLAIN_14);
        backButton.setBackground(Elements.SOFT_GREEN);
        backButton.setBorder(new RoundedBorder(10));
        JButton switchToStandardLoginButton = createStyledButton("Switch to Standard Login");
        switchToStandardLoginButton.setBackground(Elements.SOFT_GREEN);
        switchToStandardLoginButton.setFont(Elements.FONT_PLAIN_14);
        switchToStandardLoginButton.setBorder(new RoundedBorder(10));

        Dimension buttonSize = new Dimension(200, 40);
        backButton.setPreferredSize(buttonSize);
        switchToStandardLoginButton.setPreferredSize(buttonSize);

        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        switchToStandardLoginButton.addActionListener(e -> {
            dialog.dispose();
            authenticatedCard = StandardLoginGUI.showStandardLogin();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Elements.BEIGE);
        buttonPanel.setOpaque(true);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(switchToStandardLoginButton);
        buttonPanel.add(Box.createHorizontalGlue());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Elements.BEIGE);
        bottomPanel.setForeground(Elements.SEAWEED);
        bottomPanel.setOpaque(true);
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(logoPanel, BorderLayout.NORTH);
        dialog.add(inputBox, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private JPanel centeredButton(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Elements.BEIGE);
        panel.add(button);
        return panel;
    }

    private JPanel labelAndField(JLabel label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(true);
        panel.setBackground(Elements.BEIGE);
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Elements.FONT_BOLD_16);
        button.setBackground(Elements.SEAWEED);
        button.setForeground(Elements.CHAMPAGNE);
        button.setBorder(new RoundedBorder(14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 40));
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
                statusLabel.setText("📩 New code sent to: " + currentCard.email);
            } catch (MessagingException e) {
                statusLabel.setText("❌ Failed to resend code.");
            }
        }
    }

    private void verifyCode() {
        if (codeField.getText().equals(currentCode)) {
            authenticatedCard = currentCard;
            JOptionPane.showMessageDialog(dialog, "√ Signed in successfully!");
            dialog.dispose();
        } else {
            attempts--;
            if (attempts == 0) {
                JOptionPane.showMessageDialog(dialog, "X Too many incorrect attempts.");
                dialog.dispose();
            } else {
                statusLabel.setText("X Incorrect code. Attempts left: " + attempts);
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
